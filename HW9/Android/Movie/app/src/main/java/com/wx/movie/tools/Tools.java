package com.wx.movie.tools;

public class Tools {
    public  static  String getMonth(String month) {
        int index = Integer.parseInt(month);
        String[] arr = {"0", "Jan", "Feb", "Mar", "Apr", "May", "June", "July","Aug", "Sept", "Oct", "Nov", "Dec"};
        return arr[index];
    }

    public  static  String getDay(String day) {
        if (day.substring(0, 1) == "0")
            return day.substring(1);
        else
            return day;
    }

}
