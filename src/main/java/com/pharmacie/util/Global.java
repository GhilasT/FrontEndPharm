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
    
    // Improved token getter with null check
    public static String getToken() {
        return token != null ? token : "";
    }
    
    public static void setToken(String newToken) {
        if (newToken != null) {
            token = newToken;
            System.out.println("Token set successfully: " + token.substring(0, Math.min(10, token.length())) + "...");
        } else {
            token = "";
            System.out.println("Warning: Null token received");
        }
    }

    public static UUID getClientId() {
        return GlobalClient.getIdPersonne();
    }
    

}
