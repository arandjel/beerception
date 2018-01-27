import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { DisplayMessage } from '../shared/models/display-message';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/takeUntil';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit, OnDestroy {

  title = 'Sign up';
  form: FormGroup;

  /**
   * Boolean used in telling the UI
   * that the form has been submitted
   * and is awaiting a response
   */
  submitted = false;

  /**
   * Notification message from received
   * form request or router
   */
  notification: DisplayMessage;
  invalidData = false;
  formValid = true;

  returnUrl: string;
  private ngUnsubscribe: Subject<void> = new Subject<void>();

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {

  }

  ngOnInit() {
    this.route.params
    .takeUntil(this.ngUnsubscribe)
    .subscribe((params: DisplayMessage) => {
      this.notification = params;
    });
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.form = this.formBuilder.group({
      username: ['', Validators.compose([Validators.email, Validators.required])],
      password: ['', Validators.compose([Validators.required, Validators.minLength(5)])],
      firstname: ['', Validators.compose([Validators.required])],
      lastname: ['', Validators.compose([Validators.required])]
    });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  onSubmit() {
    this.invalidData = false;
    this.formValid = true;
    /**
     * Deny submit if validation errors exist
     */
    if (!this.validate()) {
      this.formValid = false;
      return;
    }

    /**
     * Innocent until proven guilty
     */
    this.notification = undefined;
    this.submitted = true;

    this.authService.signup(this.form.value)
    // show me the animation
    .delay(1000)
    .subscribe(data => {
      console.log(data);
      this.authService.login(this.form.value).subscribe(loginData => {
        this.userService.getMyInfo().subscribe();
      });
      this.router.navigate([this.returnUrl]);
    },
    error => {
      this.submitted = false;
      this.invalidData = true;
      this.notification = { msgType: 'error', msgBody: error['error'].errorMessage };
    });

  }

  validate(): boolean {
    if (this.form.get('username').invalid)
      return false;
    if (this.form.get('password').invalid)
      return false;
    if (this.form.get('firstname').invalid)
      return false;
    if (this.form.get('lastname').invalid)
      return false;

    return true;
  }

}
