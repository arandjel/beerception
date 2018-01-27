import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ConfigService } from './config.service';

@Injectable()
export class BeerceptionService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) { }

  getBeerceptionInfo() {
    return this.apiService.get(this.config.beerception_info_url);
  }

  beerUp(body: any) {
    return this.apiService.post(this.config.beerception_url + '/beerup', {});
  }

  beerDown(body: any) {
    return this.apiService.post(this.config.beerception_url + '/beerdown', {});
  }
  
}
