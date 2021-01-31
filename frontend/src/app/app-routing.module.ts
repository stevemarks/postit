import { RouteGuardService } from './service/route-guard.service';
import { LogoutComponent } from './logout/logout.component';
import { ListPostitsComponent } from './list-postits/list-postits.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ErrorComponent } from './error/error.component';

export const routes: Routes = [
  { path: '', component: ListPostitsComponent, canActivate: [RouteGuardService] },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [RouteGuardService] },

  { path: '**', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
