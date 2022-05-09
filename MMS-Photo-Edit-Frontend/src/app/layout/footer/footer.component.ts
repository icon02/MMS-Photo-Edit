import { Component, OnInit } from '@angular/core';
import packageJson from '../../../../package.json';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {
  appName: string = packageJson.appName;
  version: string = packageJson.version;

  constructor() {}

  ngOnInit(): void {}
}
