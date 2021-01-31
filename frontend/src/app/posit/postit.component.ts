import { ActivatedRoute, Router } from '@angular/router';
import { PostItService } from '../service/data/postit.service';
import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subject } from 'rxjs';
import { PostIt } from '../model/postit.model';
import { BasicAuthenticationService } from '../service/basic-authentication.service';
import { POST_IT_NOTE_REGEX } from '../app.constants';
import { CreatePostItComponent } from '../create-postit/create-postit.component';
import { AutoFocusEventService } from '../service/autofocus-event.service';

@Component({
  selector: 'app-postit',
  templateUrl: './postit.component.html',
  styleUrls: ['./postit.component.css']
})
export class PostItComponent implements OnInit, OnDestroy {

  @ViewChild('editForm') editForm: NgForm;
  @ViewChild('deleteForm') deleteForm: NgForm;
  startEditting = new Subject<PostIt>();
  isEditting = false;
  isDeleting = false;
  id: number;
  @Input() postit: PostIt;

  constructor(
    private postitService: PostItService,
    private route: ActivatedRoute,
    private router: Router,
    private basicAuthenticationService: BasicAuthenticationService,
    private autoFocusEventService: AutoFocusEventService
  ) { }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
  }

  ngOnDestroy(): void {
    this.startEditting.unsubscribe();
  }

  onEdit() {
    const regex = POST_IT_NOTE_REGEX;
    this.startEditting.subscribe(n => {
      this.editForm.setValue({
        note: n.text
      });
    });
    this.startEditting.next(this.postit);
    this.isEditting = true;
  }

  onCloseModal() {
    this.isEditting = false;
    this.isDeleting = false;

  }

  onUpdate(form: NgForm) {
    this.postit.text = form.value.note;
    const username = this.basicAuthenticationService.getAuthenticatedUser();

    this.postitService.update(username, this.postit.id, this.postit);
    this.onCloseModal();
  }

  onDelete(event) {
    event.stopPropagation();

    this.isDeleting = true;
    this.deleteForm.setValue({
      note: this.postit.text
    });
  }

  onConfirmDelete(form: NgForm) {
    const username = this.basicAuthenticationService.getAuthenticatedUser();

    this.postitService.delete(username, this.postit.id);
    this.onCloseModal();
  }
}
