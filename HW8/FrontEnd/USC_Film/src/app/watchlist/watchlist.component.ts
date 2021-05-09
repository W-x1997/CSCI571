import { Component, OnInit } from '@angular/core';
import { distinct } from 'rxjs/operators';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit {

  items_list:any;

  constructor() { }

  ngOnInit() {
    this.items_list = JSON.parse(localStorage.getItem('mylist') || '[]');
    this.items_list.reverse();
    if (this.items_list != null && this.items_list.length > 0) {
      $("#no_items").hide();
      
      //console.log(this.items_list);
     
    } else {
      $("#mylist_div").hide();

      
    }

    


  }

}
