var api_key="964498811c07e3642db2e5e0e7966a6d";

function click_home() {
    var home_tab = document.getElementById("home_tab");
    var search_tab = document.getElementById("search_tab");
    var line1 = document.getElementById("line1");
    var line2 = document.getElementById("line2");
    home_tab.style.color = "red";
    search_tab.style.color = "white";
   
    var f1 = document.getElementById("f1");
    var f2 = document.getElementById("f2");
    f1.style.display = "block";
    f2.style.display = "none";

    line1.style.display = "block";
    line2.style.display = "none";

}


function click_search() {
    var home_tab = document.getElementById("home_tab");
    var search_tab = document.getElementById("search_tab");
    var line1 = document.getElementById("line1");
    var line2 = document.getElementById("line2");

    

    home_tab.style.color = "white";
    search_tab.style.color = "red";

    f1.style.display = "none";
    f2.style.display = "block";
    line1.style.display = "none";
    line2.style.display = "block";
    
}

function loadData() {
    var media_type="movie";
    var time_window="week";
    //movie data
    var request = new XMLHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState == 4 && request.status==200) {
            
                response = request.responseText;
                processMovieData(response);
            
        }
    }

    request.open("GET", "/getTrendingMovies?media_type=" + media_type + "&time_window=" + time_window + "&api_key=" + api_key);
    request.send();

    // today TV 
    var request2 = new XMLHttpRequest();
    request2.onreadystatechange = function() {
        if (request2.readyState == 4 && request2.status==200) {
            
                response2 = request2.responseText;
                processTVData(response2);
            
        }
    }

    request2.open("GET", "/getTodayTV?api_key=" + api_key);
    request2.send();



}

function processMovieData(res) {
    //console.log(res);
    //console.log(typeof(res)); //string
    var data = JSON.parse(res)
    //console.log(data)
    //console.log(typeof(data)); object
    var movie_img = document.getElementById("movie_img")
    var intro1 = document.getElementById("intro1");
    var index = 0;
    //console.log(data[0]["pic_path"]);

    //avoid delay
    movie_img.src = data[index]["pic_path"];
    intro1.innerHTML = data[index]["title"] + " (" + data[index]["release_data"].substring(0, 4); + ")";

    index++;
    //set timer
    timer = setInterval(function() {
        movie_img.src = data[index]["pic_path"];
        var year = data[index]["release_data"].substring(0, 4);
        intro1.innerHTML = data[index]["title"] + " (" + year + ")";

        index++;
        if (index >= 5) 
            index = 0;
    }, 3000);

}


function processTVData(res) {
    //console.log(res);
    //console.log(typeof(res)); //string
    var data = JSON.parse(res)
    //console.log(data)
    //console.log(typeof(data)); object
    var tv_img = document.getElementById("tv_img")
    var intro2 = document.getElementById("intro2");
    var index = 0;
    //console.log(data[0]["pic_path"]);

    //avoid delay
    tv_img.src = data[index]["pic_path"];
    intro2.innerHTML = data[index]["name"] + " (" + data[index]["first_air_date"].substring(0, 4); + ")";
    index++;

    //set timer
    timer = setInterval(function() {
        tv_img.src = data[index]["pic_path"];
        var year = data[index]["first_air_date"].substring(0, 4);
        intro2.innerHTML = data[index]["name"] + " (" + year + ")";

        index++;
        if (index >= 5) 
            index = 0;
    }, 3000);

}


function hander_form(query_form) {
    //alert(query_form.keyword.value);
    //alert(query_form.type.value);


    var blackpart = document.getElementById("black_part");
    blackpart.style.display = "none";
    
    if (query_form.keyword.value == "" || query_form.type.value == "") {
         alert("Invalid Input! It cannot be null.");
         return false;
     }

     var search_query = query_form.keyword.value;
     search_query.replace(/\n/g, "%20");

     var search_type = query_form.type.value;
     
    //alert(search_type);
    
     var request = new XMLHttpRequest();
     request.onreadystatechange = function() {
         if (request.readyState == 4 && request.status == 200) {
             response = request.responseText;
             ProcessQueryResults(response);
         }
     }
     
     //console.log(search_query);
     request.open("GET", "/query?search_type=" + search_type + "&search_query=" + search_query +  "&api_key=" + api_key);
     request.send();

    return true;
}

function ProcessQueryResults(res) {
    var data = JSON.parse(res);

    // remove last query's data
    var list_container = document.getElementById("list");
    list_container.innerHTML = "";
    

    var no_results_div = document.getElementById("no_result");
    var show_results_div = document.getElementById("show_result");
    if (data.length == 0) { 
        no_results_div.style.display = "block";
        show_results_div.style.display = "none";
    } else {
        show_results_div.style.display = "block";
        no_results_div.style.display = "none";
    }
        
    CreatingItems(data);

}

function CreatingItems(data) {
    var list_container = document.getElementById("list");
    var size = data.length;
    console.log(data);
    for (var i = 0; i < size; i++) {
        var genre_str = "";
        for (var s in data[i]["genre_arr"]) { // s is the key 
            genre_str = genre_str + ", " + data[i]["genre_arr"][s];
        }
        genre_str = genre_str.substring(1);

        var date_time = " ";
        var poster_src = "/static/img/nofound.png";
        if ("date" in data[i])
            date_time = data[i]["date"].substring(0, 4);

        if ("poster_src" in data[i])
            poster_src = data[i]["poster_src"];    
        
        var item_div = document.createElement("div");
        item_div.setAttribute("class", "item");
        item_div.innerHTML = "<img class='cover' src='" + poster_src + "'>"
                            + "<div class='detail'>"
                            + "<h4>" + data[i]["title"] + "</h4>"
                            + "<h5>" + date_time + " | " + genre_str + "</h5>"
                            + "<h6><em>★" + data[i]["vote_average"] +"/5</em> &nbsp" + data[i]["vote_count"] + " votes</h6>"
                            + "<p class='item_p'>" + data[i]["overview"] + "</p>" 
                            + "<button  onclick='Show_more_pop(this)'  id='" + data[i]["type"] + "_" + data[i]["id"] + "'" + "class='show_more_btn'>Show more</button>"
                            + "</div>";

 
         list_container.appendChild(item_div);         
        
       
        
     }
 

    //  var btns = document.querySelectorAll(".show_more_btn");
    // //  //console.log(btns.length);
     

    //  for (var index = 0; index < btns.length; index++) {
    //      //console.log(data[index]);
        
    //     //  btns[index].onclick = function(item) {
    //     //     return Show_more_pop(item);
    //     //  }(data[index]);

    //     btns[index].addEventListener("click",Show_more_pop(data[index]), false);
    //  }

}



function Show_more_pop(div) {
    var str = div.id;
    var type = str.split("_")[0];
    var id = str.split("_")[1];

    var pop_div = document.getElementById("pop_div");
    pop_div.innerHTML="";
      
    if (type == "movie") {
        MoviePop(id);
    } 
    if (type == "tv") {
        TVPop(id);
    }

    open_div();

}


function MoviePop(id) {

    var request = new XMLHttpRequest();
     request.onreadystatechange = function() {
         if (request.readyState == 4 && request.status == 200) {
             response = request.responseText;
             ProcessPopData(response);
         }
     }

     request.open("GET", "/getMoviePOP?id=" + id + "&api_key=" + api_key);
     request.send();

}

function TVPop(id) {
    var request = new XMLHttpRequest();
     request.onreadystatechange = function() {
         if (request.readyState == 4 && request.status == 200) {
             response = request.responseText;
             ProcessPopData(response);
         }
     }

     request.open("GET", "/getTVPOP?id=" + id + "&api_key=" + api_key);
     request.send();

}


function ProcessPopData(res) {
    console.log(res);

    var data = JSON.parse(res); 
    var pop_div = document.getElementById("pop_div");

    /**
     * process  details
     */

    var pop_content = document.createElement("div"); //2
    pop_content.setAttribute("id", "pop_content");  //2


    var genre_str = "";
    for (var s in data["genre_arr"]) { // s is the key 
        genre_str = genre_str + ", " + data["genre_arr"][s];
    }
    genre_str = genre_str.substring(1);

    var language_str = "";
    for (var s in data["spoken_languages"]) { // s is the key 
        language_str = language_str + ", " + data["spoken_languages"][s];
    }
    language_str = language_str.substring(1); 

    var img_src = "/static/img/movie-placeholder.jpg";
    if (data["img_src"] != null)
        img_src = data["img_src"];


    //pop_div.innerHTML = "<div id='pop_content'>" + "<div id='pic_poster'>"
    pop_content.innerHTML = "<div id='pic_poster'>"
                        + "<img id='pop_img' src='" + img_src +    "'>" 
                        + "<button id='btn_popdiv' onclick='close_popdiv()'>x</button> "
                        + "</div>" 
                        + "<h2 class='pop_title'>" + data["title"] +  "<a target='view_window' class='p_link' href='" + data["link"] + " '>    ⓘ</a></h2>" 
                        + "<h5>" + data["release_date"].substring(0, 4) + " | " + genre_str + "</h5>"
                        + "<h6 class='pop_score'><em>★" + data["vote_average"] + "/5</em> &nbsp" + data["vote_count"] + " votes</h6>" 
                        + "<p>" + data["overview"] + "</p>"
                        + "<p class='spoken_language'>Spoken languages: " + language_str +  "</p>"
                        + "<h3 class='cast_title'>Cast</h3>";
                        


    /**
     * process casts
     */

 
     
    var profile_path = "/static/img/person-placeholder.png";
    var cast_container = document.createElement("div");
    cast_container.setAttribute("id", "cast_list");

    var casts = data["casts"]
    for (var i = 0; i < casts.length; i++) {
        if (casts[i]["profile_path"] != null)
            profile_path = casts[i]["profile_path"];


        var nickname = casts[i]["character"]
        // if (nickname != undefined && nickname.length > 35)
        //         nickname = nickname.substring(0, 25)    
        // nickname = nickname + "..."  
        let cast_item = document.createElement("div");
        cast_item.setAttribute("class", "cast_item");

        cast_item.innerHTML = "<img class='cast_img'  src=' " + profile_path + "'" + ">"
                          + "<h5 class='cast_name'>" + casts[i]["name"] + "</h5>"
                          + "<h6 class='cast_as'>AS</h6>"  
                          + "<h6 class='cast_nickname'>" + nickname + "</h6>"
                         
        cast_container.appendChild(cast_item);
    }
    pop_content.appendChild(cast_container);

   
    


    /**
     * process  reviews
     */

    var review_title = document.createElement("h3");
    review_title.setAttribute("class", "reviews_tag");
    review_title.innerHTML = "Reviews"
    pop_content.appendChild(review_title);


    var reviews_arr = data["reviews_arr"];
    var review_container = document.createElement("div");
    review_container.setAttribute("id", "reviews_list");
    
    
    for (let i = 0; i < reviews_arr.length; i++) {
        var review_item = document.createElement("div");
        review_item .setAttribute("class", "review_item");
           

        let date = reviews_arr[i]["created_at"].substring(0,10);
        let tmp = date.split("-");
        date = tmp[1] + "-" + tmp[2] + "-" + tmp[0];

        let rate_part = "";
        if ("rating" in reviews_arr[i])
            rate_part = "<h6 class='review_score'><em>★" + reviews_arr[i]["rating"].toFixed(1) + "/5</em></h6>";



        review_item.innerHTML = "<h5 class='review_nickname'><strong>" + reviews_arr[i]["username"] + "</strong> on " + date + "</h5>"
                              + rate_part
                              + "<p class='review_p'>" + reviews_arr[i]["content"] + "</p>"
                              + "<hr class='review_hr'>";

        review_container.append(review_item)                      

    }

    pop_content.append(review_container)
        
   

    // add all
    pop_div.append(pop_content);

    
}



// function ProcessMovieCredits(res) {
//     var data = JSON.parse(res); 
//     var pop_div = document.getElementById("pop_div");

//     var cast_container = document.createElement("div");

//     var profile_path = "/static/img/nofound.png";
    

//     for (var i = 0; i < data.length; i++) {
//         if (data[i]["profile_path"] != null)
//             profile_path = data[i]["profile_path"];


//         var nickname = data[i]["character"]
//         if (nickname != undefined && nickname.length > 34)
//                 nickname = nickname.substring(0, 35)    
//         nickname = nickname + "..."  
//         let cast_item = document.createElement("div");
//         cast_item.setAttribute("class", "cast_item");

//         cast_item.innerHTML = "<img class='cast_img'  src=' " + profile_path + "'" + ">"
//                           + "<h5 class='cast_name'>" + data[i]["name"] + "</h5>"
//                           + "<h6 class='cast_as'>AS</h6>"  
//                           + "<h6 class='cast_nickname'>" + nickname + "</h6>"
                         

//         cast_container.appendChild(cast_item);
//     }
//     pop_div.appendChild(cast_container);

   

// }



// function ProcessMovieDetail(res) {
//     var data = JSON.parse(res); 
//     var pop_div = document.getElementById("pop_div");

//     var genre_str = "";
//     for (var s in data["genre_arr"]) { // s is the key 
//         genre_str = genre_str + "," + data["genre_arr"][s];
//     }
//     genre_str = genre_str.substring(1);

//     var language_str = "";
//     for (var s in data["spoken_languages"]) { // s is the key 
//         language_str = language_str + "," + data["spoken_languages"][s];
//     }
//     language_str = language_str.substring(1); 

//     var img_src = "/static/img/nofound.png";
//     if (data["img_src"] != null)
//         img_src = data["img_src"];


//     pop_div.innerHTML = "<div id='pop_content'>" + "<div id='pic_poster'>"
//                         + "<img id='pop_img' src='" + img_src +    "'>" 
//                         + "<button id='btn_popdiv' onclick='close_popdiv()'>x</button> "
//                         + "</div>" 
//                         + "<h2 class='pop_title'>" + data["title"] +  "<a target='view_window' class='p_link' href='" + data["link"] + " '>    ⓘ</a></h2>" 
//                         + "<h5>" + data["release_date"].substring(0, 4) + " | " + genre_str + "</h5>"
//                         + "<h6 class='pop_score'><em>★" + data["vote_average"] + "/5</em> &nbsp" + data["vote_count"] + "votes</h6>" 
//                         + "<p>" + data["overview"] + "</p>"
//                         + "<p>Spoken languages: " + language_str +  "</p>";
                        

// }







function ProcessTVPop(id) {
    
}





function open_div() {
    var pop_div = document.getElementById("pop_div");
    pop_div.style.display = "block";
}

function close_popdiv() {
    var pop_div = document.getElementById("pop_div");
    pop_div.style.display = "none";
}