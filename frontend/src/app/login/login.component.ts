import { BasicAuthenticationService } from './../service/basic-authentication.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, NgForm, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  username = '';
  password = '';
  errorMessage = 'Invalid Credentials';
  invalidLogin = false;

  constructor(private router: Router,
    private basicAuthenticationService: BasicAuthenticationService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit() {
    if (this.basicAuthenticationService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    }
    this.form = this.formBuilder.group({
      email: ['', [
        Validators.required,
        Validators.minLength(2)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(2)
      ]],
      rememberMe: ['', []]
    });
  }

  handleLogin() {
    if (!this.form.valid) {
      this.errorMessage = '';
      this.invalidLogin = true;
      for (const c in this.form.controls) {
        if (this.form.controls.hasOwnProperty(c)) {
          const fc = this.form.controls[c];
          if (fc.errors) {
            for (const e in fc.errors) {
              if ('required' === e) {
                this.errorMessage += c + ' is a required field. ';
              } else if ('minlength' === e) {
                this.errorMessage += c + ' is too short. ';
              }
            }
          }
        }
      }

      return;
    }
    this.errorMessage = '';
    this.invalidLogin = false;

    const email = this.form.value.email;
    const password = this.form.value.password;
    const rememberMe = this.form.value.rememberMe;
    this.basicAuthenticationService.executeAuthenticationService(email, password, rememberMe)
      .subscribe(
        data => {
          console.log(data);
          this.router.navigate(['']);
          this.invalidLogin = false;
        },
        error => {
          console.log(error);
          this.invalidLogin = true;
          this.errorMessage = error.statusText;
        }
      );
  }
}
