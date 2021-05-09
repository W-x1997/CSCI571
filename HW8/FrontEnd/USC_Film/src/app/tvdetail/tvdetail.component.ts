import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppServiceService } from '../app-service.service';
import { filter } from 'rxjs/operators';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';



@Component({
  selector: 'ngbd-modal-content2',
  template: `
    <div class="modal-header">
      <h4 class="modal-title">{{cast_name}}</h4>
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>

    <div class="modal-body">
      <div class="row">
        <div id="modal_img_div" class="col-lg-3" >
           <img id="modal_img" src={{photo_path}}>
        </div>

        <div id="modal_detail" class="col-lg-8">
            <p *ngIf="cast_birthday != null" class="modal_p" >Birth: {{cast_birthday}}</p>
            <p *ngIf="place_of_birth!= null" class="modal_p" >Birth Place: {{place_of_birth}}</p>
            <p *ngIf="cast_gender != null" class="modal_p" >Gender: {{cast_gender}}</p>
            <p *ngIf="known_for_department != null" class="modal_p" >Known for: {{known_for_department}}</p>
            <p *ngIf="also_known_as != null && also_known_as.length > 0" class="modal_p" >Also Known as: {{also_known_as}}</p>
            
            <div id="icons"> 
              <a *ngIf="modal_imdb_link != null" id="modal_imdb" class="icon" target="_blank" href={{modal_imdb_link}} > <i class="fa fa-imdb fa-2x"> </i></a>
              <a *ngIf="modal_instagram_link != null" id="modal_instagram" class="icon"  target="_blank" href={{modal_instagram_link}} > <i class="fa fa-instagram fa-2x" > </i></a>
              <a *ngIf="modal_facebook_link != null" id="modal_fb" class="icon"  target="_blank" href={{modal_facebook_link}} > <i class="fa fa-facebook-official fa-2x" > </i></a>
              <a *ngIf="modal_twitter_link != null" id="modal_twitter"  class="icon"   target="_blank" href={{modal_twitter_link}}> <i class="fa fa-twitter fa-2x"> </i></a>
            
            </div>
            
        </div>

      </div>
      
      <div id="biography_div">
        <h3>Biography</h3>
        <p class="modal_p" *ngIf="biography">{{biography}}</p>
      </div>
      


    </div>
  
  `
})
export class NgbdModalContent2 {
  @Input() cast_id: any;
  @Input() cast_name: any;
  @Input() cast_birthday: any;
  @Input() cast_gender: any;
  @Input() place_of_birth: any;
  @Input() known_for_department: any;
  @Input() also_known_as: any;
  @Input() biography: any;
  @Input() photo_path: any;
  @Input() modal_imdb_link: any;
  @Input() modal_facebook_link: any;
  @Input() modal_instagram_link: any;
  @Input() modal_twitter_link: any;

  constructor(public activeModal: NgbActiveModal) {}
}


@Component({
  selector: 'app-tvdetail',
  templateUrl: './tvdetail.component.html',
  styleUrls: ['./tvdetail.component.css']
})
export class TvdetailComponent implements OnInit {
  id:any;
  title:any;
  tagline:any;
  year:any;
  avg_votes:any;
  duration:any;
  genres:any;
  language:any;
  overview:any;
  reviews:any;
  len_of_reviews:any;
  casts:any;

  twitter_link:any;
  fb_link:any;
  
  video_key:any;
  video_link:any;

  recommend_carsouel_title:string = "Recommended TV Shows";
  similar_carsouel_title:string = "Similar TV Shows";

  recommend_carsouel_data : any;
  similar_carsouel_data : any; 

  addbtn_clicked: boolean;

  poster_path: any;


  constructor( private service: AppServiceService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal) { 
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.addbtn_clicked = false;
    }

  ngOnInit() {

    //init
    this.id = this.route.snapshot.params.tv_id;
    // first set to false then check if the localStorage contains cur_item
    this.addbtn_clicked = false;
    $("#btn_add_list").text("Add to Watchlist");
    let mylist = JSON.parse(localStorage.getItem('mylist') || '[]');
    mylist.forEach((item: any) =>{
    if(item.type == 'tv' && item.id == this.id){
        this.addbtn_clicked = true;
        $("#btn_add_list").text("Remove from Watchlist");
    }});



    this.service.getTVVideo(this.id).subscribe((response: any) => {
      //console.log(response);
      if ('key' in  response[0] && response[0].key != null) {
        this.video_key = response[0].key;
        this.video_link = response[0].link;
      } else {
        this.video_key = 'tzkWB85ULJY';
        this.video_link = "https://www.youtube.com/watch?v=" + this.video_key;
      }
  });

  this.service.getTVDetails(this.id).subscribe((response: any) => {
    //console.log(response);
    this.title = response.title;
    this.poster_path = response.poster_path;

    if (response.tagline != null)
      this.tagline = response.tagline;
      
    this.overview = response.overview;
    this.avg_votes = response.vote_average;
    
    let date_str  = new String(response.first_air_date);	
    this.year = date_str.substring(0, 4) ;
   
    // let minutes = response.episode_run_time;
    // let h = Math.round(minutes / 60);
    // let m = minutes % 60;
    this.duration = response.episode_run_time[0] + "mins";
    
    let genre_str = new String();
    for (let genre of response.genres) {
      genre_str = genre_str + genre.name + ",  "
    }
    genre_str = genre_str.substring(0, genre_str.length - 3);  
    this.genres = genre_str;

    let language_str = new String();
    for (let s of response.spoken_languages) {
      language_str = language_str + s.name + ","
    }
    language_str = language_str.substring(0, language_str.length - 1);
    this.language = language_str;
   

    this.twitter_link = "https://twitter.com/intent/tweet?text=Watch%20" + this.title + "%20" + this.video_link + "      &hashtags=USC&hashtags=CSCI571&hashtags=FightOn" ;
    // this.fb_link = "https://www.facebook.com/sharer/sharer.php?u=" + this.video_key;
    this.fb_link = "https://www.facebook.com/sharer/sharer.php?u=https://www.youtube.com/watch?v=" + this.video_key;
  
    
  // add to the local
  //store it into localStorage
  if (localStorage.getItem('history') == null) {
    let newrecord = {"type" : "tv", "id" : this.id, "poster_path" : this.poster_path, "title": this.title};
    let history_list = [newrecord];
    localStorage.setItem('history', JSON.stringify(history_list));  
  } else {
    let history_list = JSON.parse(localStorage.getItem('history')!);
    // first check if exists 
    
    // let found:boolean = false;
    // history_list.forEach((item: any) =>{
    //   if(item.type == 'tv' && item.id == this.id){
    //       found = true;
    // }});

    for (let i = 0; i < history_list.length; i++) {
      if (history_list[i].type == 'tv' && history_list[i].id == this.id) {
        history_list.splice(i,1);
        break;
      }
    }

  // if (found) {
  //   history_list.delete({"type" : "tv", "id" : this.id, "poster_path" : this.poster_path, "title": this.title});
  // }  
  
    history_list.push({"type" : "tv", "id" : this.id, "poster_path" : this.poster_path, "title": this.title});
  
   
    localStorage.setItem('history', JSON.stringify(history_list));
  }
  
  });

  


  this.service.getTVCast(this.id).subscribe((response => {
    //console.log(response);
    this.casts = response;
    
  }));

  this.service.getTVReviews(this.id).subscribe((response => {
    this.reviews = response;
    this.len_of_reviews = this.reviews.length;
  }));

  this.service.getRecommendTV(this.id).subscribe((response => {
    this.recommend_carsouel_data = response;
    console.log(this.recommend_carsouel_data);
  }));

  this.service.getSimilarTV(this.id).subscribe((response => {
    //console.log(response);
    this.similar_carsouel_data = response;
  }));



  }


  getDay(day: string) {
    if (day.substring(0, 1) == "0")
      return day.substring(1);
    else 
      return day;  
  }

  getMonth(month : string) {
      let map = ["0", "January", "February", "March", "April", "May", "June", "July","August", "September", "October", "November", "December"];
      let n = Number(month);
      return map[n];
  }

  getTime(time : string)  {
    let str = new String();
    let hour =  Number(time.substring(0, 2));
    if (hour >= 12) {
      let tmp = hour - 12;
      str = tmp + time.substring(2) + " PM";
    } else {
      str = time + "AM";
    }
    return str;
  }


  openModal(cast_id : string) {
    //console.log(cast_id);
    const modalRef = this.modalService.open(NgbdModalContent2, { size: 'lg' });

    this.service.getCastExternal(cast_id).subscribe((response: any) => {
      //console.log(response);
      if (response.imdb_id != null)
        modalRef.componentInstance.modal_imdb_link = "https://www.imdb.com/name/" + response.imdb_id;
      if (response.instagram_id != null)  
        modalRef.componentInstance.modal_instagram_link = "https://www.instagram.com/" + response.instagram_id;
      if (response.facebook_id != null)
        modalRef.componentInstance.modal_facebook_link = "https://www.facebook.com" + response.facebook_id;
      if (response.twitter_id != null)
        modalRef.componentInstance.modal_twitter_link = "https://www.twitter.com/" + response.twitter_id;
    });

    this.service.getCastDetails(cast_id).subscribe((response: any) => {
      //console.log(response);
      modalRef.componentInstance.cast_id = cast_id;
      modalRef.componentInstance.cast_name = response.name;
      modalRef.componentInstance.cast_birthday = response.birthday;
      if (response.gender == "1")
        modalRef.componentInstance.cast_gender = "Female";
      if (response.gender == "2")
        modalRef.componentInstance.cast_gender = "Male";  
      modalRef.componentInstance.place_of_birth = response.place_of_birth;
      modalRef.componentInstance.known_for_department = response.known_for_department;
      modalRef.componentInstance.also_known_as = response.also_known_as;
      modalRef.componentInstance.biography = response.biography;
      modalRef.componentInstance.photo_path = response.photo;
      // console.log(modalRef.componentInstance.modal_imdb_id);
      // console.log(modalRef.componentInstance.modal_instagram_id);
      // console.log(modalRef.componentInstance.modal_facebook_id );
      // console.log(modalRef.componentInstance.modal_twitter_id);
    });

    

  }

  add_to_list() {
    if(!this.addbtn_clicked) {
      //console.log("added!")
      this.addbtn_clicked = true;
      $('#remove_alert').hide(); // hide the other one first
      $("#btn_add_list").text("Remove from Watchlist");
     
      $('#add_alert').show().delay(5000).fadeOut('slow');
      
      //add our obj to our localstorage
      if (localStorage.getItem('mylist') == null) {
        let cur_item = {"type" : "tv", "id" : this.id, "poster_path" : this.poster_path, "title": this.title};
        let newmylist = [cur_item];
        localStorage.setItem('mylist', JSON.stringify(newmylist));
      } else {
        let mylist = JSON.parse(localStorage.getItem('mylist')!);
        mylist.push({"type" : "tv", "id" : this.id, "poster_path" : this.poster_path, "title": this.title});
        localStorage.setItem('mylist', JSON.stringify(mylist));
      }
     // console.log(JSON.stringify(JSON.parse(localStorage.getItem('mylist') || '[]')));

    } else {
      //console.log("removed!")
      this.addbtn_clicked = false;
      $('#add_alert').hide(); // hide the other one first
      $("#btn_add_list").text("Add to Watchlist");
      
      $('#remove_alert').show().delay(5000).fadeOut('slow');
      //delete our obj to our localstorage
       
      let mylist = JSON.parse(localStorage.getItem('mylist') || '[]');
      mylist = mylist.filter((item: any)=>(item["type"] == "movie" || (item["type"] == "tv" && item["id"] != this.id)));
      localStorage.setItem('mylist', JSON.stringify(mylist));

      //console.log(JSON.stringify(JSON.parse(localStorage.getItem('mylist') || '[]')));

    }
  }

  
  
}
