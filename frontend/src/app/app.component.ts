import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { BasicAuthenticationService } from './service/basic-authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  isUserLoggedIn = false;

  constructor(private basicAuthenticationService: BasicAuthenticationService) { }

  ngOnInit(): void {
    this.isUserLoggedIn = this.basicAuthenticationService.isUserLoggedIn();
    this.basicAuthenticationService.userActivityEvent.subscribe(username => {
      this.isUserLoggedIn = this.basicAuthenticationService.isUserLoggedIn();
    });
  }
}
