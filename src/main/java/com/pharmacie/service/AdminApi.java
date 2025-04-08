package com.pharmacie.service;

import com.pharmacie.model.Admin;
import com.pharmacie.model.dto.AdminUpdateRequest;
import com.pharmacie.model.dto.AdministrateurCreateRequest;
import com.pharmacie.util.Global;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public class AdminApi {
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String ADMIN_ENDPOINT = "/administrateurs";
    private static final String ADMIN_URL = BASE_URL + ADMIN_ENDPOINT;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AdminApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Admin> getAllAdmins() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ADMIN_URL))
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
                    .uri(URI.create(ADMIN_URL + "/search?query=" + encodedQuery))
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
                    .uri(URI.create(ADMIN_URL + "/" + idPersonne))
                    .DELETE()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAdmin(UUID idPersonne, AdminUpdateRequest updateRequest) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String requestBody = objectMapper.writeValueAsString(updateRequest);

            // Log du corps de la requête
            System.out.println("Corps de la requête envoyé : " + requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ADMIN_URL + "/" + idPersonne))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Log de la réponse
            System.out.println("Réponse du serveur : Code " + response.statusCode() + ", Body: " + response.body());

            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
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
                    String dateStr = node.get("dateEmbauche").asText();
                    if (dateStr.contains("T")) {
                        dateEmbauche = LocalDate.parse(
                                dateStr.substring(0, 10),
                                DateTimeFormatter.ISO_DATE);
                    } else {
                        dateEmbauche = LocalDate.parse(dateStr);
                    }
                }

                double salaire = node.has("salaire") ? node.get("salaire").asDouble() : 0.0;

                String poste = node.has("poste") ? node.get("poste").asText() : "";
                String statutContrat = node.has("statutContrat") ? node.get("statutContrat").asText() : "";
                String diplome = node.has("diplome") ? node.get("diplome").asText() : "";
                String emailPro = node.has("emailPro") ? node.get("emailPro").asText() : "";
                String role = node.has("role") ? node.get("role").asText() : "";

                Admin admin = new Admin(
                        idPersonne, nom, prenom, email, telephone, adresse,
                        matricule, dateEmbauche, salaire, poste, statutContrat,
                        diplome, emailPro, role);

                admins.add(admin);
            }
        }

        return admins;
    }

    public boolean createAdmin(AdministrateurCreateRequest createRequest) {
        try {
            String requestBody = objectMapper.writeValueAsString(createRequest);

            // Log du corps de la requête
            System.out.println("Corps de la requête d'ajout : " + requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ADMIN_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Log de la réponse
            System.out.println("Réponse du serveur : Code " + response.statusCode() + ", Body: " + response.body());

            return response.statusCode() == 201; // HTTP 201 Created
        } catch (Exception e) {
            System.err.println("Erreur lors de la création d'un administrateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
