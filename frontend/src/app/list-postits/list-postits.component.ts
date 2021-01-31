import { PostItService } from '../service/data/postit.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostIt } from '../model/postit.model';
import { BasicAuthenticationService } from '../service/basic-authentication.service';

@Component({
  selector: 'app-list-postits',
  templateUrl: './list-postits.component.html',
  styleUrls: ['./list-postits.component.css']
})
export class ListPostitsComponent implements OnInit {

  postits: PostIt[];

  message: string;

  constructor(
    private postitService: PostItService,
    private router: Router,
    private basicAuthenticationService: BasicAuthenticationService
  ) { }

  ngOnInit() {
    this.refreshPostits();
    this.postitService.postitsChanged.subscribe(postits => {
      this.postits = postits;
    });
  }

  refreshPostits() {
    this.postitService.retrieveAllPostits(this.basicAuthenticationService.getAuthenticatedUser()).subscribe(
      response => {
        console.log(response);
        this.postits = response;
      }
    );
  }
}
