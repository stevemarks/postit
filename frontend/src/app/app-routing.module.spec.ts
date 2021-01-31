import { RouteGuardService } from './service/route-guard.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

import { AppComponent } from './app.component';
import { ListPostitsComponent } from './list-postits/list-postits.component';
import { ErrorComponent } from './error/error.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { PostItComponent } from './posit/postit.component';
import { MenuComponent } from './menu/menu.component';
import { CreatePostItComponent } from './create-postit/create-postit.component';

import { routes } from './app-routing.module';
import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import {
  BrowserDynamicTestingModule,
  platformBrowserDynamicTesting
} from '@angular/platform-browser-dynamic/testing';

import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { CookieService } from 'angular2-cookie';

describe('Routing', () => {

  let location: Location;
  let router: Router;
  let fixture;

  beforeEach(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
      platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        RouterTestingModule.withRoutes(routes)],
      declarations: [ErrorComponent,
        LoginComponent,
        CreatePostItComponent,
        ListPostitsComponent,
        PostItComponent,
        MenuComponent,
        LogoutComponent,
        AppComponent],
        providers: [
          CookieService
        ]
    });

    router = TestBed.get(Router);
    location = TestBed.get(Location);
    fixture = TestBed.createComponent(AppComponent);
    router.initialNavigation();
  });

  it('navigate to login on initial load', fakeAsync(() => {
    router.navigate(['']);
    tick();
    expect(location.path()).toBe('/login');
  }));

  it('an authenticated user should be able to navigate to /logout', fakeAsync(() => {
    const guard = TestBed.get(RouteGuardService);
    spyOn(guard, 'canActivate').and.returnValue(true);
    router.navigate(['logout']);
    tick();
    expect(location.path()).toBe('/logout');
  }));

  it('an un-authenticated user should NOT be able to navigate to /logout', fakeAsync(() => {
    const guard = TestBed.get(RouteGuardService);
    spyOn(guard, 'canActivate').and.returnValue(false);
    router.navigate(['logout']);
    tick();
    expect(location.path()).toBe('/');
  }));
});
