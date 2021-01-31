import { ActivatedRoute, Router } from '@angular/router';
import { PostItService } from '../service/data/postit.service';
import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { BasicAuthenticationService } from '../service/basic-authentication.service';
import { PostIt } from '../model/postit.model';
import { POST_IT_NOTE_REGEX } from '../app.constants';
import { ElementRef } from '@angular/core';
import { AfterContentChecked } from '@angular/core';
import { AutoFocusEventService } from '../service/autofocus-event.service';

@Component({
  selector: 'app-create-postit',
  templateUrl: './create-postit.component.html',
  styleUrls: ['./create-postit.component.css']
})
export class CreatePostItComponent implements OnInit, OnDestroy {

  isUserLoggedIn = false;
  form: FormGroup;
  disableBtn = true;

  constructor(
    private basicAuthenticationService: BasicAuthenticationService,
    private postitService: PostItService,
    private formBuilder: FormBuilder,
    private autoFocusEventService: AutoFocusEventService
  ) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      text: ['', [
        Validators.required,
        Validators.pattern(POST_IT_NOTE_REGEX)]],
    });
    this.form.valueChanges
      .subscribe((changedObj: any) => {
        this.disableBtn = !this.form.valid;
      });

    this.isUserLoggedIn = this.basicAuthenticationService.isUserLoggedIn();
    this.basicAuthenticationService.userActivityEvent.subscribe(username => {
      this.isUserLoggedIn = username ? true : false;
    });
    this.autoFocusEventService.getEvents().subscribe(event => {
      
    });
  }

  ngOnDestroy(): void { }

  onCreate() {
    if (!this.form.valid) {
      return;
    }
    this.disableBtn = true;

    const username = this.basicAuthenticationService.getAuthenticatedUser();
    const postit = new PostIt(null, this.form.value.text, username);
    this.postitService.create(username, postit);
    this.form.reset();
  }
}
