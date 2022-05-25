import { Point, Circle, Rectangle } from './shapes.model';

export function getCircle(a: Point, b: Point): Circle {
  let width = Math.abs(b.x - a.x);
  let height = Math.abs(b.y - a.y);

  const radius = width > height ? height / 2 : width / 2;

  /*
  x = center

  0 -- x
  |
  y
              |
     top left | top right
              |
  ----------- x -----------
              |
  bottom left | bottom right
              |
  */
  if (b.x < a.x && b.y < a.y) {
    // top left
    return {
      center: {
        x: a.x - radius,
        y: a.y - radius,
      },
      radius: radius,
    };
  } else if (b.x > a.x && b.y < a.y) {
    // top right
    return {
      center: {
        x: a.x + radius,
        y: a.y - radius,
      },
      radius: radius,
    };
  } else if (b.x > a.x && b.y > a.y) {
    // bottom right
    return {
      center: {
        x: a.x + radius,
        y: a.y + radius,
      },
      radius: radius,
    };
  } else if (b.x < a.x && b.y > a.y) {
    // bottom left
    return {
      center: {
        x: a.x - radius,
        y: a.y + radius,
      },
      radius: radius,
    };
  } else {
    /* y or x coordinates are the same */
    // return smallest circle
    return {
      center: {
        x: a.x,
        y: a.y,
      },
      radius: 1,
    };
  }
}

export function getRect(startPoint: Point, endPoint: Point): Rectangle {
  const width = Math.abs(endPoint.x - startPoint.x);
  const height = Math.abs(endPoint.y - startPoint.y);

  /*
  x = center

  0 -- x
  |
  y
              |
     top left | top right
              |
  ----------- x -----------
              |
  bottom left | bottom right
              |
  */
  if (endPoint.x < startPoint.x && endPoint.y < startPoint.y) {
    // top left
    return {
      topLeft: { x: endPoint.x, y: endPoint.y },
      width: width,
      height: height,
    };
  } else if (endPoint.x > startPoint.x && endPoint.y < startPoint.y) {
    // top right
    return {
      topLeft: { x: startPoint.x, y: endPoint.y },
      width: width,
      height: height,
    };
  } else if (endPoint.x > startPoint.x && endPoint.y > startPoint.y) {
    // bottom right
    return {
      topLeft: { x: startPoint.x, y: startPoint.y },
      width: width,
      height: height,
    };
  } else if (endPoint.x < startPoint.x && endPoint.y > startPoint.y) {
    // bottom left
    return {
      topLeft: { x: endPoint.x, y: startPoint.y },
      width: width,
      height: height,
    };
  } else {
    // point a and b are the same
    // return smallest rectangle
    return {
      topLeft: { x: startPoint.x - 1, y: startPoint.y },
      width: 2,
      height: 2,
    };
  }
}
