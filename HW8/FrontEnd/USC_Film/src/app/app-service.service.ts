import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';




const options = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AppServiceService {
 
// private hostname = "http://localhost:3000";
private hostname = "https://wxdemogcp.wl.r.appspot.com/";

constructor(private http: HttpClient) {

}

getCurrentPlayingMovies() {
  return this.http.get(this.hostname + '/currentplayingmovies', options);
}


getPopularMovies() {
  return this.http.get(this.hostname + '/popularmovies', options);
}

getTopRatedMovies()  {
  return this.http.get(this.hostname + '/topratedMovies', options);
}

getTrendingMovies() {
  return this.http.get(this.hostname + '/trendingmovies', options);
}

getPopularTV() {
  return this.http.get(this.hostname + '/populartv', options);
}


getTopRatedTV() {
  return this.http.get(this.hostname + '/topratedtv', options);
}

getTrendingTV()  {
  return this.http.get(this.hostname + '/trendingtv', options);
}

getMovieVideo(id: string) {
  return this.http.get(this.hostname + '/movievideo?movie_id=' + id, options);
}

getMovieDetails(id: string) {
  return this.http.get(this.hostname + '/moviedetails?movie_id=' + id, options);
}

getMovieCast(id: string) {
  return this.http.get(this.hostname + '/moviecast?movie_id=' + id, options);
}

getMovieReviews(id: string) {
  return this.http.get(this.hostname + '/moviereviews?movie_id=' + id, options);
}

getRecommendMovies(id: string) {
  return this.http.get(this.hostname + '/recommendmovies?movie_id=' + id, options);
}

getSimilarMovies(id: string) {
  return this.http.get(this.hostname + '/similarmovies?movie_id=' + id, options);
}

getCastDetails(id: string) {
  return this.http.get(this.hostname + '/castdetails?person_id=' + id, options);
}

getCastExternal(id: string) {
  return this.http.get(this.hostname + '/castexternal?person_id=' + id, options);
}


getTVVideo(id: string) {
  return this.http.get(this.hostname + '/tvvideo?tv_id=' + id, options);
}


getTVDetails(id: string) {
  return this.http.get(this.hostname + '/tvdetails?tv_id=' + id, options);
}


getTVCast(id: string) {
  return this.http.get(this.hostname + '/tvcast?tv_id=' + id, options);
}

getTVReviews(id: string) {
  return this.http.get(this.hostname + '/tvreviews?tv_id=' + id, options);
}

getRecommendTV(id: string) {
  return this.http.get(this.hostname + '/recommendtv?tv_id=' + id, options);
}

getSimilarTV(id: string) {
  return this.http.get(this.hostname + '/similartv?tv_id=' + id, options);
}

getSearch(query_input: string) {
  return this.http.get(this.hostname + '/search?query_input=' + query_input, options);
}

}


