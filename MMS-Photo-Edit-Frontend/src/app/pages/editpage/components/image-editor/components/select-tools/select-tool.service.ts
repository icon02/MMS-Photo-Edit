import { Injectable } from '@angular/core';
import { DistinctBehaviorSubject } from 'src/app/utils/DistinctBehaviorSubject';
import { SelectTool } from './select-tool.type';

@Injectable({
  providedIn: 'root',
})
export class SelectToolService {
  selectedToolBSubject: DistinctBehaviorSubject<SelectTool | null> =
    new DistinctBehaviorSubject<SelectTool | null>(null);

  constructor() {}

  get selectTool(): SelectTool | null {
    return this.selectedToolBSubject.getValue();
  }
}
