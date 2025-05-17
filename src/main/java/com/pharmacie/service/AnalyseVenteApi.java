package com.pharmacie.service;

import com.pharmacie.model.AnalyseVenteData;
import com.pharmacie.util.Global;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AnalyseVenteApi {
    private static final String API_BASE_URL = Global.getBaseUrl() + "/analyse-ventes/";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static Map<LocalDate, AnalyseVenteData> getVentesSemaine() throws Exception {
        return fetchData("semaine");
    }

    public static Map<LocalDate, AnalyseVenteData> getVentesMoisJour() throws Exception {
        return fetchData("mois-jour");
    }

    public static Map<LocalDate, AnalyseVenteData> getHistoriqueVentes(int nbMois) throws Exception {
        return fetchData("historique?nbMois=" + nbMois);
    }

    private static Map<LocalDate, AnalyseVenteData> fetchData(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + endpoint))
                .header("Authorization", "Bearer " + Global.getToken())
                .GET()
                .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
    
        // Débogage - afficher la réponse brute
        System.out.println("Réponse API: " + responseBody);
    
        JSONObject json = new JSONObject(responseBody);
        Map<LocalDate, AnalyseVenteData> dataMap = new TreeMap<>();
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("yyyy-MM");
    
        // Parcourir toutes les clés du JSON
        for (String key : json.keySet()) {
            try {
                LocalDate date = null;
                boolean isMonthlyData = false;
                
                // Vérifier le format YYYY-MM-DD (données journalières)
                if (key.matches("(vente|commande|vente-CA)-\\d{4}-\\d{2}-\\d{2}")) {
                    String dateStr = key.substring(key.length() - 10);
                    date = LocalDate.parse(dateStr, formatterDay);
                } 
                // Vérifier le format YYYY-MM (données mensuelles)
                else if (key.matches("(vente|commande|vente-CA)-\\d{4}-\\d{2}")) {
                    String dateStr = key.substring(key.length() - 7);
                    // Pour les données mensuelles, on prend le 1er jour du mois
                    date = LocalDate.parse(dateStr + "-01", formatterDay);
                    isMonthlyData = true;
                } else {
                    System.out.println("Format de clé non reconnu: " + key);
                    continue;
                }
    
                // Créer ou obtenir l'entrée existante
                AnalyseVenteData data = dataMap.getOrDefault(date, new AnalyseVenteData(date, 0, 0, 0));
    
                // Mettre à jour la valeur appropriée
                if (key.startsWith("vente-") && !key.startsWith("vente-CA-")) {
                    data.setVentes(json.getInt(key));
                } else if (key.startsWith("commande-")) {
                    data.setCommandes(json.getInt(key));
                } else if (key.startsWith("vente-CA-")) {
                    data.setChiffreAffaire(json.getDouble(key));
                }
    
                dataMap.put(date, data);
            } catch (Exception e) {
                System.err.println("Erreur lors du traitement de la clé " + key + ": " + e.getMessage());
            }
        }
        
        // Débogage - vérifier les entrées traitées
        System.out.println("Nombre d'entrées après traitement: " + dataMap.size());
        for (Map.Entry<LocalDate, AnalyseVenteData> entry : dataMap.entrySet()) {
            System.out.println("Date: " + entry.getKey() +
                    ", V: " + entry.getValue().getVentes() +
                    ", C: " + entry.getValue().getCommandes() +
                    ", CA: " + entry.getValue().getChiffreAffaire());
        }
        return dataMap;
    }
}