import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-preview-apply-group',
  templateUrl: './preview-apply-group.component.html',
})
export class PreviewApplyGroupComponent implements OnInit {
  constructor() {}

  @Output('onPreviewClicked')
  onPreviewEmitter: EventEmitter<MouseEvent> = new EventEmitter<MouseEvent>();

  @Output('onApplyClicked')
  onApplyEmitter: EventEmitter<MouseEvent> = new EventEmitter<MouseEvent>();

  ngOnInit(): void {}

  onPreview(event: MouseEvent): void {
    this.onPreviewEmitter.emit(event);
  }

  onApply(event: MouseEvent): void {
    this.onApplyEmitter.emit(event);
  }
}
