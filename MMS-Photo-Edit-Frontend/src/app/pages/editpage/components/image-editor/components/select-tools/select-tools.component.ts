import { Component, OnInit } from '@angular/core';
import { SelectToolService } from './select-tool.service';
import { SelectTool } from './select-tool.type';

@Component({
  selector: 'app-select-tools',
  templateUrl: './select-tools.component.html',
  styleUrls: ['./select-tools.component.scss'],
})
export class SelectToolsComponent implements OnInit {
  selectedTool: SelectTool | null = null;

  constructor(private selectToolService: SelectToolService) {}

  ngOnInit(): void {}

  onRectClicked(): void {
    this.selectedTool = 'rectangle';
    this.selectToolService.selectedToolBSubject.next('rectangle');
  }

  onEllipseClicked(): void {
    this.selectedTool = 'ellipse';
    this.selectToolService.selectedToolBSubject.next('ellipse');
  }

  onFreeHandClicked(): void {
    this.selectedTool = 'free-hand';
    this.selectToolService.selectedToolBSubject.next('free-hand');
  }

  onClearClicked(): void {
    this.selectedTool = null;
    this.selectToolService.selectedToolBSubject.next(null);
  }
}
