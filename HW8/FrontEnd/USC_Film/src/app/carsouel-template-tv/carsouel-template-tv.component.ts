import { Component, Input, OnInit } from '@angular/core';
import { AppServiceService } from '../app-service.service';

@Component({
  selector: 'app-carsouel-template-tv',
  templateUrl: './carsouel-template-tv.component.html',
  styleUrls: ['./carsouel-template-tv.component.css']
})
export class CarsouelTemplateTvComponent implements OnInit {
  @Input() title : string = "";
  @Input() data: any;
  items = new Array();
  constructor(private service: AppServiceService) { }

  ngOnInit() {
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
