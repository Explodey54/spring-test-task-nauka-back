import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudFilterComponent } from './crud-filter.component';

describe('CrudFilterComponent', () => {
  let component: CrudFilterComponent;
  let fixture: ComponentFixture<CrudFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrudFilterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrudFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
