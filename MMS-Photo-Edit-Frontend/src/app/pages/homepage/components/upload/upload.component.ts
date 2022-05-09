import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss'],
})
export class UploadComponent implements OnInit, OnChanges {
  //
  componentState: 'initial' | 'dragOver' = 'initial';

  @ViewChild('imginput')
  imageInput!: ElementRef<HTMLInputElement>;

  @Output('onChange')
  onChangeEmitter: EventEmitter<File> = new EventEmitter<File>();

  constructor() {}

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    console.log('changes', changes);
  }

  @HostListener('dragover', ['$event'])
  onDragOver(event: Event): void {
    event.preventDefault();
    this.componentState = 'dragOver';
  }

  @HostListener('dragleave', ['$event'])
  onDragLeave(event: Event): void {
    event.preventDefault();
    this.componentState = 'initial';
  }

  @HostListener('dragend', ['$event'])
  onDragEnd(event: Event): void {
    event.preventDefault();
    this.componentState = 'initial';
  }

  @HostListener('drop', ['$event'])
  onDrop(e: Event): void {
    const event = e as DragEvent;
    event?.preventDefault();
    event?.stopPropagation();

    const file = event?.dataTransfer?.files[0];
    if (file) this.changeImage(file);
  }

  @HostListener('keyup', ['$event'])
  onKeyUp(event: KeyboardEvent): void {
    if (event.key === 'Enter' || event.which === 13 || event.keyCode === 13) {
      if (!event.shiftKey) {
        event.preventDefault();
        this.onCustomInputClick();
      }
    } else if (
      event.key === 'Space' ||
      event.which === 32 ||
      event.keyCode === 32
    ) {
      if (!event.shiftKey) {
        event.preventDefault();
        this.onCustomInputClick();
      }
    }
  }

  onCustomInputClick(): void {
    this.imageInput.nativeElement.click();
  }

  onInputChange(e: Event): void {
    if (this.imageInput.nativeElement.files) {
      const image: File = Array.from(this.imageInput.nativeElement.files)[0];
      this.changeImage(image);
    }
  }

  private changeImage(file: File): void {
    this.onChangeEmitter.emit(file);
  }
}
