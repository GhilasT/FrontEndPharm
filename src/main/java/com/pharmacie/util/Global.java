package com.pharmacie.util;

import com.pharmacie.model.Client;

public class Global {
    public static Client GlobalClient = new Client();
    private static final String BASE_URL = "https://pharmaplus-c71b024acb6b.herokuapp.com/api";

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
