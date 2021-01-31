import { TestBed } from '@angular/core/testing';

import { HttpIntercepterBasicAuthService } from './http-intercepter-basic-auth.service';

import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from '../../app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';

import { AppComponent } from '../../app.component';
import { ListPostitsComponent } from '../../list-postits/list-postits.component';
import { ErrorComponent } from '../../error/error.component';
import { LoginComponent } from '../../login/login.component';
import { LogoutComponent } from '../../logout/logout.component';
import { PostItComponent } from '../../posit/postit.component';
import { MenuComponent } from '../../menu/menu.component';
import { CreatePostItComponent } from '../../create-postit/create-postit.component';
import { CookieService } from 'angular2-cookie';

describe('HttpIntercepterBasicAuthService', () => {
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
  }
  );

  it('should be created', () => {
    const service: HttpIntercepterBasicAuthService = TestBed.get(HttpIntercepterBasicAuthService);
    expect(service).toBeTruthy();
  });
});
