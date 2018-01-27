import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpRequest, HttpEventType, HttpParams, HttpXsrfTokenExtractor } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/map';

import { serialize } from '../shared/utilities/serialize';

export enum RequestMethod {
  Get = 'GET',
  Head = 'HEAD',
  Post = 'POST',
  Put = 'PUT',
  Delete = 'DELETE',
  Options = 'OPTIONS',
  Patch = 'PATCH'
}

@Injectable()
export class ApiService {

  headers = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  });

  constructor(
    private http: HttpClient,
    private xsrf: HttpXsrfTokenExtractor
  ) {}

  get(path: string, args?: any): Observable<any> {
    const options = {
      headers: this.headers,
      withCredentials: true
    };

    if (args) {
      options['params'] = serialize(args);
    }

    return this.http.get(path, options)
      .catch(this.checkError.bind(this));
  }

  post(path: string, body: any, customHeaders?: HttpHeaders): Observable<any> {
    let _customHeaders: HttpHeaders;

    if (!customHeaders) {
      _customHeaders = this.headers.append('X-XSRF-TOKEN', this.xsrf.getToken());
    } else if (customHeaders && !customHeaders.get('X-XSRF-TOKEN')) {
      _customHeaders = customHeaders.append('X-XSRF-TOKEN', this.xsrf.getToken());
    }

    return this.request(path, body, RequestMethod.Post, _customHeaders);
  }

  put(path: string, body: any): Observable<any> {
    let _customHeaders: HttpHeaders;
    _customHeaders = this.headers.append('X-XSRF-TOKEN', this.xsrf.getToken());

    return this.request(path, body, RequestMethod.Put, _customHeaders);
  }

  delete(path: string, body?: any): Observable<any> {
    let _customHeaders: HttpHeaders;
    _customHeaders = this.headers.append('X-XSRF-TOKEN', this.xsrf.getToken());

    return this.request(path, body, RequestMethod.Delete, _customHeaders);
  }

  private request(path: string, body: any, method = RequestMethod.Post, customHeaders?: HttpHeaders): Observable<any> {
    const req = new HttpRequest(method, path, body, {
      headers: customHeaders || this.headers,
      withCredentials: true
    });

    return this.http.request(req)
      .filter(response => response instanceof HttpResponse)
      .map((response: HttpResponse<any>) => response.body)
      .catch(error => this.checkError(error));
  }

  // Display error if logged in, otherwise redirect to IDP
  private checkError(error: any): any {
    if (error && error.status === 401) {
      // this.redirectIfUnauth(error);
    } else {
      // this.displayError(error);
    }
    throw error;
  }

  private getXsrfToken() {
    // "XSRF-TOKEN"

    const token: string = this.xsrf.getToken();

    if (token)
      return this.xsrf.getToken();

    return null;
  }
}
