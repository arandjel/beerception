import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { NotFoundComponent } from './not-found/not-found.component';
import { ApiService } from './services/api.service';
import { ConfigService } from './services/config.service';
import { UserService } from './services/user.service';
import { HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { GuestGuard } from './guard/guest.guard';
import { LoginComponent } from './login/login.component';
import { AuthService } from './services/auth.service';
import { SignupComponent } from './signup/signup.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginGuard } from './guard/login.guard';
import { BeerceptionComponent } from './beerception/beerception.component';
import { CreateBeerceptionComponent } from './create-beerception/create-beerception.component';
import { UploadFileService } from './services/upload-file.service';
import { BeerceptionService } from './services/beerception.service';

export function initUserFactory(userService: UserService) {
  console.log("initUserFactory");
  return () => userService.initUser();
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NotFoundComponent,
    NavbarComponent,
    FooterComponent,
    LoginComponent,
    SignupComponent,
    BeerceptionComponent,
    CreateBeerceptionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule.forRoot()
  ],
  providers: [
    ApiService,
    UserService,
    ConfigService,
    AuthService,
    UploadFileService,
    BeerceptionService,
    GuestGuard,
    LoginGuard,
    {
      'provide': APP_INITIALIZER,
      'useFactory': initUserFactory,
      'deps': [UserService],
      'multi': true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
