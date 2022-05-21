import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-greyscale-manipulation',
  templateUrl: './greyscale-manipulation.component.html',
  styleUrls: ['./greyscale-manipulation.component.scss'],
})
export class GreyscaleManipulationComponent implements OnInit {
  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  onApply(): void {
    this.imageService.greyscale();
  }
}
