from flask import Flask, render_template
from flask import request,jsonify
import json
import requests

from datetime import timedelta

app = Flask(__name__)



@app.route('/')
def app_init():
    return app.send_static_file("index.html")


@app.route('/getTrendingMovies')
def getTrendingMovies():
    data = request.args
    media_type = data.get("media_type")
    time_window = data.get("time_window")
    api_key = data.get("api_key")

    req = "https://api.themoviedb.org/3/trending/" + media_type + "/" + time_window + "?api_key=" + api_key
    response = requests.get(req)

    response_json = response.json()
    res = []
    for i in range(5):
        obj = {};
        obj["title"] = response_json["results"][i]["original_title"]
        backdrop_path = response_json["results"][i]["backdrop_path"]
        obj["pic_path"] = getIMGpath(backdrop_path, "w780");
        obj["release_data"] = response_json["results"][i]["release_date"]
        res.append(obj)

    return jsonify(res)


@app.route('/getTodayTV')
def getTodayTV():
    data = request.args;
    api_key = data.get("api_key")
    req = "https://api.themoviedb.org/3/tv/airing_today?api_key=" + api_key
    response = requests.get(req)

    response_json = response.json()

    res = []
    
    for i in range(5):
        obj = {};
        obj["name"] = response_json["results"][i]["name"]
        backdrop_path = response_json["results"][i]["backdrop_path"]
        obj["pic_path"] = getIMGpath(backdrop_path, "w780");
        obj["first_air_date"] = response_json["results"][i]["first_air_date"]
        res.append(obj)

    return jsonify(res)



def getIMGpath(backdrop_path, size):
    res = "https://image.tmdb.org/t/p/" + size + "/" + backdrop_path;
    return res


#search data  query
@app.route('/query')
def getQueryResult():
    data = request.args;
    api_key = data.get("api_key")
    search_type = data.get("search_type");
    search_query = data.get("search_query")
 
    

    req = "https://api.themoviedb.org/3/search/" + search_type + "?api_key=" + api_key +  "&language=en-US&query=" + search_query + "&page=1&include_adult=false"
    response = requests.get(req)
    response_json = response.json()
    
    #print(response_json)

    res = []
    
    movie_genre_dict = dict();
    movie_genre_res = requests.get("https://api.themoviedb.org/3/genre/movie/list?api_key=" + api_key + "&language=en-US");
    movie_genre_json = movie_genre_res.json()
    for item in movie_genre_json["genres"]:
        movie_genre_dict[item["id"]] = item["name"]

    #print(movie_genre_dict)    
    tv_genre_dict = dict()
    tv_genre_res = requests.get("https://api.themoviedb.org/3/genre/tv/list?api_key=" + api_key + "&language=en-US");
    tv_genre_json = tv_genre_res.json()
    for item in tv_genre_json["genres"]:
        tv_genre_dict[item["id"]] = item["name"]
    



    for item in response_json["results"]:
        obj = {}
        #print(item["id"]);  

        if search_type == "multi" and item["media_type"] != "tv" and item["media_type"] != "movie":
            continue
        
        ids = item["genre_ids"]
        


        if (search_type == "multi" and item["media_type"] == "tv") or (search_type == "tv"):
            obj["type"] = "tv"
            #turn the genre id into the string arr
            genre_arr = list()
            for id in ids:
                genre_arr.append(tv_genre_dict[id])
            obj["genre_arr"] = genre_arr

            obj["title"] = item["name"]
            if "first_air_date" in item.keys():
                obj["date"] = item["first_air_date"]     
        else :
            obj["type"] = "movie"
            #turn the genre id into the string arr
            genre_arr = list()
            for id in ids:
                genre_arr.append(movie_genre_dict[id])

            obj["genre_arr"] = genre_arr
            obj["title"] = item["title"]    
            if "release_date" in item.keys():
                obj["date"] = item["release_date"]
                    
        obj["id"] = item["id"]
        obj["overview"] = item["overview"]
        #obj["poster_path"] = item["poster_path"]
        if "poster_path" in item.keys() and item["poster_path"] is not None:
            obj["poster_src"] = getIMGpath(item["poster_path"], "w185")

        
        obj["vote_average"] =  1.0 * item["vote_average"] / 2 #scale down to half
        obj["vote_count"] = item["vote_count"]
        
        #print(obj["genre_arr"])
        res.append(obj);
        
    return jsonify(res);



@app.route('/getMoviePOP')
def getMoviePOP():
    data = request.args
    api_key = data.get("api_key")
    movie_id = data.get("id")

    #get movie detail
    movie_genre_dict = dict();
    movie_genre_res = requests.get("https://api.themoviedb.org/3/genre/movie/list?api_key=" + api_key + "&language=en-US");
    movie_genre_json = movie_genre_res.json()
    for item in movie_genre_json["genres"]:
        movie_genre_dict[item["id"]] = item["name"]


    req = "https://api.themoviedb.org/3/movie/" + movie_id + "?api_key="  + api_key + "&language=en-US"
    response = requests.get(req)
    
    response_json = response.json()
    res = {}
    res["id"] = response_json["id"]
    res["title"] = response_json["title"]
    #res["runtime"] = response_json["runtime"]
    res["release_date"] = response_json["release_date"]

    language_arr = []
    for item in response_json["spoken_languages"]:
        language_arr.append(item["english_name"])
    res["spoken_languages"] = language_arr


    res["vote_average"] = 1.0 * response_json["vote_average"] / 2
    res["vote_count"] = response_json["vote_count"]

    if "backdrop_path" in response_json.keys() and response_json["backdrop_path"] is not None:
        res["img_src"] = getIMGpath(response_json["backdrop_path"], "w780")        
     
   
    genre_arr = list()
    for obj in response_json["genres"]:
        genre_arr.append(movie_genre_dict[obj["id"]])
    
    res["genre_arr"] = genre_arr
    res["link"] = "https://www.themoviedb.org/movie/" + movie_id
    res["overview"] = response_json["overview"]

    #getMovieCredits

    req = "https://api.themoviedb.org/3/movie/" + movie_id + "/credits?api_key="  + api_key + "&language=en-US"
    response = requests.get(req)
    response_json = response.json()
    casts = []

   
    for item in response_json["cast"]:
        obj = {}
        obj["name"] = item["name"]

        if "profile_path" in item.keys() and item["profile_path"] is not None:
            obj["profile_path"] = getIMGpath(item["profile_path"], "w185")
        
        obj["character"] = item["character"]
        casts.append(obj)
        
     
        if len(casts) >= 8:
            break


    res["casts"] = casts



    #getReviews

    req = "https://api.themoviedb.org/3/movie/" + movie_id + "/reviews?api_key="  + api_key + "&language=en-US&page=1"
    response = requests.get(req)
    response_json = response.json()
    reviews = response_json["results"]

    reviews_arr = []

    for review in reviews:
        obj = dict()
        obj["username"] = review["author_details"]["username"]
        obj["content"] = review["content"]

        if "rating" in review["author_details"].keys() and review["author_details"]["rating"] is not None:
            obj["rating"] = 1.0 * (review["author_details"]["rating"]) / 2
        obj["created_at"] = review["created_at"] 
        
        
        reviews_arr.append(obj)
        if  len(reviews_arr) == 5:
            break;


    res["reviews_arr"] = reviews_arr
    return  jsonify(res)  




#getTVPOP data
@app.route('/getTVPOP')
def getTVPOP():
    data = request.args
    api_key = data.get("api_key")
    tv_id = data.get("id")

    
    #get tv genre dict
    tv_genre_dict = dict()
    tv_genre_res = requests.get("https://api.themoviedb.org/3/genre/tv/list?api_key=" + api_key + "&language=en-US");
    tv_genre_json = tv_genre_res.json()
    for item in tv_genre_json["genres"]:
        tv_genre_dict[item["id"]] = item["name"]


    #get tv detail
    req = "https://api.themoviedb.org/3/tv/" + tv_id + "?api_key="  + api_key + "&language=en-US"
    response = requests.get(req)
    
    response_json = response.json()
    res = {}
    res["id"] = response_json["id"]
    res["title"] = response_json["name"]
    #res["runtime"] = response_json["runtime"]
    res["release_date"] = response_json["first_air_date"]

    language_arr = []
    for item in response_json["spoken_languages"]:
        language_arr.append(item["english_name"])
    res["spoken_languages"] = language_arr


    res["vote_average"] = 1.0 * response_json["vote_average"] / 2
    res["vote_count"] = response_json["vote_count"]

    if "backdrop_path" in response_json.keys() and response_json["backdrop_path"] is not None:
        res["img_src"] = getIMGpath(response_json["backdrop_path"], "w780")        
     
   
    genre_arr = list()
    for obj in response_json["genres"]:
        genre_arr.append(tv_genre_dict[obj["id"]])
    
    res["genre_arr"] = genre_arr
    res["link"] = "https://www.themoviedb.org/tv/" + tv_id
    res["overview"] = response_json["overview"]

    #getMovieCredits

    req = "https://api.themoviedb.org/3/tv/" + tv_id + "/credits?api_key="  + api_key + "&language=en-US"
    response = requests.get(req)
    response_json = response.json()
    casts = []

   
    for item in response_json["cast"]:
        obj = {}
        obj["name"] = item["name"]

        if "profile_path" in item.keys() and item["profile_path"] is not None:
            obj["profile_path"] = getIMGpath(item["profile_path"], "w185")
        
        obj["character"] = item["character"]
        casts.append(obj)
        
     
        if len(casts) >= 8:
            break


    res["casts"] = casts



    #getReviews

    req = "https://api.themoviedb.org/3/tv/" + tv_id + "/reviews?api_key="  + api_key + "&language=en-US&page=1"
    response = requests.get(req)
    response_json = response.json()
    reviews = response_json["results"]

    reviews_arr = []

    for review in reviews:
        obj = dict()
        obj["username"] = review["author_details"]["username"]
        obj["content"] = review["content"]

        if "rating" in review["author_details"].keys() and review["author_details"]["rating"] is not None:
            obj["rating"] = 1.0 * (review["author_details"]["rating"]) / 2
        obj["created_at"] = review["created_at"] 
        
        
        reviews_arr.append(obj)
        if  len(reviews_arr) == 5:
            break;


    res["reviews_arr"] = reviews_arr
    return  jsonify(res)  

# @app.route('/getMovieCredits')
# def getMovieCredits():
#     data = request.args
#     api_key = data.get("api_key")
#     movie_id = data.get("id")


#     req = "https://api.themoviedb.org/3/movie/" + movie_id + "/credits?api_key="  + api_key + "&language=en-US"
#     response = requests.get(req)
#     response_json = response.json()
#     res = []

   
#     for item in response_json["cast"]:
#         obj = {}
#         obj["name"] = item["name"]

#         if "profile_path" in item.keys() and item["profile_path"] is not None:
#             obj["profile_path"] = getIMGpath(item["profile_path"], "w185")
        
#         obj["character"] = item["character"]
#         res.append(obj)
        
     
#         if len(res) >= 8:
#             break

#     return jsonify(res)






if __name__ == '__main__':
    app.run(debug=True)    