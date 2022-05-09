import { HttpEvent, HttpEventType } from '@angular/common/http';
import { Component, OnInit, SecurityContext } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ImageService } from 'src/app/image.service';
import { getTitle } from 'src/app/utils/doctitle.util';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss'],
})
export class HomepageComponent implements OnInit {
  state: 'initial' | 'uploading' | 'waitingForResponse' = 'initial';
  progress: number = 0;
  isUploadError: boolean = false;

  srStatus: string = 'Upload progress: 0%';
  private readonly srStatusInterval: number = 5000; //ms

  imageSrc: SafeResourceUrl | undefined = undefined;
  image: File | undefined = undefined;
  imageName: string | undefined = undefined;

  constructor(
    private imageService: ImageService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    document.title = getTitle(document.title, 'Upload Image');
  }

  onFileChange(file: File): void {
    this.image = file;
    this.imageName = file.name;

    this.imageSrc = this.sanitizer.bypassSecurityTrustResourceUrl(
      URL.createObjectURL(file)
    );

    console.log('Homepage: onFileChange: file:', file);
  }

  onUse(event: Event): void {
    event.preventDefault();
    let timestamp = new Date().getMilliseconds();

    if (this.imageSrc) {
      this.imageService
        .setImage(this.imageSrc, this.imageName as string)
        .then((observer) => {
          observer.subscribe((event: HttpEvent<any>) => {
            this.state = 'uploading';

            switch (event.type) {
              case HttpEventType.UploadProgress:
                const progress = Math.round(
                  (event.loaded / (event.total as number)) * 100
                );
                if (progress > 99.9) this.state = 'waitingForResponse';
                this.progress = progress;

                // update SR-text if necessary
                timestamp = this.onUseForSR(timestamp, progress);
                break;
              case HttpEventType.Response:
                const status = event.status;
                if (status === 200) {
                  this.router.navigateByUrl('/edit');
                } else {
                  console.error(event);
                }
                break;
            }
          });
        });
      //
    }
  }

  onReset(event: Event): void {
    event.preventDefault();

    if (this.imageSrc) {
      // revoke created object URL to avoid memory leaks
      const url = this.sanitizer.sanitize(
        SecurityContext.RESOURCE_URL,
        this.imageSrc
      );

      url && URL.revokeObjectURL(url);
      this.imageSrc = undefined;
      this.image = undefined;
      this.imageName = undefined;
    }
  }

  private onUseForSR(prevTimestamp: number, progress: number): number {
    const timestampNow = new Date().getMilliseconds();
    if (progress === 100) {
      this.srStatus = 'Upload done. You will be redirected shortly';
      return timestampNow;
    } else if (timestampNow - prevTimestamp > this.srStatusInterval) {
      this.srStatus = `Upload progress: ${progress}%`;
      return timestampNow;
    }

    return prevTimestamp;
  }
}
