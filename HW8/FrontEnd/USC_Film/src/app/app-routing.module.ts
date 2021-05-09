import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { MoviedetailComponent } from './moviedetail/moviedetail.component';
import { TvdetailComponent } from './tvdetail/tvdetail.component';
import { WatchlistComponent } from './watchlist/watchlist.component';

const routes: Routes = [
  {path: '', component: HomepageComponent},
  {path: 'watch/movie/:movie_id', component: MoviedetailComponent},
  {path: 'watch/tv/:tv_id', component: TvdetailComponent},
  {path: 'mylist', component: WatchlistComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
