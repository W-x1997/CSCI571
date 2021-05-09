import { Component, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppServiceService } from './app-service.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of, OperatorFunction} from 'rxjs';
import {catchError, debounceTime, distinctUntilChanged, map, tap, switchMap} from 'rxjs/operators';
import { Router } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  model: any;


  data: any;


  constructor(private service: AppServiceService, 
              private router: Router) {}

  search = (text$: Observable<string>) =>
  text$.pipe(
    debounceTime(200),
    map((term) => this.fetchSearchData(term))
  )

  formatter = (x: {name: string}) => "";
 

  fetchSearchData(query: string) {
    this.service.getSearch(query).subscribe((response => {
      this.data = response;
      return this.data;
    }));
    //console.log(this.data);
    return this.data;
  }

  

  handler_clickitem(type:string, id:string ) {
    //console.log(id + " " + type);
    // $("#search_input")[0].reset();
    //console.log( $("#search_input").val());
    
    let url = "watch/" + type + "/" + id;   
    
    let title = "";
    let poster_path = "";

    if (type == "movie") {
      this.service.getMovieDetails(id).subscribe(( (response : any) => {
        //console.log(response);
        title = response["title"];
        poster_path = response["poster_path"];
        //console.log(title + ", " + type + ", " + id + ", " + poster_path);


        //store it into localStorage
        if (localStorage.getItem('history') == null) {
          let newrecord = {"type" : "movie", "id" : id, "poster_path" : poster_path, "title": title};
          let history_list = [newrecord];
          localStorage.setItem('history', JSON.stringify(history_list));  
        } else {
          let history_list = JSON.parse(localStorage.getItem('history')!);
          // first check if exists 
          let found:boolean = false;
          history_list.forEach((item: any) =>{
            if(item.type == 'movie' && item.id == id){
                found = true;
          }});

        if (!found) {
          history_list.push({"type" : "movie", "id" : id, "poster_path" : poster_path, "title": title});
        } 
         
          localStorage.setItem('history', JSON.stringify(history_list));
        }
      }));
    }

    if (type == "tv") {
      this.service.getTVDetails(id).subscribe(( (response : any) => {
        title = response["title"];
        poster_path = response["poster_path"];

        //store it into localStorage
        if (localStorage.getItem('history') == null) {
          let newrecord = {"type" : "tv", "id" : id, "poster_path" : poster_path, "title": title};
          let history_list = [newrecord];
          localStorage.setItem('history', JSON.stringify(history_list));  
        } else {
          let history_list = JSON.parse(localStorage.getItem('history')!);
          // first check if exists 
          let found:boolean = false;
          history_list.forEach((item: any) =>{
            if(item.type == 'tv' && item.id == id){
                found = true;
          }});

        if (!found) {
          history_list.push({"type" : "tv", "id" : id, "poster_path" : poster_path, "title": title});
        } 
         localStorage.setItem('history', JSON.stringify(history_list));
        }


      }));

    }
    

    


    this.router.navigateByUrl(url);
    
  }


}
