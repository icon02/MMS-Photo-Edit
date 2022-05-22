import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-brightness-manipulation',
  templateUrl: './brightness-manipulation.component.html',
  styleUrls: ['./brightness-manipulation.component.scss'],
})
export class BrightnessManipulationComponent implements OnInit {
  private generalBrightness: number = 0;
  private darkBrightness: number = 0;
  private brightBrightness: number = 0;

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  onGeneralBrightnessChange(e: Event): void {
    this.generalBrightness = Number.parseInt(
      (e.target as HTMLInputElement).value
    );
  }

  onGeneralBrightnessApply(): void {
    this.imageService.adaptBrightness(this.generalBrightness, 'all');
  }

  onDarkBrightnessChange(e: Event): void {
    this.darkBrightness = Number.parseInt((e.target as HTMLInputElement).value);
  }

  onDarkBrightnessApply(): void {
    this.imageService.adaptBrightness(this.darkBrightness, 'dark');
  }

  onBrightBrightnessChange(e: Event): void {
    this.brightBrightness = Number.parseInt(
      (e.target as HTMLInputElement).value
    );
  }

  onBrightBrightnessApply(): void {
    this.imageService.adaptBrightness(this.brightBrightness, 'bright');
  }
}
