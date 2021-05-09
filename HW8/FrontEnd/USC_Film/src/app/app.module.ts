import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { YTPlayerModule } from 'angular-youtube-player';
import { YouTubePlayerModule } from '@angular/youtube-player';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HomepageComponent } from './homepage/homepage.component';
import { MoviedetailComponent } from './moviedetail/moviedetail.component';
import { NgbdModalContent2, TvdetailComponent } from './tvdetail/tvdetail.component';
import { WatchlistComponent } from './watchlist/watchlist.component';
import { CurrentMoviesCarouselComponent } from './current-movies-carousel/current-movies-carousel.component';
import { HttpClientModule } from '@angular/common/http';
import { PopularMovieCarouselComponent } from './popular-movie-carousel/popular-movie-carousel.component';
import { TopMoviesCarouselComponent } from './top-movies-carousel/top-movies-carousel.component';
import { TrendMoviesCarouselComponent } from './trend-movies-carousel/trend-movies-carousel.component';
import { PopularTvCarouselComponent } from './popular-tv-carousel/popular-tv-carousel.component';
import { TopTvCarouselComponent } from './top-tv-carousel/top-tv-carousel.component';
import { TrendTvCarouselComponent } from './trend-tv-carousel/trend-tv-carousel.component';
import { CarsouelTemplateComponent } from './carsouel-template/carsouel-template.component';
import {NgbdModalContent} from './moviedetail/moviedetail.component';
import { CarsouelTemplateTvComponent } from './carsouel-template-tv/carsouel-template-tv.component';
import { FormsModule } from '@angular/forms';
import { ContinueCarouselComponent } from './continue-carousel/continue-carousel.component';


@NgModule({
  declarations: [			
    AppComponent,
    HomepageComponent,
      MoviedetailComponent,
      TvdetailComponent,
      WatchlistComponent,
      CurrentMoviesCarouselComponent,
      PopularMovieCarouselComponent,
      TopMoviesCarouselComponent,
      TrendMoviesCarouselComponent,
      PopularTvCarouselComponent,
      TopTvCarouselComponent,
      TrendTvCarouselComponent,
      CarsouelTemplateComponent,
      NgbdModalContent,
      NgbdModalContent2,
      CarsouelTemplateTvComponent,
      ContinueCarouselComponent
   ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    YTPlayerModule,
    BrowserModule,
    YouTubePlayerModule,
    FormsModule,
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
