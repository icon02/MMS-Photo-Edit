import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-rgb-manipulation',
  templateUrl: './rgb-manipulation.component.html',
  styleUrls: ['./rgb-manipulation.component.scss'],
})
export class RgbManipulationComponent implements OnInit {
  red: number = 0;
  green: number = 0;
  blue: number = 0;

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  onRedChange(e: Event): void {
    this.red = Number.parseInt((e.target as HTMLInputElement).value);
  }

  onGreenChange(e: Event): void {
    this.green = Number.parseInt((e.target as HTMLInputElement).value);
  }

  onBlueChange(e: Event): void {
    this.blue = Number.parseInt((e.target as HTMLInputElement).value);
  }

  onApply(): void {
    this.imageService.adaptRGB(this.red, this.green, this.blue);
  }
}
