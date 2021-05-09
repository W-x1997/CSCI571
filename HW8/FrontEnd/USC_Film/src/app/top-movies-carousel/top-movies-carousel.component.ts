import { Component, OnInit } from '@angular/core';
import { AppServiceService } from '../app-service.service';

@Component({
  selector: 'app-top-movies-carousel',
  templateUrl: './top-movies-carousel.component.html',
  styleUrls: ['./top-movies-carousel.component.css']
})
export class TopMoviesCarouselComponent implements OnInit {
  data:any;
  items=new Array;
  constructor(private service : AppServiceService) { }

  ngOnInit() {
    this.service.getTopRatedMovies().subscribe((response) => {
      //console.log(response);
      this.data = response;
      //console.log(this.data.length);
      //console.log(this.items[0]);

      let tmp = new Array();
      for (let i = 0; i < this.data.length; i++) {
        tmp.push(this.data[i]);
        if (tmp.length == 6 || i == this.data.length - 1) {
          this.items.push(tmp);
          tmp = new Array();
        }

      }
    });
  }

}
