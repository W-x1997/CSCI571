import { Component, Input, OnInit } from '@angular/core';
import { AppServiceService } from '../app-service.service';

@Component({
  selector: 'app-carsouel-template',
  templateUrl: './carsouel-template.component.html',
  styleUrls: ['./carsouel-template.component.css']
})
export class CarsouelTemplateComponent implements OnInit {
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
