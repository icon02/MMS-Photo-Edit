import { BehaviorSubject } from 'rxjs';

export class DistinctBehaviorSubject<T> extends BehaviorSubject<T> {
  /**
   * Only overrides the current value, if the
   * new value is not equal to the current value
   *
   * @param value: new value
   */
  override next(value: T): void {
    if (this.value !== value) super.next(value);
  }
}
