const express = require('express');
const app = express();
const port = process.env.PORT || 3000;


// packages loads
const axios = require('axios');
const bodyParser = require('body-parser');

//api_key
const api_key="964498811c07e3642db2e5e0e7966a6d";


app.use(bodyParser.json());

//for cors policy
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*"); // update to match the domain you will make the request from
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});


// // search query 4.1.1
// app.get('/search', (req, res) => {
//   var query_input = req.query.query_input;
//   var url = "https://api.themoviedb.org/3/search/multi?api_key=" + api_key + "&language=enUS&query=" + query_input;
//   axios.get(url).then(response => {
//     var data = response.data;
 
//     var results = [];
//     for (let i = 0; i < data["results"].length; i++) {
//       if (data["results"][i]["backdrop_path"] == null)
//         continue;
//       let item = {};
//       item["id"] = data["results"][i]["id"];
//       item["name"] = data["results"][i]["title"];
//       item["media_type"] = data["results"][i]["media_type"];
//       item["backdrop_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["backdrop_path"];
//       results.push(item);
//       if (results.length == 7)
//         break;
//     }

//     res.json(results);
//   }).catch(error => {
//     console.log(error);
//   });

// });

// search query 4.1.1
app.get('/search', (req, res) => {
  var query_input = req.query.query_input;
  var url = "https://api.themoviedb.org/3/search/multi?api_key=" + api_key + "&language=enUS&query=" + query_input;
  axios.get(url).then(response => {
    var data = response.data;
 
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      if (data["results"][i]["backdrop_path"] == null)
        continue;
      let item = {};
      item["id"] = data["results"][i]["id"];
      if (data["results"][i]["title"] != null)
        item["name"] = data["results"][i]["title"];
      else 
        item["name"] = data["results"][i]["name"];

      if (data["results"][i]["media_type"] == "movie") 
        item["year"] = data["results"][i]["release_date"].substring(0,4);
      
      if (data["results"][i]["media_type"] == "tv") 
        item["year"] = data["results"][i]["first_air_date"].substring(0,4);
        


      item["rating"] = data["results"][i]["vote_average"];
      item["media_type"] = data["results"][i]["media_type"];
      item["backdrop_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["backdrop_path"];
      results.push(item);
      if (results.length == 7)
        break;
    }

    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});




/**
 * Movie part
 */


//get Trending Movies  4.1.2
app.get('/trendingmovies', (req, res) => {
  var url = "https://api.themoviedb.org/3/trending/movie/day?api_key=" + api_key;
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["title"] = data["results"][i]["title"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
      results.push(item);
    }

    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});



//get Top-Rated Movie 4.1.3
app.get('/topratedMovies', (req, res) => {
  var url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + api_key + "&language=en-US&page=1" ;
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["title"] = data["results"][i]["title"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});


// currently playing movies 4.1.4 
app.get('/currentplayingmovies', (req, res) => {
  var url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + api_key + "&language=en-US&page=1" ;
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < 5; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["title"] = data["results"][i]["title"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["backdrop_path"];
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});


// Popular Movies 4.1.5 
app.get('/popularmovies', (req, res) => {
  var url = "https://api.themoviedb.org/3/movie/popular?api_key=" + api_key + "&language=en-US&page=1" ;
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["title"] = data["results"][i]["title"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});

// Recommended Movies 4.1.6        format: /recommendmovies?movie_id=1
app.get('/recommendmovies', (req, res) => {
  var movie_id = req.query.movie_id;
  var url = "https://api.themoviedb.org/3/movie/" + movie_id + "/recommendations?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["title"] = data["results"][i]["title"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
    
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});


// Similar Movies 4.1.7        format: /similarmovies?movie_id=1
app.get('/similarmovies', (req, res) => {
  var movie_id = req.query.movie_id;
  var url = "https://api.themoviedb.org/3/movie/" + movie_id + "/similar?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["title"] = data["results"][i]["title"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
    
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});


//  Movies  video 4.1.8        format: /movievideo?movie_id=1
app.get('/movievideo', (req, res) => {
  var movie_id = req.query.movie_id;
  var url = "https://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["site"] = data["results"][i]["site"];
      item["type"] = data["results"][i]["type"];
      item["name"] = data["results"][i]["name"];
      item["link"] = "https://www.youtube.com/watch?v=" + data["results"][i]["key"];
      item["key"] =  data["results"][i]["key"];
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});


//  Movies  Details 4.1.9        format: /moviedetails?movie_id=1
app.get('/moviedetails', (req, res) => {
  var movie_id = req.query.movie_id;
  var url = "https://api.themoviedb.org/3/movie/" + movie_id + "?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = {};

    results["title"] = data["title"];
    results["genres"] = data["genres"];
    results["spoken_languages"] = data["spoken_languages"];
    results["release_date"] = data["release_date"];
    results["runtime"] = data["runtime"];
    results["overview"] = data["overview"];
    results["vote_average"] = data["vote_average"];
    results["tagline"] = data["tagline"];
    results["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["poster_path"];

    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});


//  Movies  Reviews 4.1.10        format: /moviereviews?movie_id=1
app.get('/moviereviews', (req, res) => {
  var movie_id = req.query.movie_id;
  var url = "https://api.themoviedb.org/3/movie/" + movie_id + "/reviews?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["author"] = data["results"][i]["author"];
      item["content"] = data["results"][i]["content"];
      item["created_at"] = data["results"][i]["created_at"];
      item["url"] = data["results"][i]["url"];
      if ("rating" in data["results"][i]["author_details"] && data["results"][i]["author_details"]["rating"] != null) 
        item["rating"] = data["results"][i]["author_details"]["rating"];
      else 
        item["rating"] = "0";

      if ("avatar_path" in data["results"][i]["author_details"] && data["results"][i]["author_details"]["avatar_path"] != null) {
        if (data["results"][i]["author_details"]["avatar_path"].search("https") == -1)
          item["avatar_path"] = "https://image.tmdb.org/t/p/original" + data["results"][i]["author_details"]["avatar_path"]; 
        else 
          item["avatar_path"] = data["results"][i]["author_details"]["avatar_path"].substring(1);
      } else 
        item["avatar_path"] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU";
      
      
      results.push(item);
      if (i == 9)
        break;
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});



//  Movies  Cast  4.1.11        format: /moviecast?movie_id=1
app.get('/moviecast', (req, res) => {
  var movie_id = req.query.movie_id;
  var url = "https://api.themoviedb.org/3/movie/" + movie_id + "/credits?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["cast"].length; i++) {
      if (!"profile_path" in data["cast"][i] || data["cast"][i]["profile_path"] == null)
        continue;

      let item = {};
      item["id"] = data["cast"][i]["id"];
      item["name"] = data["cast"][i]["name"];
      item["character"] = data["cast"][i]["character"];
    
      item["profile_path"] = "https://image.tmdb.org/t/p/w500/" + data["cast"][i]["profile_path"];
 
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});

/**
 * TV part
 */


//get Trending TV  4.1.12
app.get('/trendingtv', (req, res) => {
  var url = "https://api.themoviedb.org/3/trending/tv/day?api_key=" + api_key;
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["name"] = data["results"][i]["name"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
      results.push(item);
    }

    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});
  
//get Toprated TV 4.1.13
app.get('/topratedtv', ( req, res) => {
  var url = "https://api.themoviedb.org/3/tv/top_rated?api_key=" + api_key + "&language=en-US&page=1" ;
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["name"] = data["results"][i]["name"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});



// Popular TV 4.1.14 
app.get('/populartv', (req, res) => {
  var url = "https://api.themoviedb.org/3/tv/popular?api_key=" + api_key + "&language=en-US&page=1" ;
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["name"] = data["results"][i]["name"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});

// recommend TV 4.1.15 
app.get('/recommendtv', (req, res) => {
  var tv_id = req.query.tv_id;
  var url = "https://api.themoviedb.org/3/tv/" + tv_id + "/recommendations?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["name"] = data["results"][i]["name"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
    
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});



// Similar TV 4.1.16        
app.get('/similartv', (req, res) => {
  var tv_id = req.query.tv_id;
  var url = "https://api.themoviedb.org/3/tv/" + tv_id + "/similar?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["id"] = data["results"][i]["id"];
      item["name"] = data["results"][i]["name"];
      item["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["results"][i]["poster_path"];
    
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});
/**
 * 
 */


//  TV  video 4.1.17        
app.get('/tvvideo', (req, res) => {
  var tv_id = req.query.tv_id;
  var url = "https://api.themoviedb.org/3/tv/" + tv_id + "/videos?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["site"] = data["results"][i]["site"];
      item["type"] = data["results"][i]["type"];
      item["name"] = data["results"][i]["name"];
      item["link"] = "https://www.youtube.com/watch?v=" + data["results"][i]["key"];
      item["key"] =  data["results"][i]["key"];
    
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});


//  TV  Details 4.1.18        
app.get('/tvdetails', (req, res) => {
  var tv_id = req.query.tv_id;
  var url = "https://api.themoviedb.org/3/tv/" + tv_id + "?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = {};

    results["title"] = data["name"];
    results["genres"] = data["genres"];
    results["spoken_languages"] = data["spoken_languages"];
    results["first_air_date"] = data["first_air_date"];
    results["episode_run_time"] = data["episode_run_time"];
    results["overview"] = data["overview"];
    results["vote_average"] = data["vote_average"];
    results["tagline"] = data["tagline"];
    results["poster_path"] = "https://image.tmdb.org/t/p/w500" + data["poster_path"];

    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});


//  TV  Reviews 4.1.19        
app.get('/tvreviews', (req, res) => {
  var tv_id = req.query.tv_id;
  var url = "https://api.themoviedb.org/3/tv/" + tv_id + "/reviews?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["results"].length; i++) {
      let item = {};
      item["author"] = data["results"][i]["author"];
      item["content"] = data["results"][i]["content"];
      item["created_at"] = data["results"][i]["created_at"];
      item["url"] = data["results"][i]["url"];
      if ("rating" in data["results"][i]["author_details"] && data["results"][i]["author_details"]["rating"] != null) 
        item["rating"] = data["results"][i]["author_details"]["rating"];
      else 
        item["rating"] = "0";

       if ("avatar_path" in data["results"][i]["author_details"] && data["results"][i]["author_details"]["avatar_path"] != null) {
        if (data["results"][i]["author_details"]["avatar_path"].search("https") == -1)
          item["avatar_path"] = "https://image.tmdb.org/t/p/original" + data["results"][i]["author_details"]["avatar_path"]; 
        else 
          item["avatar_path"] = data["results"][i]["author_details"]["avatar_path"].substring(1);
      } else 
        item["avatar_path"] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnPmUvFLjjmoYWAbLTEmLLIRCPpV_OgxCVA&usqp=CAU";
      
      results.push(item);
      if (i == 9)
        break;
    }
    res.json(results);
    
  }).catch(error => {
    console.log(error);
  });

});



//  TV  Cast  4.1.20       
app.get('/tvcast', (req, res) => {
  var tv_id = req.query.tv_id;
  var url = "https://api.themoviedb.org/3/tv/" + tv_id + "/credits?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = [];
    for (let i = 0; i < data["cast"].length; i++) {
      if (!"profile_path" in data["cast"][i] || data["cast"][i]["profile_path"] == null)
        continue;

      let item = {};
      item["id"] = data["cast"][i]["id"];
      item["name"] = data["cast"][i]["name"];
      item["character"] = data["cast"][i]["character"];
    
      item["profile_path"] = "https://image.tmdb.org/t/p/w500/" + data["cast"][i]["profile_path"];
 
      results.push(item);
    }
    res.json(results);
  }).catch(error => {
    console.log(error);
  });

});


//cast Details 4.1.21
app.get('/castdetails', (req, res) => {
  var person_id = req.query.person_id;
  var url = "https://api.themoviedb.org/3/person/" + person_id + "?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = {};

    results["birthday"] = data["birthday"];
    results["gender"] = data["gender"];
    results["name"] = data["name"];
    results["homepage"] = data["homepage"];
    results["also_known_as"] = data["also_known_as"];
    results["known_for_department"] = data["known_for_department"];
    results["biography"] = data["biography"];
    results["place_of_birth"] = data["place_of_birth"];
    results["photo"] = "https://image.tmdb.org/t/p/w500" + data["profile_path"];
    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});

// cast external ids  4.1.22
app.get('/castexternal', (req, res) => {
  var person_id = req.query.person_id;
  var url = "https://api.themoviedb.org/3/person/" + person_id + "/external_ids?api_key=" + api_key + "&language=en-US&page=1";
  axios.get(url).then(response => {
    var data = response.data;
    var results = {};

    results["imdb_id"] = data["imdb_id"];
    results["facebook_id"] = data["facebook_id"];
    results["instagram_id"] = data["instagram_id"];
    results["twitter_id"] = data["twitter_id"];
   
    
    res.json(results);
  }).catch(error => {
    console.log(error);
  });
});




app.get('/', (req, res) => {
  res.send('Hello World! Weixin!')
})

app.listen(port, () => {
  console.log("Express API is running at port 3000");
}); 



