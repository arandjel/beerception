import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  collapse: boolean = true;

  modalReference: any;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private modalService: NgbModal
  ) { }

  ngOnInit() {
  }

  logout() {
    this.authService.logout().subscribe(res => {
      this.userService.getMyInfo().subscribe();
      this.router.navigate(['/login']);
    });
  }

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    // return user.firstname + ' ' + user.lastname;
    return user.username;
  }



  open(content) {
    this.modalReference = this.modalService.open(content)
    this.modalReference.result.then((result) => {
      console.log(`Closed with: ${result}`);
    }, (reason) => {
      console.log(`Dismissed ${this.getDismissReason(reason)}`);
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }

}
