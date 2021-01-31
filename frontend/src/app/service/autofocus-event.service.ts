import { API_URL } from './../app.constants';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { CookieService } from 'angular2-cookie/core';
import { Subject } from 'rxjs';
import { Observable } from 'rxjs';

export const TOKEN = 'token';
export const AUTHENTICATED_USER = 'authenticaterUser';

@Injectable({ providedIn: 'root' })
export class AutoFocusEventService {
    private subject = new Subject<any>();

    event(event: string) {
        this.subject.next({ text: event });
    }

    clearEvents() {
        this.subject.next();
    }

    getEvents(): Observable<any> {
        return this.subject.asObservable();
    }
}