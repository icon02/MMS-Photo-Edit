import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-edge-colorization-manipulation',
  templateUrl: './edge-colorization-manipulation.component.html',
  styleUrls: ['./edge-colorization-manipulation.component.scss'],
})
export class EdgeColorizationManipulationComponent implements OnInit {
  threshold: number = 0;
  bgColor: string = '#000000';
  edgeColor: string = '#057452';

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  onThresholdChange(e: Event): void {
    this.threshold = Number.parseInt((e.target as HTMLInputElement).value);
  }

  onApply(): void {
    this.imageService.colorizeEdges(
      this.threshold,
      this.bgColor,
      this.edgeColor
    );
  }
}
