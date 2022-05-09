import { Component, OnDestroy, OnInit } from '@angular/core';
import { SessionService } from './session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  constructor(private sessionService: SessionService) {}

  ngOnInit(): void {
    this.sessionService.createSession();
  }

  ngOnDestroy(): void {
    this.sessionService.closeSession();
  }
}
