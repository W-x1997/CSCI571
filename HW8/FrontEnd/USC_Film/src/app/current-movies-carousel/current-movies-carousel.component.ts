import { Component, OnInit } from '@angular/core';
import { AppServiceService } from '../app-service.service';

@Component({
  selector: 'app-current-movies-carousel',
  templateUrl: './current-movies-carousel.component.html',
  styleUrls: ['./current-movies-carousel.component.css']
})
export class CurrentMoviesCarouselComponent implements OnInit {
  items:any;

  constructor(private service : AppServiceService) { }

  ngOnInit() {
    this.service.getCurrentPlayingMovies().subscribe((response) => {
      //console.log(response);
      this.items = response;
      //console.log(this.items[0]);
    });
    
  }

}
