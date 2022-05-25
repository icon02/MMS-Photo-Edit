import { Injectable } from '@angular/core';
import { DistinctBehaviorSubject } from 'src/app/utils/DistinctBehaviorSubject';
import { Shape } from '../../shapes.model';
import { SelectTool } from './select-tool.type';

@Injectable({
  providedIn: 'root',
})
export class SelectToolService {
  selectedToolBSubject: DistinctBehaviorSubject<SelectTool | null> =
    new DistinctBehaviorSubject<SelectTool | null>(null);

  curSelectionBSubject: DistinctBehaviorSubject<Shape | null> =
    new DistinctBehaviorSubject<Shape | null>(null);

  canvasWidth: number = 0;
  canvasHeight: number = 0;

  constructor() {}

  get selectTool(): SelectTool | null {
    return this.selectedToolBSubject.getValue();
  }

  get curSelection(): Shape | null {
    return this.curSelectionBSubject.value;
  }

  resetSelection(): void {
    this.curSelectionBSubject.next(null);
  }
}
