import { Component, OnInit } from '@angular/core';
import packageJson from '../../../../package.json';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  appName: string = packageJson.appName;

  constructor() {}

  ngOnInit(): void {}
}
