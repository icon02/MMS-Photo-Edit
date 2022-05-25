import {
  HttpClient,
  HttpHeaders,
  HttpParams,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable, SecurityContext } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { catchError, map, Observable } from 'rxjs';
import { SelectToolService } from './pages/editpage/components/image-editor/components/select-tools/select-tool.service';
import { Session } from './session.model';
import { SessionService } from './session.service';
import { DistinctBehaviorSubject } from './utils/DistinctBehaviorSubject';

const IMAGE_BASE_URL = 'http://localhost:8080/image';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  //
  private baseImageName: string | undefined = undefined;
  private baseImage: SafeResourceUrl | undefined = undefined;
  private images: SafeResourceUrl[] = [];

  private curImageIdx = -1;

  /* ==================== SUBJECTS ==================== */

  curImageSubject: DistinctBehaviorSubject<SafeResourceUrl | undefined> =
    new DistinctBehaviorSubject<SafeResourceUrl | undefined>(this.baseImage);
  allowsUndoSubject: DistinctBehaviorSubject<boolean> =
    new DistinctBehaviorSubject<boolean>(false);
  allowsRedoSubject: DistinctBehaviorSubject<boolean> =
    new DistinctBehaviorSubject<boolean>(false);
  /**
   * While isLoading === true, any function calls (except reset)
   * will not be executed
   */
  isLoadingSubject: DistinctBehaviorSubject<boolean> =
    new DistinctBehaviorSubject<boolean>(false);

  /* ==================== NETWORK OBJECTS ==================== */

  private defaultHeaders: HttpHeaders;

  /* ==================== CONSTRUCTOR ==================== */

  constructor(
    private sessionService: SessionService,
    private selectToolService: SelectToolService,
    private httpClient: HttpClient,
    private sanitizer: DomSanitizer
  ) {
    const session = this.sessionService.sessionBSubject.value;

    this.defaultHeaders = this.generateHeaders(session);
    this.sessionService.sessionBSubject.subscribe((newSession) => {
      this.defaultHeaders = this.generateHeaders(newSession);
    });
  }

  private generateHeaders(session: Session | null): HttpHeaders {
    let headers = new HttpHeaders();
    if (session) headers = headers.set('Authorization', session.id);

    return headers;
  }

  /* ==================== LOCAL METHODS ==================== */

  async setImage(
    imageUrl: SafeResourceUrl,
    imageName: string
  ): Promise<Observable<any>> {
    this.reset();
    this.baseImage = imageUrl;
    this.baseImageName = imageName;

    this.curImageIdx++;
    this.images.push(this.baseImage);
    this.curImageSubject.next(this.baseImage);

    const file: File = await this.getFileFromSafeUrl(imageUrl);
    const formData = new FormData();
    formData.append('image', file);

    const options = {
      headers: this.defaultHeaders,
      reportProgress: true,
      observe: 'event',
    };

    const request = new HttpRequest(
      'POST',
      'http://localhost:8080/image/use',
      formData,
      options
    );

    return this.httpClient.request<any>(request);
  }

  reset(): void {
    if (this.baseImage) {
      this.images = [this.baseImage];
      this.curImageIdx = 0;
      this.curImageSubject.next(this.baseImage);
    } else {
      this.baseImage = undefined;
      this.baseImageName = undefined;
      this.images = [];
      this.curImageIdx = -1;
      this.curImageSubject.next(undefined);
    }
  }

  undo(): void {
    if (!this.isLoadingSubject.value) {
      if (this.allowsUndoSubject.value) {
        this.curImageIdx = this.curImageIdx - 1;
        this.curImageSubject.next(this.images[this.curImageIdx]);

        this.refreshUndoSubject();
        this.refreshRedoSubject();
      }
    }
  }

  redo(): void {
    if (!this.isLoadingSubject.value) {
      if (this.allowsRedoSubject.value) {
        this.curImageIdx++;
        this.curImageSubject.next(this.images[this.curImageIdx]);

        this.refreshUndoSubject();
        this.refreshRedoSubject();
      }
    }
  }

  downloadCurrent(): Observable<any> {
    const options = {
      headers: this.defaultHeaders,
      observe: 'response',
      responseType: 'blob',
      reportProgress: true,
    };

    const request = new HttpRequest(
      'GET',
      IMAGE_BASE_URL + '/current',
      options
    );

    return this.httpClient.request<any>(request);
  }

  mirror(dir: 'vertical' | 'horizontal'): void {
    this.isLoadingSubject.next(true);

    const params = new HttpParams().set('dir', dir);

    this.httpClient
      .post(
        IMAGE_BASE_URL + '/mirror',
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
          params: params,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(false);
      });
  }

  rotate(rotation: number): void {
    this.isLoadingSubject.next(true);

    const params = new HttpParams().set('rotation', rotation);

    this.httpClient
      .post(
        IMAGE_BASE_URL + '/rotate',
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
          params: params,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(true);
      });
  }

  adaptRGB(r: number, g: number, b: number): void {
    this.isLoadingSubject.next(true);

    const params = new HttpParams().set('r', r).set('g', g).set('b', b);

    this.httpClient
      .post(
        IMAGE_BASE_URL + '/rgb',
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
          params: params,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(true);
      });
  }

  greyscale(): void {
    this.isLoadingSubject.next(true);

    this.httpClient
      .post(
        IMAGE_BASE_URL + '/greyscale',
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(true);
      });
  }

  adaptBrightness(
    val: number,
    type: 'all' | 'dark' | 'bright' = 'bright'
  ): void {
    this.isLoadingSubject.next(true);

    let url = IMAGE_BASE_URL + '/brightness';
    switch (type) {
      case 'all':
        break;
      case 'dark':
        url += '/dark';
        break;
      case 'bright':
        url += '/bright';
        break;
    }

    const params = new HttpParams().set('val', val);

    this.httpClient
      .post(
        url,
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
          params: params,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(true);
      });
  }

  blur(variance: number): void {
    this.isLoadingSubject.next(true);

    const params = new HttpParams().set('variance', variance);

    this.httpClient
      .post(
        IMAGE_BASE_URL + '/blur',
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
          params: params,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(true);
      });
  }

  colorInvert(): void {
    this.isLoadingSubject.next(true);

    this.httpClient
      .post(
        IMAGE_BASE_URL + '/color-invert',
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(false);
      });
  }

  colorizeEdges(threshold: number, bgColor: string, edgeColor: string): void {
    this.isLoadingSubject.next(true);

    const params = new HttpParams()
      .set('threshold', threshold)
      .set('bg-color', bgColor)
      .set('edge-color', edgeColor);

    this.httpClient
      .post(
        IMAGE_BASE_URL + '/edge-colorize',
        this.selectToolService.curSelection
          ? {
              ...this.selectToolService.curSelection,
              canvasWidth: this.selectToolService.canvasWidth,
              canvasHeight: this.selectToolService.canvasHeight,
            }
          : {},
        {
          headers: this.defaultHeaders,
          observe: 'response',
          responseType: 'blob',
          withCredentials: true,
          params: params,
        }
      )
      .subscribe((res) => {
        this.setNextImage(res);
        this.isLoadingSubject.next(false);
      });
  }

  /* ==================== PRIVATE METHODS ==================== */

  private refreshUndoSubject(): void {
    if (this.curImageIdx > 0) this.allowsUndoSubject.next(true);
    else this.allowsUndoSubject.next(false);
  }

  private refreshRedoSubject(): void {
    if (this.curImageIdx >= this.images.length)
      this.allowsRedoSubject.next(false);
    else this.allowsRedoSubject.next(true);
  }

  private async getFileFromSafeUrl(imageUrl: SafeResourceUrl): Promise<File> {
    const url = this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, imageUrl);

    return fetch(url as string)
      .then((response) => response.blob())
      .then((blob) => new File([blob], this.baseImageName as string));
  }

  private setNextImage(response: HttpResponse<Blob>): void {
    if (response.status === 200 && response.body) {
      const unsafeUrl = window.URL.createObjectURL(response.body);
      const safeImgUrl =
        this.sanitizer.bypassSecurityTrustResourceUrl(unsafeUrl);
      this.images.push(safeImgUrl);
      this.curImageSubject.next(safeImgUrl);

      // TODO logic for allowUndoSubject
    }
  }
}
