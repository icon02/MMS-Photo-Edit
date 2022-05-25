export interface Rectangle {
  topLeft: Point;
  width: number;
  height: number;
}

export interface Circle {
  center: Point;
  radius: number;
}

export interface Point {
  x: number;
  y: number;
}

export interface FreeHandShape {
  points: Point[];
}

export type Shape = Rectangle | Circle | FreeHandShape;
