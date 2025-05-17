package com.pharmacie.util;

import java.util.UUID;

import com.pharmacie.model.Client;

public class Global {
    public static Client GlobalClient = new Client();
    private static final String Hosted_URL = "https://pharmaplus-c71b024acb6b.herokuapp.com/api";
    public static final String Local_URL = "http://localhost:8080/api";
    public static final String BASE_URL = Hosted_URL;
    public static String getBaseUrl() {
        return BASE_URL;
    }
    private static String token = "";
    public static String getToken() {
        return token;
    }
    public static void setToken(String token) {
        Global.token = token;
    }

    public static UUID getClientId() {
        return GlobalClient.getIdPersonne();
    }
    

}
