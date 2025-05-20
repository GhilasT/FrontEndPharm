package com.pharmacie.service;

import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.dto.FournisseurCreateRequest;
import com.pharmacie.model.dto.FournisseurUpdateRequest;
import com.pharmacie.util.Global;

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

/**
 * Classe API pour gérer les opérations CRUD pour les fournisseurs.
 * Interagit avec un service backend via HTTP.
 */
public class FournisseurApi {
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String FOURNISSEUR_ENDPOINT = "/fournisseurs";
    private static final String FOURNISSEUR_URL = BASE_URL + FOURNISSEUR_ENDPOINT;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructeur pour FournisseurApi.
     * Initialise HttpClient et ObjectMapper.
     */
    public FournisseurApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // Opération CRUD

    /**
     * Récupère tous les fournisseurs depuis le service backend.
     *
     * @return Une liste de {@link Fournisseur}, ou une liste vide en cas d'erreur.
     */
    public List<Fournisseur> getAllFournisseurs() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL))
                    .header("Authorization", "Bearer " + Global.getToken())
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

    /**
     * Récupère un fournisseur par son ID.
     *
     * @param id L'ID du fournisseur à récupérer.
     * @return Un {@link Optional} contenant le {@link Fournisseur} s'il est trouvé, sinon un {@link Optional} vide.
     */
    public Optional<Fournisseur> getFournisseurById(UUID id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/" + id))
                    .header("Authorization", "Bearer " + Global.getToken())
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

    /**
     * Récupère un fournisseur par son adresse e-mail.
     *
     * @param email L'adresse e-mail du fournisseur à récupérer.
     * @return Un {@link Optional} contenant le {@link Fournisseur} s'il est trouvé, sinon un {@link Optional} vide.
     */
    public Optional<Fournisseur> getFournisseurByEmail(String email) {
        try {
            String encodedEmail = java.net.URLEncoder.encode(email, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/email/" + encodedEmail))
                    .header("Authorization", "Bearer " + Global.getToken())
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

    /**
     * Récupère un fournisseur par son numéro de téléphone.
     *
     * @param telephone Le numéro de téléphone du fournisseur à récupérer.
     * @return Un {@link Optional} contenant le {@link Fournisseur} s'il est trouvé, sinon un {@link Optional} vide.
     */
    public Optional<Fournisseur> getFournisseurByTelephone(String telephone) {
        try {
            String encodedTel = java.net.URLEncoder.encode(telephone, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/telephone/" + encodedTel))
                    .header("Authorization", "Bearer " + Global.getToken())
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

    /**
     * Crée un nouveau fournisseur.
     *
     * @param request L'objet {@link FournisseurCreateRequest} contenant les informations du nouveau fournisseur.
     * @return {@code true} si la création a réussi (statut 201), {@code false} sinon.
     */
    public boolean createFournisseur(FournisseurCreateRequest request) {
        try {
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());
            return response.statusCode() == 201; // 201 Created
        } catch (Exception e) {
            System.err.println("Erreur createFournisseur: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour un fournisseur existant.
     *
     * @param id L'ID du fournisseur à mettre à jour.
     * @param request L'objet {@link FournisseurUpdateRequest} contenant les informations de mise à jour.
     * @return {@code true} si la mise à jour a réussi, {@code false} sinon.
     */
    public boolean updateFournisseur(UUID id, FournisseurUpdateRequest request) {
        try {
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Erreur updateFournisseur: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recherche des fournisseurs en fonction d'un terme de recherche.
     *
     * @param query Le terme de recherche.
     * @return Une liste de {@link Fournisseur} correspondant au terme de recherche, ou une liste vide en cas d'erreur.
     */
    public List<Fournisseur> searchFournisseurs(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/search?q=" + encodedQuery))
                    .header("Authorization", "Bearer " + Global.getToken())
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

    /**
     * Supprime un fournisseur par son ID.
     *
     * @param id L'ID du fournisseur à supprimer.
     * @return {@code true} si la suppression a réussi (statut 204), {@code false} sinon.
     */
    public boolean deleteFournisseur(UUID id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FOURNISSEUR_URL + "/" + id))
                    .header("Authorization", "Bearer " + Global.getToken())
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

    /**
     * Analyse une chaîne JSON et la convertit en une liste d'objets {@link Fournisseur}.
     *
     * @param json La chaîne JSON à analyser.
     * @return Une liste d'objets {@link Fournisseur}.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de l'analyse.
     */
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

    /**
     * Analyse une chaîne JSON et la convertit en un objet {@link Fournisseur}.
     *
     * @param json La chaîne JSON à analyser.
     * @return Un objet {@link Fournisseur}.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de l'analyse.
     */
    private Fournisseur parseFournisseurFromJson(String json) throws IOException {
        JsonNode node = objectMapper.readTree(json);
        return parseFournisseurNode(node);
    }

    /**
     * Convertit un nœud JSON en un objet {@link Fournisseur}.
     *
     * @param node Le {@link JsonNode} à convertir.
     * @return Un objet {@link Fournisseur}.
     */
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