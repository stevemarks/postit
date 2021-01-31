import { async, ComponentFixture, TestBed } from '@angular/core/testing';

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
import { PostIt } from '../model/postit.model';
import { CookieService } from 'angular2-cookie';

describe('PostItComponent', () => {
  let component: PostItComponent;
  let fixture: ComponentFixture<PostItComponent>;

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
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PostItComponent);
    component = fixture.componentInstance;

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display postit note text', () => {
    const text = 'test note';
    const postitId = 1;
    component.postit = new PostIt(postitId, text, 'admin');
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const element = compiled.querySelector('#note-content-text-' + postitId);
    expect(element.textContent).toBe(text);
  });

  it('should not display edit modal on load', () => {
    const text = 'test note';
    component.postit = new PostIt(null, text, 'admin');
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const element = compiled.querySelector('#edit-modal');

    expect(element.style.display).toBe('none');
  });

  it('should display edit modal when editting a postit', () => {
    const text = 'test note';
    component.postit = new PostIt(null, text, 'admin');
    component.isEditting = true;
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const element = compiled.querySelector('#edit-modal');

    expect(element.style.display).toBe('block');
  });

  it('should not display delete modal on load', () => {
    const text = 'test note';
    component.postit = new PostIt(null, text, 'admin');
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const element = compiled.querySelector('#delete-modal');

    expect(element.style.display).toBe('none');
  });

  it('should display delete modal when deleting a postit', () => {
    const text = 'test note';
    component.postit = new PostIt(null, text, 'admin');
    component.isDeleting = true;
    fixture.detectChanges();

    const compiled = fixture.debugElement.nativeElement;
    const element = compiled.querySelector('#delete-modal');

    expect(element.style.display).toBe('block');
  });
});
