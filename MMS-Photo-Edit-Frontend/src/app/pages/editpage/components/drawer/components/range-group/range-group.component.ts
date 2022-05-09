import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';

@Component({
  selector: 'app-range-group',
  templateUrl: './range-group.component.html',
  styleUrls: ['./range-group.component.scss'],
})
export class RangeGroupComponent implements OnInit {
  //
  /* ==================== INPUR ==================== */
  @Input('label')
  label: string = '';

  @Input('name')
  name: string = '';

  @Input('defaultValue')
  defaultValue: number = 0;
  @Input('minValue')
  minValue: number = -100;
  @Input('maxValue')
  maxValue: number = 100;

  @Output('onChange')
  onValueChange: EventEmitter<Event> = new EventEmitter<Event>();

  value: number = 0;

  constructor() {}

  ngOnInit(): void {
    this.value = this.defaultValue;
  }

  checkBoundaries(event: Event): void {
    const inputEl = event.target as HTMLInputElement;
    if ((inputEl.value as any) > this.maxValue)
      (inputEl.value as any) = this.maxValue;
    else if ((inputEl.value as any) < this.minValue)
      (inputEl.value as any) = this.minValue;
  }

  onChange(e: Event): void {
    this.checkBoundaries(e);
    this.onValueChange.emit(e);
  }
}
