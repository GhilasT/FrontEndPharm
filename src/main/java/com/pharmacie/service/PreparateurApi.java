package com.pharmacie.service;

import com.pharmacie.model.Preparateur;
import com.pharmacie.model.dto.PreparateurCreateRequest;
import com.pharmacie.model.dto.PreparateurUpdateRequest;
import com.pharmacie.util.Global;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
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
 * Classe API pour gérer les opérations CRUD pour les préparateurs.
 * Interagit avec un service backend via HTTP.
 */
public class PreparateurApi {
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String PREPARATEUR_ENDPOINT = "/preparateurs";
    private static final String PREPARATEUR_URL = BASE_URL + PREPARATEUR_ENDPOINT;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructeur pour PreparateurApi.
     * Initialise HttpClient et ObjectMapper.
     */
    public PreparateurApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Récupère tous les préparateurs depuis le service backend.
     *
     * @return Une liste de {@link Preparateur}, ou une liste vide en cas d'erreur.
     */
    public List<Preparateur> getAllPreparateurs() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PREPARATEUR_URL))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parsePreparateursFromJson(response.body());
            } else {
                System.err.println("Erreur GET: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception GET: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des préparateurs en fonction d'un terme de recherche.
     *
     * @param query Le terme de recherche.
     * @return Une liste de {@link Preparateur} correspondant au terme de recherche, ou une liste vide en cas d'erreur.
     */
    public List<Preparateur> searchPreparateurs(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PREPARATEUR_URL + "/search?term=" + encodedQuery))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parsePreparateursFromJson(response.body());
            } else {
                System.err.println("Erreur recherche: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception recherche: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Supprime un préparateur par son ID.
     *
     * @param idPersonne L'ID du préparateur à supprimer.
     * @return {@code true} si la suppression a réussi, {@code false} sinon.
     */
    public boolean deletePreparateur(UUID idPersonne) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PREPARATEUR_URL + "/" + idPersonne))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .DELETE()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("Erreur suppression: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un préparateur existant.
     *
     * @param idPersonne L'ID du préparateur à mettre à jour.
     * @param updateRequest L'objet {@link PreparateurUpdateRequest} contenant les informations de mise à jour.
     * @return {@code true} si la mise à jour a réussi, {@code false} sinon.
     */
    public boolean updatePreparateur(UUID idPersonne, PreparateurUpdateRequest updateRequest) {
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            requestMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String requestBody = requestMapper.writeValueAsString(updateRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PREPARATEUR_URL + "/" + idPersonne))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur mise à jour: " + e.getMessage());
            return false;
        }
    }

    /**
     * Analyse une chaîne JSON et la convertit en une liste d'objets {@link Preparateur}.
     *
     * @param json La chaîne JSON à analyser.
     * @return Une liste d'objets {@link Preparateur}.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de l'analyse.
     */
    private List<Preparateur> parsePreparateursFromJson(String json) throws IOException {
        List<Preparateur> preparateurs = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(json);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                UUID idPersonne = UUID.fromString(node.get("idPersonne").asText());
                String nom = node.get("nom").asText();
                String prenom = node.get("prenom").asText();
                String email = node.get("email").asText();
                String telephone = node.get("telephone").asText();
                String adresse = node.get("adresse").asText();
                String matricule = node.has("matricule") ? node.get("matricule").asText() : "";

                LocalDate dateEmbauche = null;
                if (node.has("dateEmbauche") && !node.get("dateEmbauche").isNull()) {
                    String dateStr = node.get("dateEmbauche").asText();
                    dateEmbauche = LocalDate.parse(dateStr.substring(0, 10), DateTimeFormatter.ISO_DATE);
                }

                double salaire = node.has("salaire") ? node.get("salaire").asDouble() : 0.0;
                String poste = node.has("poste") ? node.get("poste").asText() : "PREPARATEUR";
                String statutContrat = node.has("statutContrat") ? node.get("statutContrat").asText() : "";
                String diplome = node.has("diplome") ? node.get("diplome").asText() : "";
                String emailPro = node.has("emailPro") ? node.get("emailPro").asText() : "";

                preparateurs.add(new Preparateur(
                        idPersonne, nom, prenom, email, telephone, adresse,
                        matricule, dateEmbauche, salaire, poste, statutContrat,
                        diplome, emailPro
                ));
            }
        }
        return preparateurs;
    }

    /**
     * Crée un nouveau préparateur.
     *
     * @param createRequest L'objet {@link PreparateurCreateRequest} contenant les informations du nouveau préparateur.
     * @return {@code true} si la création a réussi, {@code false} sinon.
     */
    public boolean createPreparateur(PreparateurCreateRequest createRequest) {
        try {
            String requestBody = objectMapper.writeValueAsString(createRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PREPARATEUR_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur création: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}