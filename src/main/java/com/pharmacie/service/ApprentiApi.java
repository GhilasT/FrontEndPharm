package com.pharmacie.service;

import com.pharmacie.model.Apprenti;
import com.pharmacie.model.dto.ApprentiCreateRequest;
import com.pharmacie.model.dto.ApprentiUpdateRequest;
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

/**
 * Classe API pour gérer les opérations CRUD pour les apprentis.
 * Interagit avec un service backend via HTTP.
 */
public class ApprentiApi {
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String APPRENTI_ENDPOINT = "/apprentis";
    private static final String APPRENTI_URL = BASE_URL + APPRENTI_ENDPOINT;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructeur pour ApprentiApi.
     * Initialise HttpClient et ObjectMapper.
     */
    public ApprentiApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Récupère tous les apprentis depuis le service backend.
     *
     * @return Une liste d'objets {@link Apprenti}, ou une liste vide en cas d'erreur.
     */
    public List<Apprenti> getAllApprentis() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APPRENTI_URL))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseApprentisFromJson(response.body());
            } else {
                System.err.println("Erreur lors de la requête: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception lors de la récupération des apprentis: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des apprentis en fonction d'un terme de recherche.
     *
     * @param query Le terme de recherche.
     * @return Une liste d'{@link Apprenti} correspondant au terme de recherche, ou une liste vide en cas d'erreur.
     */
    public List<Apprenti> searchApprentis(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APPRENTI_URL + "/search?term=" + encodedQuery))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseApprentisFromJson(response.body());
            } else {
                System.err.println("Erreur lors de la recherche: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception lors de la recherche d'apprentis: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Supprime un apprenti par son ID.
     *
     * @param idPersonne L'ID de l'apprenti à supprimer.
     * @return {@code true} si la suppression a réussi, {@code false} sinon.
     */
    public boolean deleteApprenti(UUID idPersonne) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APPRENTI_URL + "/" + idPersonne))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .DELETE()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un apprenti existant.
     *
     * @param idPersonne L'ID de l'apprenti à mettre à jour.
     * @param updateRequest L'objet {@link ApprentiUpdateRequest} contenant les informations de mise à jour.
     * @return {@code true} si la mise à jour a réussi, {@code false} sinon.
     */
    public boolean updateApprenti(UUID idPersonne, ApprentiUpdateRequest updateRequest) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String requestBody = objectMapper.writeValueAsString(updateRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APPRENTI_URL + "/" + idPersonne))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
            return false;
        }
    }

    /**
     * Analyse une chaîne JSON et la convertit en une liste d'objets {@link Apprenti}.
     *
     * @param json La chaîne JSON à analyser.
     * @return Une liste d'objets {@link Apprenti}.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de l'analyse.
     */
    private List<Apprenti> parseApprentisFromJson(String json) throws IOException {
        List<Apprenti> apprentis = new ArrayList<>();
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
                    dateEmbauche = LocalDate.parse(dateStr.substring(0, 10), DateTimeFormatter.ISO_DATE);
                }

                double salaire = node.has("salaire") ? node.get("salaire").asDouble() : 0.0;
                String poste = node.has("poste") ? node.get("poste").asText() : "";
                String statutContrat = node.has("statutContrat") ? node.get("statutContrat").asText() : "";
                String diplome = node.has("diplome") ? node.get("diplome").asText() : "";
                String ecole = node.has("ecole") ? node.get("ecole").asText() : "";
                String emailPro = node.has("emailPro") ? node.get("emailPro").asText() : "";

                apprentis.add(new Apprenti(
                        idPersonne, nom, prenom, email, telephone, adresse,
                        matricule, dateEmbauche, salaire, poste, statutContrat,
                        diplome, ecole, emailPro
                ));
            }
        }
        return apprentis;
    }

    /**
     * Crée un nouvel apprenti.
     *
     * @param createRequest L'objet {@link ApprentiCreateRequest} contenant les informations du nouvel apprenti.
     * @return {@code true} si la création a réussi, {@code false} sinon.
     */
    public boolean createApprenti(ApprentiCreateRequest createRequest) {
        try {
            String requestBody = objectMapper.writeValueAsString(createRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APPRENTI_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création d'un apprenti: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}