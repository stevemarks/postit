import { HttpIntercepterBasicAuthService } from './service/http/http-intercepter-basic-auth.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { ErrorComponent } from './error/error.component';
import { ListPostitsComponent } from './list-postits/list-postits.component';
import { MenuComponent } from './menu/menu.component';
import { LogoutComponent } from './logout/logout.component';
import { PostItComponent } from './posit/postit.component';
import { CreatePostItComponent } from './create-postit/create-postit.component';
import { CookieService } from 'angular2-cookie/core';
import { AutofocusDirective } from './directive/autofocus.directive';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ErrorComponent,
    ListPostitsComponent,
    MenuComponent,
    LogoutComponent,
    PostItComponent,
    CreatePostItComponent,
    AutofocusDirective
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpIntercepterBasicAuthService, multi: true },
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
