import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Session } from './session.model';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  sessionBSubject: BehaviorSubject<Session | null> =
    new BehaviorSubject<Session | null>(null);

  constructor(private http: HttpClient) {}

  createSession(): void {
    this.http
      .get<Session>('http://localhost:8080/sessions/free')
      .subscribe((session) => {
        this.sessionBSubject.next(session);
      });
  }

  closeSession(): Promise<any> {
    return fetch('http://localhost:8080/sessions/close', { method: 'DELETE' });
  }
}
