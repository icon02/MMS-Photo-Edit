import { HttpEvent, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-default-functions',
  templateUrl: './default-functions.component.html',
  styleUrls: ['./default-functions.component.scss'],
})
export class DefaultFunctionsComponent implements OnInit {
  allowUndo!: boolean;
  allowRedo!: boolean;

  isDownloading: boolean = false;
  downloadProgress: number = 0;
  srDownloadStatus: string = '';
  private readonly srStatusInterval: number = 5000; // ms

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {
    this.allowUndo = this.imageService.allowsUndoSubject.value;
    this.imageService.allowsUndoSubject.subscribe(
      (val) => (this.allowUndo = val)
    );

    this.allowRedo = this.imageService.allowsRedoSubject.value;
    this.imageService.allowsRedoSubject.subscribe(
      (val) => (this.allowRedo = val)
    );
  }

  onResetClicked(): void {
    this.imageService.reset();
  }

  onUndoClicked(): void {
    if (this.allowUndo) this.imageService.undo();
  }

  onRedoClicked(): void {
    if (this.allowRedo) this.imageService.redo();
  }

  onDownloadClicked(): void {
    // for sr-purpose
    let timestamp = new Date().getMilliseconds();

    this.imageService.downloadCurrent().subscribe((event: HttpEvent<any>) => {
      this.isDownloading = true;

      switch (event.type) {
        case HttpEventType.DownloadProgress:
          this.downloadProgress = Math.round(
            (event.loaded / (event.total as number)) * 100
          );

          // update SR-text if necessary
          timestamp = this.onUseForSR(timestamp, this.downloadProgress);

          break;
        case HttpEventType.Response:
          const httpResponse = event as HttpResponse<any>;
          httpResponse.headers.keys(); // lazy load
          const cd = httpResponse.headers.get('content-disposition');

          console.log('content disposition', cd);
          const filename = this.cdToFilename(cd as string);
          console.log('filename', filename);
          const blob = httpResponse.body as Blob;

          const downloadURL = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = filename;
          link.click();

          console.log('downloadLink', link);

          this.isDownloading = false;
          this.downloadProgress = 0;
          this.srDownloadStatus = '';
      }
    });
  }

  private cdToFilename(cd: string): string {
    const tmp = cd.split('=')[1];

    return tmp.substring(1, tmp.length - 1); // remove first and last char
  }

  private onUseForSR(prevTimestamp: number, progress: number): number {
    const timestampNow = new Date().getMilliseconds();
    if (progress === 100) {
      this.srDownloadStatus =
        'Download done. Your file will be ready in a moment';
      return timestampNow;
    } else if (timestampNow - prevTimestamp > this.srStatusInterval) {
      this.srDownloadStatus = `Download progress: ${progress}%`;
      return timestampNow;
    }

    return prevTimestamp;
  }
}
