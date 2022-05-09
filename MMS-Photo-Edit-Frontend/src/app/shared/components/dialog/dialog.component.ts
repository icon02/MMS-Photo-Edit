import {
  AfterViewChecked,
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

// see https://gomakethings.com/how-to-get-the-first-and-last-focusable-elements-in-the-dom/
const focusableQuery =
  'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';

// see wai-aria-examples for dialogs https://www.w3.org/TR/wai-aria-practices/examples/dialog-modal/dialog.html
@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
})
export class DialogComponent implements OnInit, AfterViewChecked, OnChanges {
  @Input('show')
  show: boolean = true;

  @Output('onClose')
  onCloseEmitter: EventEmitter<Event> = new EventEmitter<Event>();

  @ViewChild('root', { read: ElementRef })
  private rootRef!: ElementRef;

  private prevFocusedElement?: HTMLElement | null;
  private firstFocusableElement?: HTMLElement;
  private lastFocusableElement?: HTMLElement;

  private viewInitialized: boolean = false;

  constructor() {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.viewInitialized) {
      console.log('changes', changes);

      if (changes['show']) {
        this.prevFocusedElement = document.activeElement
          ? (document.activeElement as HTMLElement)
          : null;

        if (
          changes['show'].previousValue != changes['show'].currentValue &&
          changes['show'].currentValue
        ) {
          setTimeout(() => this.firstFocusableElement?.focus(), 0);
        } else if (
          changes['show'].previousValue != changes['show'].currentValue &&
          !changes['show'].currentValue
        ) {
          console.log('prevFocusedElement', this.prevFocusedElement);
          setTimeout(() => this.prevFocusedElement?.focus(), 0);
        }
      }
    } else {
      // initialize focusable elements
      this.initialzieView();
    }
  }

  ngOnInit(): void {}

  ngAfterViewChecked(): void {
    if (!this.viewInitialized) this.initialzieView();
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyPress(event: KeyboardEvent) {
    if (this.viewInitialized && this.show) {
      if (event.key === 'Tab' || event.keyCode === 9 || event.which === 9) {
        this.onTabPress(event);
      } else if (
        event.key === 'Escape' ||
        event.keyCode === 27 ||
        event.which === 9
      ) {
        this.onEscapePress(event);
      }
    }
  }

  private initialzieView(): void {
    if (this.rootRef && this.rootRef.nativeElement) {
      const focusableElements: HTMLElement[] =
        this.rootRef.nativeElement.querySelectorAll(focusableQuery);

      this.firstFocusableElement = focusableElements[0];
      this.lastFocusableElement =
        focusableElements[focusableElements.length - 1];

      console.log('focusableElements', focusableElements);

      if (this.show) {
        console.log('show');
        this.prevFocusedElement = document.activeElement
          ? (document.activeElement as HTMLElement)
          : null;
        this.firstFocusableElement?.focus();
      }

      this.viewInitialized = true;
      console.log('view initialized..');
    }
  }

  private onTabPress(event: KeyboardEvent) {
    if (
      event.shiftKey &&
      document.activeElement === this.firstFocusableElement
    ) {
      this.lastFocusableElement?.focus();
      event.preventDefault();
    } else if (
      document.activeElement === this.lastFocusableElement &&
      !event.shiftKey
    ) {
      this.firstFocusableElement?.focus();
      event.preventDefault();
    }
  }

  private onEscapePress(event: KeyboardEvent) {
    // only do the close event if there is a subscriber
    // to onClose
    if (this.onCloseEmitter.observed) {
      this.prevFocusedElement?.focus();
      this.onCloseEmitter.emit(event);
    }
  }

  public onClose(event: Event): void {
    this.onCloseEmitter.emit(event);
  }
}
