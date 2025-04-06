package com.pharmacie.service;

import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.dto.FournisseurCreateRequest;
import com.pharmacie.model.dto.FournisseurUpdateRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class FournisseurApi {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final String FOURNISSEUR_ENDPOINT = "/fournisseurs";
    private static final String FOURNISSEUR_URL = BASE_URL + FOURNISSEUR_ENDPOINT;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FournisseurApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // Opération CRUD

    public List<Fournisseur> getAllFournisseurs() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseFournisseursFromJson(response.body());
            } else {
                System.err.println("Erreur GET /fournisseurs: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("Exception getAllFournisseurs: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<Fournisseur> getFournisseurById(UUID id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/" + id))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(parseFournisseurFromJson(response.body()));
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Erreur getFournisseurById: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Fournisseur> getFournisseurByEmail(String email) {
        try {
            String encodedEmail = java.net.URLEncoder.encode(email, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/email/" + encodedEmail))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(parseFournisseurFromJson(response.body()));
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Erreur getFournisseurByEmail: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Fournisseur> getFournisseurByTelephone(String telephone) {
        try {
            String encodedTel = java.net.URLEncoder.encode(telephone, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/telephone/" + encodedTel))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(parseFournisseurFromJson(response.body()));
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Erreur getFournisseurByTelephone: " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean createFournisseur(FournisseurCreateRequest request) {
        try {
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());
            return response.statusCode() == 201; // 201 Created
        } catch (Exception e) {
            System.err.println("Erreur createFournisseur: " + e.getMessage());
            return false;
        }
    }

    public boolean updateFournisseur(UUID id, FournisseurUpdateRequest request) {
        try {
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur updateFournisseur: " + e.getMessage());
            return false;
        }
    }
    public List<Fournisseur> searchFournisseurs(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/search?term=" + encodedQuery))
                    .GET()
                    .build();
    
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
    
            if (response.statusCode() == 200) {
                return parseFournisseursFromJson(response.body());
            } else {
                System.err.println("Erreur recherche fournisseurs: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("Exception searchFournisseurs: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public boolean deleteFournisseur(UUID id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/" + id))
                    .DELETE()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, BodyHandlers.discarding());
            return response.statusCode() == 204; // 204 No Content
        } catch (Exception e) {
            System.err.println("Erreur deleteFournisseur: " + e.getMessage());
            return false;
        }
    }

    // Parsing JSON

    private List<Fournisseur> parseFournisseursFromJson(String json) throws IOException {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(json);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                fournisseurs.add(parseFournisseurNode(node));
            }
        }
        return fournisseurs;
    }

    private Fournisseur parseFournisseurFromJson(String json) throws IOException {
        JsonNode node = objectMapper.readTree(json);
        return parseFournisseurNode(node);
    }

    private Fournisseur parseFournisseurNode(JsonNode node) {
        UUID id = UUID.fromString(node.get("idFournisseur").asText());
        String nomSociete = node.get("nomSociete").asText();
        String sujetFonction = node.has("sujetFonction") ? node.get("sujetFonction").asText() : null;
        String fax = node.has("fax") ? node.get("fax").asText() : null;
        String email = node.get("email").asText();
        String telephone = node.get("telephone").asText();
        String adresse = node.get("adresse").asText();

        return new Fournisseur(id, nomSociete, sujetFonction, fax, email, telephone, adresse);
    }
}