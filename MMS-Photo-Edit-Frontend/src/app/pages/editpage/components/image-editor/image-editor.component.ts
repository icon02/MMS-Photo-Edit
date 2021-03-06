import {
  AfterViewInit,
  Component,
  ElementRef,
  HostListener,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { ImageService } from 'src/app/image.service';
import { SelectToolService } from './components/select-tools/select-tool.service';
import { SelectTool } from './components/select-tools/select-tool.type';
import { Circle, FreeHandShape, Point, Rectangle } from './shapes.model';

import { getCircle, getRect } from './shape.utils';

//
/* ==================== CANVAS EDITOR CONSTANTS ==================== */

const canvas_dim_multiplier = 3;
const canvas_zoom = 1 / canvas_dim_multiplier;

const stroke1_color: string = '#000000';
const stroke2_color: string = '#ffffff';
const line_width: number = 1.5 * canvas_dim_multiplier;
const line_dash: number[] = [
  3 * canvas_dim_multiplier,
  3 * canvas_dim_multiplier,
];

// used to identify if the click is supposed to close the free-hand area
const free_hand_path_close_threshold = 10 * canvas_dim_multiplier;

//
/* ==================== NG COMPONENT ==================== */

@Component({
  selector: 'app-image-editor',
  templateUrl: './image-editor.component.html',
  styleUrls: ['./image-editor.component.scss'],
})
export class ImageEditorComponent implements OnInit, AfterViewInit {
  //
  @Input('src')
  src: String | SafeResourceUrl | undefined | null = null;

  @ViewChild('canvas')
  canvas!: ElementRef<HTMLCanvasElement>;
  private context: CanvasRenderingContext2D | null = null;

  @ViewChild('img')
  private img!: ElementRef<HTMLImageElement>;

  /* ========== CANVAS VARS ========== */

  canvasWidth: number = 0;
  canvasHeight: number = 0;

  /* ========== SELECTION VARS ========== */

  private selectedTool: SelectTool | null = null;
  private curSelection: Rectangle | Circle | FreeHandShape | null = null;
  private mouseDownAt: Point | null = null;

  /*
  used to change the mouse-pointer if free-hand selection
  is selected and the mouse-pointer is near the very first point
  */
  isClosePath: boolean = false;

  private isPathClosed: boolean = false;

  /* ========== OTHER STATE VARS ========== */
  isLoading: boolean = false;

  constructor(
    private selectToolService: SelectToolService,
    private imageService: ImageService
  ) {}

  ngOnInit(): void {
    this.selectToolService.selectedToolBSubject.subscribe((val) => {
      this.selectedTool = val;
      this.resetSelection();
      this.clearCanvas();
    });

    this.selectToolService.curSelectionBSubject.subscribe((selection) => {
      if (
        selection &&
        (selection as any).topLeft &&
        (selection as any).width &&
        (selection as any).height
      ) {
        // Rectangle
        const rectSelection: Rectangle = selection as Rectangle;
        this.clearCanvas();
        this.drawRect(
          rectSelection.topLeft.x,
          rectSelection.topLeft.y,
          rectSelection.width,
          rectSelection.height
        );
      } else if (
        selection &&
        (selection as any).center &&
        (selection as any).radius
      ) {
        // Circle
        const circleSelection: Circle = selection as Circle;
        this.clearCanvas();
        this.drawCircle(
          circleSelection.center.x,
          circleSelection.center.y,
          circleSelection.radius
        );
      } else if (selection && Array.isArray((selection as any).points)) {
        // FreeHandShape
        const freeHandSelection: FreeHandShape = selection as FreeHandShape;
        const length = freeHandSelection.points.length;
        if (length >= 2) {
          this.drawLine(
            freeHandSelection.points[length - 2],
            freeHandSelection.points[length - 1]
          );
        }
      }
    });

    this.imageService.isLoadingSubject.subscribe(
      (val) => (this.isLoading = val)
    );
  }

  ngAfterViewInit(): void {
    this.context = this.canvas.nativeElement.getContext('2d');
  }

  @HostListener('window:resize', [])
  onWindowResize(): void {
    this.setCanvas();
  }

  @HostListener('mousemove', ['$event'])
  onMouseMove(event: PointerEvent): void {
    const point = this.getPointOnCanvas(event);
    if (this.mouseDownAt && this.selectedTool === 'rectangle') {
      const rect: Rectangle = getRect(this.mouseDownAt, point);
      if (rect) {
        this.clearCanvas();
        this.selectToolService.curSelectionBSubject.next(rect);
        // this.drawRect(rect.topLeft.x, rect.topLeft.y, rect.width, rect.height);
      }
    } else if (this.mouseDownAt && this.selectedTool === 'ellipse') {
      const circle = getCircle(this.mouseDownAt, point);
      if (circle) {
        this.clearCanvas();
        this.selectToolService.curSelectionBSubject.next(circle);
        // this.drawCircle(circle.center.x, circle.center.y, circle.radius);
      }
    } else if (this.selectedTool === 'free-hand') {
      if (this.selectToolService.curSelection && !this.isPathClosed) {
        const curPath = this.selectToolService.curSelection as FreeHandShape;
        if (curPath.points.length > 1) {
          const firstPoint = curPath.points[0];
          if (this.shouldPathClose(firstPoint, point)) this.isClosePath = true;
          else this.isClosePath = false;
        }
      }
    }
  }

  onImgLoad(): void {
    this.setCanvas();
    console.log('canvas:', this.canvas);
  }

  onCanvasClicked(e: MouseEvent): void {
    const eventPoint: Point = this.getPointOnCanvas(e);
    console.log('onCanvasClicked');
    switch (this.selectedTool) {
      case 'free-hand': {
        if (this.selectToolService.curSelection === null) {
          // first click
          // this.curSelection = { points: [eventPoint] };
          this.selectToolService.curSelectionBSubject.next({
            points: [eventPoint],
          });
        } else if ((this.selectToolService.curSelection as any).points) {
          if (this.isPathClosed) this.resetSelection();

          // any click after first click
          const curSelection = {
            points: (this.selectToolService.curSelection as FreeHandShape)
              .points,
          };
          if (this.isClosePath) {
            curSelection.points = [
              ...curSelection.points,
              curSelection.points[0],
            ];
            this.isClosePath = false;
            this.isPathClosed = true;
          } else if (this.isPathClosed) {
            this.resetSelection();
          } else {
            curSelection.points = [...curSelection.points, eventPoint];
          }

          this.selectToolService.curSelectionBSubject.next(curSelection);
          /*
          this.curSelection = curSelection;

          this.drawLine(
            curSelection.points[curSelection.points.length - 2],
            curSelection.points[curSelection.points.length - 1]
          );
          */
        } else {
          this.resetSelection();
          this.selectToolService.selectedToolBSubject.next('free-hand');
        }
        break;
      }
      case 'ellipse':
      case 'rectangle':
      default:
        if (this.mouseDownAt) this.resetSelection();
    }
  }

  onCanvasMouseDown(e: MouseEvent): void {
    if (this.selectedTool === 'rectangle' || this.selectedTool === 'ellipse') {
      //
      e.preventDefault();
      this.mouseDownAt = this.getPointOnCanvas(e);
      //
    }
  }

  onCanvasMouseUp(e: MouseEvent): void {
    const point = this.getPointOnCanvas(e);
    if (this.mouseDownAt && this.selectedTool == 'rectangle') {
      const rect = getRect(this.mouseDownAt, point);
      if (rect) {
        this.clearCanvas();
        this.drawRect(rect.topLeft.x, rect.topLeft.y, rect.width, rect.height);
        this.mouseDownAt = null;
      }
    } else if (this.mouseDownAt && this.selectedTool == 'ellipse') {
      const circle = getCircle(this.mouseDownAt, point);
      if (circle) {
        this.clearCanvas();
        this.drawCircle(circle.center.x, circle.center.y, circle.radius);
        this.mouseDownAt = null;
      }
    }
  }

  /* ==================== PRIVATE DRAWING METHODS ==================== */

  private drawRect(x: number, y: number, width: number, height: number): void {
    if (this.context) {
      this.context.beginPath();
      this.setStrokeStyle1();
      this.context.rect(x, y, width, height);
      this.context.stroke();

      this.context.beginPath();
      this.setStrokeStyle2();
      this.context.rect(x, y, width, height);
      this.context.stroke();
    }
  }

  private drawCircle(x: number, y: number, radius: number): void {
    if (this.context) {
      this.context.beginPath();
      this.setStrokeStyle1();
      this.context.arc(x, y, radius, 0, 360);
      this.context.stroke();

      this.context.beginPath();
      this.setStrokeStyle2();
      this.context.arc(x, y, radius, 0, 360);
      this.context.stroke();
    }
  }

  private drawLine(from: Point, to: Point): void {
    if (this.context) {
      this.context.beginPath();
      this.setStrokeStyle1();
      this.context.moveTo(from.x, from.y);
      this.context.lineTo(to.x, to.y);
      this.context.stroke();
      this.context.closePath();

      this.context.beginPath();
      this.setStrokeStyle2();
      this.context.moveTo(from.x, from.y);
      this.context.lineTo(to.x, to.y);
      this.context.stroke();
      this.context.closePath();
    }
  }

  private clearCanvas(): void {
    if (this.context) {
      // Store the current transformation matrix
      this.context.save();

      // Use the identity matrix while clearing the canvas
      this.context.setTransform(1, 0, 0, 1, 0, 0);
      this.context.clearRect(
        0,
        0,
        this.context.canvas.width,
        this.context.canvas.height
      );

      // Restore the transform
      this.context.restore();
    }
  }

  private setStrokeStyle1(): void {
    if (this.context) {
      this.context.setLineDash(line_dash);
      this.context.lineWidth = line_width;
      this.context.lineDashOffset = 0;
      this.context.strokeStyle = stroke1_color;
    }
  }

  private setStrokeStyle2(): void {
    if (this.context) {
      this.context.setLineDash(line_dash);
      this.context.lineWidth = line_width;
      this.context.lineDashOffset = line_dash[0];
      this.context.strokeStyle = stroke2_color;
    }
  }

  /* ==================== PRIVATE SELECTION METHODS ==================== */

  private resetSelection(): void {
    this.selectToolService.curSelectionBSubject.next(null);
    // this.curSelection = null;
    this.mouseDownAt = null;
    this.clearCanvas();
    this.isClosePath = false;
    this.isPathClosed = false;
  }

  /* ==================== PRIVATE METHODS ==================== */

  private setCanvas(): void {
    // compute image dimensions within image-Element
    // see https://stackoverflow.com/a/52187440
    const img = this.img.nativeElement;
    // console.log('img', img);
    if (!img) new Error('Could not get image from onload-Event');
    const ratio = img.naturalWidth / img.naturalHeight;
    let width = img.height * ratio;
    let height = img.height;
    if (width > img.width) {
      width = img.width;
      // +1 pixels, otherwise rounding difs will get cut
      height = width / ratio + 1;
    }

    this.canvas.nativeElement.width = width * canvas_dim_multiplier;
    this.canvas.nativeElement.height = height * canvas_dim_multiplier;
    this.canvas.nativeElement.style.width =
      width * canvas_dim_multiplier + 'px';
    this.canvas.nativeElement.style.height =
      height * canvas_dim_multiplier + 'px';

    // using the zoom property to not override existing transform
    (this.canvas.nativeElement.style as any).zoom = canvas_zoom;

    this.selectToolService.canvasWidth = width * canvas_dim_multiplier;
    this.selectToolService.canvasHeight = height * canvas_dim_multiplier;
  }

  private getPointOnCanvas(event: MouseEvent): Point {
    const canvasRect = this.canvas.nativeElement.getBoundingClientRect();
    const x = event.clientX - canvasRect.left / canvas_dim_multiplier;
    const y = event.clientY - canvasRect.top / canvas_dim_multiplier;

    return { x: x * canvas_dim_multiplier, y: y * canvas_dim_multiplier };
  }

  private shouldPathClose(a: Point, b: Point): boolean {
    const triA = Math.abs(a.x - b.x);
    const triB = Math.abs(a.y - b.y);

    const c = Math.sqrt(Math.pow(triA, 2) + Math.pow(triB, 2));

    return c < free_hand_path_close_threshold;
  }
}
