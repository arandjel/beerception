import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBeerceptionComponent } from './create-beerception.component';

describe('CreateBeerceptionComponent', () => {
  let component: CreateBeerceptionComponent;
  let fixture: ComponentFixture<CreateBeerceptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateBeerceptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateBeerceptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
