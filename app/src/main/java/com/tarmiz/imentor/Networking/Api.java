package com.tarmiz.imentor.Networking;

public class Api {

    private static String IP = "imentor.ly";
    private static String PORT = "80";
     private static String BASE = "http://" + IP + ":" + PORT  + "/api/";



    public static String getAllAdverts() {
        return BASE + "advertisements";
    }

    public static String getAllSchools() {
        return BASE + "schools";
    }

    public static String getSliderImages() {
        return BASE + "slider";
    }

    public static String getAllUniversity() {
        return BASE + "universities";
    }

    public static String getAllCenter() {
        return BASE + "tcenters";
    }

    public static String getAllTeachers() {
        return BASE + "teachers";
    }

    public static String getAllSneeds() {
        return BASE + "sneeds";
    }

    public static String getAllImages() {
        return BASE + "gallaryimages";
    }

}
