import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable()
export class ConfigService {

  //private _host_url = 'http://localhost:8080';

  private _host_url = 'https://beerception.herokuapp.com';

  private _api_url = this._host_url + '/api/v1';

  private _refresh_token_url = this._api_url + '/auth/refresh';

  private _login_url = this._api_url + '/auth/login';

  private _logout_url = this._api_url + '/auth/logout';

  private _signup_url = this._api_url + '/auth/signup';

  private _change_password_url = this._api_url + '/auth/changePassword';

  private _whoami_url = this._api_url + '/auth/whoami';

  private _user_url = this._api_url + '/auth/user';

  private _reset_credentials_url = this._user_url + '/reset-credentials';

  private _users_url = this._user_url + '/auth/all';

  private _beerception_url = this._api_url + '/beerception';

  private _beerception_info_url = this._beerception_url + '/info';

  get reset_credentials_url(): string {
      return this._reset_credentials_url;
  }

  get refresh_token_url(): string {
      return this._refresh_token_url;
  }

  get whoami_url(): string {
      return this._whoami_url;
  }

  get users_url(): string {
      return this._users_url;
  }

  get login_url(): string {
      return this._login_url;
  }

  get logout_url(): string {
      return this._logout_url;
  }

  get change_password_url(): string {
      return this._change_password_url;
  }

  get signup_url(): string {
      return this._signup_url;
  }

  get beerception_url(): string {
      return this._beerception_url;
  }

  get beerception_info_url(): string {
      return this._beerception_info_url;
  }

}
