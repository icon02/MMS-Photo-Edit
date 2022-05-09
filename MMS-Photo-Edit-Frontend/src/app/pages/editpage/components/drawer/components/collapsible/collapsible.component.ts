import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { v4 as uuid } from 'uuid';

@Component({
  selector: 'app-collapsible',
  templateUrl: './collapsible.component.html',
  styleUrls: ['./collapsible.component.scss'],
  encapsulation: ViewEncapsulation.Emulated,
})
export class CollapsibleComponent implements OnInit {
  @Input('label')
  title: string = 'Test Title';
  @Input('initialExpanded')
  initialExpanded: boolean = false;

  expanded: boolean = this.initialExpanded;

  @Input('id')
  id: string = uuid();

  constructor() {}

  ngOnInit(): void {}

  toggle(): void {
    this.expanded = !this.expanded;
  }
}
