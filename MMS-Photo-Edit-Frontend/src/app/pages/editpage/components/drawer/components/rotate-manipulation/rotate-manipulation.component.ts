import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-rotate-manipulation',
  templateUrl: './rotate-manipulation.component.html',
  styleUrls: ['./rotate-manipulation.component.scss'],
})
export class RotateManipulationComponent implements OnInit {
  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  rotate90Left(): void {
    this.imageService.rotate(270);
  }

  rotate90Right(): void {
    this.imageService.rotate(90);
  }

  rotate180(): void {
    this.imageService.rotate(180);
  }
}
