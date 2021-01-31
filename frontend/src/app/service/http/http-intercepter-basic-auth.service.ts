import { BasicAuthenticationService } from './../basic-authentication.service';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HttpIntercepterBasicAuthService implements HttpInterceptor {

  constructor(
    private basicAuthenticationService: BasicAuthenticationService
  ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    const basicAuthHeaderString = this.basicAuthenticationService.getAuthenticatedToken();
    const username = this.basicAuthenticationService.getAuthenticatedUser();
    const hostname = window.location.hostname;

    if (basicAuthHeaderString && username) {
      request = request.clone({
        setHeaders: {
          Tenant: hostname,
          Authorization: basicAuthHeaderString
        }
      });
    } else {
      request = request.clone({
        setHeaders: {
          Tenant: hostname
        }
      });
    }
    return next.handle(request);
  }
}
