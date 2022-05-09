import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RotateManipulationComponent } from './rotate-manipulation.component';

describe('RotateManipulationComponent', () => {
  let component: RotateManipulationComponent;
  let fixture: ComponentFixture<RotateManipulationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RotateManipulationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RotateManipulationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
