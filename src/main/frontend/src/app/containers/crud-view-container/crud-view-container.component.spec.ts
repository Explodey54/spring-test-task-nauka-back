import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudViewContainerComponent } from './crud-view-container.component';

describe('CrudViewContainerComponent', () => {
  let component: CrudViewContainerComponent<any>;
  let fixture: ComponentFixture<CrudViewContainerComponent<any>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrudViewContainerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrudViewContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
