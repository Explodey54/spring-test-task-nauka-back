import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudHeaderComponent } from './crud-header.component';

describe('CrudHeaderComponent', () => {
  let component: CrudHeaderComponent;
  let fixture: ComponentFixture<CrudHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrudHeaderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrudHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
