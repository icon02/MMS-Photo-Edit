import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutComponent } from './layout/layout.component';
import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { DialogComponent } from './shared/components/dialog/dialog.component';
import { ClickedOutsideDirective } from './shared/directives/clicked-outside.directive';
import { UploadComponent } from './pages/homepage/components/upload/upload.component';
import { EditPageComponent } from './pages/editpage/editpage.component';
import { DrawerComponent } from './pages/editpage/components/drawer/drawer.component';
import { CollapsibleComponent } from './pages/editpage/components/drawer/components/collapsible/collapsible.component';
import { RgbManipulationComponent } from './pages/editpage/components/drawer/components/rgb-manipulation/rgb-manipulation.component';
import { DefaultFunctionsComponent } from './pages/editpage/components/drawer/components/default-functions/default-functions.component';
import { ImageEditorComponent } from './pages/editpage/components/image-editor/image-editor.component';
import { SelectToolsComponent } from './pages/editpage/components/image-editor/components/select-tools/select-tools.component';
import { PreviewApplyGroupComponent } from './pages/editpage/components/drawer/components/preview-apply-group/preview-apply-group.component';
import { BrightnessManipulationComponent } from './pages/editpage/components/drawer/components/brightness-manipulation/brightness-manipulation.component';
import { RangeGroupComponent } from './pages/editpage/components/drawer/components/range-group/range-group.component';
import { MirrorManipulationComponent } from './pages/editpage/components/drawer/components/mirror-manipulation/mirror-manipulation.component';
import { RotateManipulationComponent } from './pages/editpage/components/drawer/components/rotate-manipulation/rotate-manipulation.component';
import { BlurManipulationComponent } from './pages/editpage/components/drawer/components/blur-manipulation/blur-manipulation.component';
import { GreyscaleManipulationComponent } from './pages/editpage/components/drawer/components/greyscale-manipulation/greyscale-manipulation.component';
import { ColorInverterManipulationComponent } from './pages/editpage/components/drawer/components/color-inverter-manipulation/color-inverter-manipulation.component';
import { EdgeColorizationManipulationComponent } from './pages/editpage/components/drawer/components/edge-colorization-manipulation/edge-colorization-manipulation.component';

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    HeaderComponent,
    FooterComponent,
    HomepageComponent,
    DialogComponent,
    ClickedOutsideDirective,
    UploadComponent,
    EditPageComponent,
    DrawerComponent,
    CollapsibleComponent,
    RgbManipulationComponent,
    DefaultFunctionsComponent,
    ImageEditorComponent,
    SelectToolsComponent,
    PreviewApplyGroupComponent,
    BrightnessManipulationComponent,
    RangeGroupComponent,
    MirrorManipulationComponent,
    RotateManipulationComponent,
    BlurManipulationComponent,
    GreyscaleManipulationComponent,
    ColorInverterManipulationComponent,
    EdgeColorizationManipulationComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
