import {
  HttpClient,
  HttpEvent,
  HttpHeaders,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable, SecurityContext } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { Session } from './session.model';
import { SessionService } from './session.service';
import { DistinctBehaviorSubject } from './utils/DistinctBehaviorSubject';

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
      'http://localhost:8080/images/use',
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
      'http://localhost:8080/images/current',
      options
    );

    return this.httpClient.request<any>(request);
  }

  /* ==================== NETWORK METHODS ==================== */
  // TODO add methods

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
}
