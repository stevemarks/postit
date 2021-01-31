import { async, ComponentFixture, TestBed } from '@angular/core/testing';

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
import { CookieService } from 'angular2-cookie';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        FormsModule,
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
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);

    component = fixture.componentInstance;
    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain heading', () => {
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('POST IT');
  });

  it('form should be invalid on initial load', () => {
    expect(component.form.valid).toBeFalsy();
  });

  it('email form validity', () => {
    const email = component.form.controls['email'];
    expect(email.errors['required']).toBeTruthy();
    expect(email.errors['minLength']).toBeFalsy();
    expect(email.valid).toBeFalsy();

    email.setValue('u');
    expect(email.errors['required']).toBeFalsy();
    expect(email.errors['minlength']).toBeTruthy();
    expect(email.valid).toBeFalsy();

    email.setValue('us');
    expect(email.errors).toBeNull();
    expect(email.valid).toBeTruthy();
  });

  it('password form validity on component load', () => {
    const password = component.form.controls['password'];
    console.log('password.errors:', password.errors);
    expect(password.errors['required']).toBeTruthy();
    expect(password.errors['minLength']).toBeFalsy();
    expect(password.valid).toBeFalsy();
  });

  it('password form validity', () => {
    const password = component.form.controls['password'];

    password.setValue('p');
    expect(password.errors['required']).toBeFalsy();
    expect(password.errors['minlength']).toBeTruthy();
    expect(password.valid).toBeFalsy();

    password.setValue('pa');
    expect(password.errors).toBeNull();
    expect(password.valid).toBeTruthy();
  });

  it('there should be user feedback for invalid form submittions', () => {
    const email = component.form.controls['email'];
    const password = component.form.controls['password'];

    component.handleLogin();
    expect(component.errorMessage).toBe('email is a required field. password is a required field. ');

    email.setValue('a');
    component.handleLogin();
    expect(component.errorMessage).toBe('email is too short. password is a required field. ');

    email.setValue('admin');
    component.handleLogin();
    expect(component.errorMessage).toBe('password is a required field. ');

    password.setValue('p');
    component.handleLogin();
    expect(component.errorMessage).toBe('password is too short. ');

    password.setValue('password');
    component.handleLogin();
    expect(component.errorMessage).toBe('');
  });
});
