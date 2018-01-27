import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { GuestGuard } from './guard/guest.guard';
import { LoginGuard } from './guard/login.guard';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';

import { NotFoundComponent } from './not-found/not-found.component';
import { SignupComponent } from './signup/signup.component';
import { CreateBeerceptionComponent } from './create-beerception/create-beerception.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'signup',
    component: SignupComponent,
    canActivate: [GuestGuard],
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [GuestGuard]
  },
  {
    path: 'create-beerception',
    component: CreateBeerceptionComponent,
    canActivate: [LoginGuard]
  },
  /*
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuard]
  },
  */
  {
    path: '404',
    component: NotFoundComponent
  },
  /*
  {
    path: '403',
    component: ForbiddenComponent
  },
  */
  {
    path: '**',
    redirectTo: '/404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
