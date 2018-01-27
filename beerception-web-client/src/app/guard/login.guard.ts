import { Injectable } from '@angular/core';
import { Router, CanActivate, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { UserService } from '../services/user.service';


@Injectable()
export class LoginGuard implements CanActivate {

  private state: RouterStateSnapshot;

  constructor(private router: Router, private userService: UserService) {
    this.state = router.routerState.snapshot;
  }

  canActivate(): boolean {
    if (this.userService.currentUser) {
      return true;
    } 
    else {
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.state.url }});
      return false;
    }
  }
}
