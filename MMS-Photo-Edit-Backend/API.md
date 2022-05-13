# API

This file contains api usage information

## Rest-Endpoints

| relative url                                                      | type   | input       | output¹ | session locked² | description                                                                                                   |
|-------------------------------------------------------------------|--------|-------------|---------|-----------------|---------------------------------------------------------------------------------------------------------------|
| **SessionController**                                             |        |             |         |                 |                                                                                                               |
| /sessions/create                                                  | GET    |             | Session | false           | creates the session used to call image manipulation endpoints                                                 |
| /sessions/update                                                  | PATCH  |             | Session | true            | extends the current session                                                                                   |
| /sessions/close                                                   | DELETE |             |         | true            | wipes the session with all its belongings from the repos                                                      |
| **ImageController**                                               |        |             |         |                 |                                                                                                               |
| <u>Base methods</u>                                               |        |             |         |                 |                                                                                                               |
| /image/use                                                        | POST   | Image       |         | true            | sets te base image for manipulation                                                                           |
| /image/current                                                    | GET    |             | Image   | true            | gets the current image                                                                                        |
| /image/undo                                                       | PATCH  |             |         | true            | undos the previous applied filter                                                                             |
| /image/redo                                                       | PATCH  |             |         | true            | redos previously undone applied filter                                                                        |
| <u>Manipulation</u>⁴                                              |        |             |         |                 |                                                                                                               |
| /image/mirror?dir={dir}                                           | PATCH  | Selection³  | Image   | true            | mirrors the image by a given direction ('vertical', 'horizontal')                                             |
| /image/rotate?rotation={val}                                      | PATCH  | Selection³  | Image   | true            | rotates the image by a given angle (=value)                                                                   |   
| /image/rgb?r={r}&g={g}&b={b}                                      | PATCH  | Selection³  | Image   | true            | adds given percentage (-100 <= r, g, b <= 100) to the rgb values                                              |
| /image/greyscale                                                  | PATCH  | Selection³  | Image   | true            | sets the pixel of the image to grey (image format does not change)                                            |
| /image/brightness?val={val}                                       | PATCH  | Selection³  | Image   | true            | changes the general brightness of the image by val (-100 <= val <= 100)                                       |
| /image/brightness/dark?val={val}                                  | PATCH  | Selection³  | Image   | true            | changes the brightness of dark areas by val (-100 <= val <= 100)                                              |
| /image/brightness/bright?val={val}                                | PATCH  | Selection³  | Image   | true            | changes the brightness of bright areas by val (-100 <= val <= 100)                                            |
| /image/blur?variance={variance}                                   | PATCH  | Selection³  | Image   | true            | blurs the image with the given variance (0 <= variance <= 10)                                                 |
| /image/color-invert                                               | PATCH  | Selection³  | Image   | true            | inverts all colors of the image                                                                               |
| /image/edge-colorize?threshold={th}&bg-color={bg}&edge-color={ec} | PATCH  | Selection³  | Image   | true            | creates an image that contains the background with 'bg' and edges with 'eg' (-100 <= th <= 100; bg, eg as hex |





¹ output objects are always wrapped within a ResponseEntity<> object.  
² if true, the header must contain the session-id (see more in "Sessions")  
³ if empty, the filter will be applied to the whole image, else only on the selection  
⁴ all manipulation methods allow the boolean parameter 'preview', which default is set to false.
If true it will apply the filter only to the thumbnail, and it won't change the state

## Sessions
TODO
