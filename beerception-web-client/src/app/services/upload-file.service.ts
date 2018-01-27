import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ConfigService } from './config.service';
import { HttpClient, HttpHeaders, HttpResponse, HttpRequest, HttpXsrfTokenExtractor } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/map';

@Injectable()
export class UploadFileService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService,
    private http: HttpClient,
    private xsrf: HttpXsrfTokenExtractor
  ) { }

  addBeerception(file: File) {
    let formdata: FormData = new FormData();

    formdata.append('beerFile', file);

    let headers = new HttpHeaders({
      'Accept': 'application/json',
      'X-XSRF-TOKEN': this.xsrf.getToken()
    });

    const req = new HttpRequest('POST', this.config.beerception_url, formdata, {
      headers: headers,
      withCredentials: true,
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req)
      .catch(error => {
        if (error && error.status === 401) {
          // this.redirectIfUnauth(error);
        } else {
          // this.displayError(error);
        }
        throw error;
      });
  }

  getBeerception() : Observable<any>{
    let headers = new HttpHeaders({
      'Accept': 'image/jpeg',
      'Content-Type': 'image/jpeg'
    });
    
    const req = new HttpRequest('GET', this.config.beerception_url, {}, {
      headers: headers,
      withCredentials: true,
      responseType: 'text'
    });

    return this.http.request(req)
      .filter(response => response instanceof HttpResponse)
      .map((response: HttpResponse<any>) => response.body)
      .catch(error => {
          if (error && error.status === 401) {
            // this.redirectIfUnauth(error);
          } else {
            // this.displayError(error);
          }
          throw error;
        });
  }
}
