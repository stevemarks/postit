import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';

import { BrowserModule, By } from '@angular/platform-browser';
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
import { BasicAuthenticationService } from '../service/basic-authentication.service';
import { PostItService } from '../service/data/postit.service';
import { CookieService } from 'angular2-cookie';

describe('CreatePostItComponent', () => {
  let component: CreatePostItComponent;
  let fixture: ComponentFixture<CreatePostItComponent>;
  let mockBasicAuthenticationService: BasicAuthenticationService;
  let mockPostItService: PostItService;

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
        BasicAuthenticationService,
        PostItService,
        CookieService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreatePostItComponent);
    component = fixture.componentInstance;

    mockBasicAuthenticationService = TestBed.get(BasicAuthenticationService);
    mockPostItService = TestBed.get(PostItService);
    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form should be invalid on initial load', () => {
    const text = component.form.controls['text'];
    expect(text.value).toBeFalsy();
    expect(component.form.valid).toBeFalsy();
  });

  it('text form validity', () => {
    expect(component.form.valid).toBeFalsy();
    const text = component.form.controls['text'];
    expect(text.errors['required']).toBeTruthy();
    expect(text.errors['pattern']).toBeFalsy();
    expect(text.valid).toBeFalsy();

    text.setValue('D');
    expect(text.valid).toBeTruthy();

    text.setValue('Do');
    expect(text.valid).toBeTruthy();

    text.setValue('Do Laundry.');
    expect(text.errors).toBeNull();
    expect(text.valid).toBeTruthy();

    text.setValue('sdfjlskfhskjdfhskfjhaskjfhsjkdfhskjfhskljdfhlskjdhfkjsdhfsjkdhfjkdshfkjlsdhfkjhdsfjksdhflsdjkfhkjsdhfjksdhfjksdhfkjsdhflkjshdfkjsdhfjkhdfkljhsldjhfssdkjfhskjdfhksjdhfkjsdhfklsjdhflksjdhfkjsdhfkjsfhsjk');
    expect(text.errors).toBeNull();
    expect(text.valid).toBeTruthy();

    text.setValue('sdfjlskfhskjdfhskfjhaskjfhsjkdfhskjfhskljdfhlskjdhfkjsdhfsjkdhfjkdshfkjlsdhfkjhdsfjksdhflsdjkfhkjsdhfjksdhfjksdhfkjsdhflkjshdfkjsdhfjkhdfkljhsldjhfssdkjfhskjdfhksjdhfkjsdhfklsjdhflksjdhfkjsdhfkjsfhsjk>>>>>>>>>>..More than allowed.');
    expect(text.errors).toBeTruthy();
    expect(text.valid).toBeFalsy();
  });

  it('form should be reset after successful submittion', () => {
    const text = component.form.controls['text'];
    text.setValue('Do Laundry');

    component.onCreate();
    expect(text.value).toBeFalsy();
  });

  it('form should be reset after successful submittion', () => {
    component.isUserLoggedIn = true;

    const compiled = fixture.debugElement.nativeElement;
    let inputElement = compiled.querySelector('input');
    const text = component.form.controls['text'];

    text.setValue('valid text');
    fixture.detectChanges();
    inputElement = compiled.querySelector('input');
    expect(inputElement.value).toBeTruthy();

    component.onCreate();
    fixture.detectChanges();

    inputElement = compiled.querySelector('input');
    expect(inputElement.value).toBeFalsy();

    const buttonElement = compiled.querySelector('button');
    expect(buttonElement.disabled).toBeTruthy();
  });

  it('form submit button should be enabled once the form is valid', () => {
    component.isUserLoggedIn = true;

    const compiled = fixture.debugElement.nativeElement;
    let element = compiled.querySelector('button');
    const text = component.form.controls['text'];

    text.setValue('');
    fixture.detectChanges();
    element = compiled.querySelector('button');
    expect(element.disabled).toBeTruthy();

    text.setValue('valid text');
    fixture.detectChanges();
    element = compiled.querySelector('button');
    expect(element.disabled).toBeFalsy();
  });
});
