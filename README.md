# MMS-Photo-Edit

The project exists of two parts (frontend and backend). The following two sections describe how to set the project up properly and how to get it running.

## MMS-Photo-Edit-Backend

Built with Java and Maven

### Project setup

1. Open the project as a Maven Project. Common IDEs will do the project setup for you. If not so, follow the "import maven project"-instructions according to your IDE.
2. Make sure <code>Port 8080</code> is free
3. Run the maven project

NOTE: The project is using Java 8.

## MMS-Photo-Edit-Frontend

Built with Typescript and Angular

### Requirements

- [Node](https://nodejs.org/en/) >= v16.13.2 \*
- [Node Package Manager](https://www.npmjs.com/) >= 8.1.2 \*
- [Angular cli](https://angular.io/cli) >= 13.1.4 \*\*

NOTE: make sure all of the tools above are accessible via the terminal

### Project setup

1. Navigate into the root folder of the frontend and open a Terminal
2. Run <code>npm install</code>
3. Run <code>ng serve</code>
4. Open a browser (preferably Google Chrome, because the application is 'tested' there) and go to [localhost:4200](http://localhost:4200/)

## Important Notes

- It is recommended to start the backend first and the open localhost:4200, because loading the webpage will request a token. Since we are still in Beta, no error messages are displayed in the frontend and you might end up wondering why uploading your image does not work. If still something does not work, refreshing the page usually helps.
- Due to a lack of time, the frontend was only written and visually tested on Google Chrome Version 102.0.5005.61 (Official Build) (x86_64) on a zoom level of 100%. Other zoom-levels might cause bugs on the canvas, on which the selection is drawn.

### Loss of 4th team member

Due to the loss of our 4th team member, the free-hand-selection is only implemented partly and image filters might cause unknown behavior when a free-hand-selection is active. Furthermore the preview-feature is not implemented at all.

---

\* [Installing Node and NPM](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)  
\*\* [Installing Angular CLI via NPM](https://angular.io/cli)
