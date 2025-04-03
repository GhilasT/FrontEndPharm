package com.pharmacie.service;

import com.pharmacie.model.Admin;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public class AdminService {
    private static final String BASE_URL = "http://localhost:8080/api/administrateurs";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public AdminService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public List<Admin> getAllAdmins() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
                
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseAdminsFromJson(response.body());
            } else {
                System.err.println("Erreur lors de la requête: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception lors de la récupération des administrateurs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public List<Admin> searchAdmins(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/search?query=" + encodedQuery))
                .GET()
                .build();
                
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseAdminsFromJson(response.body());
            } else {
                System.err.println("Erreur lors de la recherche: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception lors de la recherche d'administrateurs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public boolean deleteAdmin(UUID idPersonne) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idPersonne))
                .DELETE()
                .build();
    
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }
    private List<Admin> parseAdminsFromJson(String json) throws IOException {
        List<Admin> admins = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(json);
        
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                UUID idPersonne = UUID.fromString(node.get("idPersonne").asText());
                String nom = node.get("nom").asText();
                String prenom = node.get("prenom").asText();
                String email = node.get("email").asText();
                String telephone = node.get("telephone").asText();
                String adresse = node.get("adresse").asText();
                String matricule = node.get("matricule").asText();
                
                LocalDate dateEmbauche = null;
                if (node.has("dateEmbauche") && !node.get("dateEmbauche").isNull()) {
                    dateEmbauche = LocalDate.parse(node.get("dateEmbauche").asText());
                }
                
                double salaire = node.has("salaire") ? node.get("salaire").asDouble() : 0.0;
                
                String poste = node.has("poste") ? node.get("poste").asText() : "";
                String statutContrat = node.has("statutContrat") ? node.get("statutContrat").asText() : "";
                String diplome = node.has("diplome") ? node.get("diplome").asText() : "";
                String emailPro = node.has("emailPro") ? node.get("emailPro").asText() : "";
                
                Admin admin = new Admin(
                    idPersonne, nom, prenom, email, telephone, adresse,
                    matricule, dateEmbauche, salaire, poste, statutContrat,
                    diplome, emailPro
                );
                
                admins.add(admin);
            }
        }
        
        return admins;
    }
}