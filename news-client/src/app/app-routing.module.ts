import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LandingComponent} from "./components/landing/landing.component";
import {ShareComponent} from "./components/share/share.component";
import {NewsListComponent} from "./components/news-list/news-list.component";

const routes: Routes = [
  { path: "", component: LandingComponent },
  { path: "share", component: ShareComponent },
  { path: "news/:tag", component: NewsListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
