import { API_URL } from './../app.constants';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { CookieService } from 'angular2-cookie/core';

export const TOKEN = 'token';
export const AUTHENTICATED_USER = 'authenticaterUser';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthenticationService {

  private rememberMeCookieStorage = 'rememberMe';
  userActivityEvent = new EventEmitter<string>();
  constructor(private http: HttpClient,
    private _cookieService: CookieService) {
    const isRememberMe = this._cookieService.get(this.rememberMeCookieStorage);
    if (!isRememberMe) {
      this.logout();
    }
  }

  executeAuthenticationService(username, password, rememberMe) {
    const basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);
    const headers = new HttpHeaders({ Authorization: basicAuthHeaderString });

    return this.http.get<AuthenticationBean>(
      `${API_URL}/basicauth`,
      { headers }).pipe(
        map(
          data => {
            sessionStorage.setItem(AUTHENTICATED_USER, username);
            sessionStorage.setItem(TOKEN, basicAuthHeaderString);
            this._cookieService.put(this.rememberMeCookieStorage, rememberMe);

            this.userActivityEvent.emit(username);
            return data;
          }
        )
      );
  }

  getAuthenticatedUser() {
    return sessionStorage.getItem(AUTHENTICATED_USER);
  }

  getAuthenticatedToken() {
    if (this.getAuthenticatedUser()) {
      return sessionStorage.getItem(TOKEN);
    }
  }

  isUserLoggedIn() {
    const user = sessionStorage.getItem(AUTHENTICATED_USER);
    return !(user === null);
  }

  logout() {
    sessionStorage.removeItem(AUTHENTICATED_USER);
    sessionStorage.removeItem(TOKEN);
    this.userActivityEvent.emit();
  }
}

export class AuthenticationBean {
  constructor(public message: string) { }
}
