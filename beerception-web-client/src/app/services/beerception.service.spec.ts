import { TestBed, inject } from '@angular/core/testing';

import { BeerceptionService } from './beerception.service';

describe('BeerceptionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BeerceptionService]
    });
  });

  it('should be created', inject([BeerceptionService], (service: BeerceptionService) => {
    expect(service).toBeTruthy();
  }));
});
