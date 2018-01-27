import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BeerceptionComponent } from './beerception.component';

describe('BeerceptionComponent', () => {
  let component: BeerceptionComponent;
  let fixture: ComponentFixture<BeerceptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BeerceptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BeerceptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
