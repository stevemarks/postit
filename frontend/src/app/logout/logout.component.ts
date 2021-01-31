import { Component, OnInit } from '@angular/core';
import { BasicAuthenticationService } from '../service/basic-authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(
    private basicAuthenticationService: BasicAuthenticationService,
    private router: Router
  ) { }

  ngOnInit() {
    this.basicAuthenticationService.logout();
    this.router.navigate(['login']);
  }

}
