import {
  Directive,
  ElementRef,
  EventEmitter,
  HostListener,
  Output,
} from '@angular/core';

@Directive({
  selector: '[appClickedOutside]',
})
export class ClickedOutsideDirective {
  @Output() clickOutsideEmitter: EventEmitter<Event> =
    new EventEmitter<Event>();

  constructor(private elementRef: ElementRef) {}

  @HostListener('document:click', ['$event'])
  onClick(event: Event): void {
    const clickedInside = this.elementRef.nativeElement.contains(event.target);
    if (!clickedInside) this.clickOutsideEmitter.emit(event);
  }
}
