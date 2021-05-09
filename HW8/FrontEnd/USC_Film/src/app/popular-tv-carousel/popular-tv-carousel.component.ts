import { Component, OnInit } from '@angular/core';
import { AppServiceService } from '../app-service.service';

@Component({
  selector: 'app-popular-tv-carousel',
  templateUrl: './popular-tv-carousel.component.html',
  styleUrls: ['./popular-tv-carousel.component.css']
})
export class PopularTvCarouselComponent implements OnInit {
  data:any;
  items=new Array;

  constructor(private service : AppServiceService) { }

  ngOnInit() {
    this.service.getPopularTV().subscribe((response) => {
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
