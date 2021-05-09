import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-continue-carousel',
  templateUrl: './continue-carousel.component.html',
  styleUrls: ['./continue-carousel.component.css']
})
export class ContinueCarouselComponent implements OnInit {
  data: any;
  items = new Array();
  has_records:boolean = false;

  constructor() { }

  ngOnInit() {
    if (localStorage.getItem('history') == null) {
      this.has_records = false;  
    } else {
      this.has_records = true;  
      let history_list = JSON.parse(localStorage.getItem('history')!)
      if (history_list.length > 24) {
        history_list = history_list.slice(-24);
        localStorage.setItem('history', JSON.stringify(history_list));
        this.data = history_list.reverse();
      } else {
        this.data = history_list.reverse();
      }


      let tmp = new Array();
      for (let i = 0; i < this.data.length; i++) {
        tmp.push(this.data[i]);
        if (tmp.length == 6 || i == this.data.length - 1) {
          this.items.push(tmp);
          tmp = new Array();
        }

      }

    }


  }

}
