import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-rgb-manipulation',
  templateUrl: './rgb-manipulation.component.html',
  styleUrls: ['./rgb-manipulation.component.scss'],
})
export class RgbManipulationComponent implements OnInit {
  red: number = 0;
  green: number = 0;
  blue: number = 0;

  constructor() {}

  ngOnInit(): void {}

  onSubmit(event: Event): void {
    console.log('rgb-manipulation: onSubmit.event', event);
  }
}
