import { TestBed } from '@angular/core/testing';

import { RouteGuardService } from './route-guard.service';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from '../app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';

import { AppComponent } from '../app.component';
import { ListPostitsComponent } from '../list-postits/list-postits.component';
import { ErrorComponent } from '../error/error.component';
import { LoginComponent } from '../login/login.component';
import { LogoutComponent } from '../logout/logout.component';
import { PostItComponent } from '../posit/postit.component';
import { MenuComponent } from '../menu/menu.component';
import { CreatePostItComponent } from '../create-postit/create-postit.component';
import { BasicAuthenticationService } from './basic-authentication.service';
import { Router } from '@angular/router';
import { CookieService } from 'angular2-cookie';


describe('RouteGuardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterTestingModule
      ],
      declarations: [
        ErrorComponent,
        LoginComponent,
        CreatePostItComponent,
        ListPostitsComponent,
        PostItComponent,
        MenuComponent,
        LogoutComponent,
        AppComponent
      ],
      providers: [
        CookieService
      ]
    });
  });

  it('should be created', () => {
    const service: RouteGuardService = TestBed.get(RouteGuardService);
    expect(service).toBeTruthy();
  });

  it('should redirect users if they are not logged in', () => {
    const service: RouteGuardService = TestBed.get(RouteGuardService);
    const basicAuthenticationService = TestBed.get(BasicAuthenticationService);
    const router = TestBed.get(Router);

    spyOn(basicAuthenticationService, 'isUserLoggedIn').and.returnValue(false);
    const fakeNavigateFunction = (location) => { };
    spyOn(router, 'navigate').and.callFake(fakeNavigateFunction);

    expect(service.canActivate(null, null)).toBeFalsy();
    expect(router.navigate.calls.mostRecent().args).toEqual([['login']]);
  });

  it('should allow access to route if user is logged in', () => {
    const service: RouteGuardService = TestBed.get(RouteGuardService);
    const basicAuthenticationService = TestBed.get(BasicAuthenticationService);

    spyOn(basicAuthenticationService, 'isUserLoggedIn').and.returnValue(true);
    expect(service.canActivate(null, null)).toBeTruthy();
  });
});
