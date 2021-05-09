/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { CarsouelTemplateTvComponent } from './carsouel-template-tv.component';

describe('CarsouelTemplateTvComponent', () => {
  let component: CarsouelTemplateTvComponent;
  let fixture: ComponentFixture<CarsouelTemplateTvComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CarsouelTemplateTvComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarsouelTemplateTvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
