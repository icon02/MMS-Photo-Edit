import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-mirror-manipulation',
  templateUrl: './mirror-manipulation.component.html',
  styleUrls: ['./mirror-manipulation.component.scss'],
})
export class MirrorManipulationComponent implements OnInit {
  constructor(private imageService: ImageService) {}

  ngOnInit(): void {}

  onHorizontalClicked(): void {
    this.imageService.mirror('horizontal');
  }

  onVerticalClicked(): void {
    this.imageService.mirror('vertical');
  }
}
