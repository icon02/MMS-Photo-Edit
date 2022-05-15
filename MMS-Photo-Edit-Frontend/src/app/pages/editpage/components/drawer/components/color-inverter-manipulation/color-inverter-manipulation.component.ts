import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-color-inverter-manipulation',
  templateUrl: './color-inverter-manipulation.component.html',
  styleUrls: ['./color-inverter-manipulation.component.scss'],
})
export class ColorInverterManipulationComponent implements OnInit {
  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  onApply(): void {
    this.imageService.colorInvert();
  }

  onPreview(): void {
    // TODO
  }
}
