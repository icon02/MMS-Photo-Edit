import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-brightness-manipulation',
  templateUrl: './brightness-manipulation.component.html',
  styleUrls: ['./brightness-manipulation.component.scss'],
})
export class BrightnessManipulationComponent implements OnInit {
  generalBrightness: number = 0;

  constructor() {}

  ngOnInit(): void {}
}
