import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudViewerRowHeadComponent } from './crud-viewer-row-head.component';

describe('CrudViewerRowHeadComponent', () => {
  let component: CrudViewerRowHeadComponent;
  let fixture: ComponentFixture<CrudViewerRowHeadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrudViewerRowHeadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrudViewerRowHeadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
