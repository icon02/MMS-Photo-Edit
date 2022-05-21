import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-blur-manipulation',
  templateUrl: './blur-manipulation.component.html',
  styleUrls: ['./blur-manipulation.component.scss'],
})
export class BlurManipulationComponent implements OnInit {
  private variance: number = 0;

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  onVarianceChange(e: Event): void {
    this.variance = Number.parseInt((e.target as HTMLInputElement).value);
  }

  onApply(): void {
    this.imageService.blur(this.variance);
  }
}
