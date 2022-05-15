import { Component, OnInit } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ImageService } from 'src/app/image.service';

@Component({
  selector: 'app-editpage',
  templateUrl: './editpage.component.html',
  styleUrls: ['./editpage.component.scss'],
})
export class EditPageComponent implements OnInit {
  imageSrc!: SafeResourceUrl | undefined;
  disabled!: boolean;

  constructor(private imageService: ImageService, private router: Router) {}

  ngOnInit(): void {
    this.imageSrc = this.imageService.curImageSubject.value;
    if (!this.imageSrc) {
      console.log('editpage: imageSrc:', this.imageSrc);
      console.log('editpage: imageService:', this.imageService);
      this.router.navigateByUrl('/');
    }
    this.imageService.curImageSubject.subscribe((img) => (this.imageSrc = img));

    this.imageService.isLoadingSubject.subscribe(
      (val) => (this.disabled = this.disabled)
    );
  }
}
