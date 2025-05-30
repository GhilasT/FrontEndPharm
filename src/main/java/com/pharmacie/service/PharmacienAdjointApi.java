package com.pharmacie.service;

import com.pharmacie.model.PharmacienAdjoint;
import com.pharmacie.model.dto.PharmacienAdjointCreateRequest;
import com.pharmacie.model.dto.PharmacienAdjointUpdateRequest;
import com.pharmacie.util.Global;
import com.pharmacie.model.Medecin;

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
 * Classe API pour gérer les opérations CRUD pour les pharmaciens adjoints.
 * Interagit avec un service backend via HTTP.
 * Inclut également des fonctionnalités de recherche pour les médecins.
 */
public class PharmacienAdjointApi {
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String PHARMACIEN_ADJOINT_ENDPOINT = "/pharmaciens-adjoints";
    private static final String PHARMACIEN_ADJOINT_URL = BASE_URL + PHARMACIEN_ADJOINT_ENDPOINT;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructeur pour PharmacienAdjointApi.
     * Initialise HttpClient et ObjectMapper.
     */
    public PharmacienAdjointApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Récupère tous les pharmaciens adjoints depuis le service backend.
     *
     * @return Une liste de {@link PharmacienAdjoint}, ou une liste vide en cas d'erreur.
     */
    public List<PharmacienAdjoint> getAllPharmaciensAdjoints() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PHARMACIEN_ADJOINT_URL))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parsePharmaciensAdjointsFromJson(response.body());
            } else {
                System.err.println("Erreur requête GET: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception GET: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Recherche des pharmaciens adjoints en fonction d'un terme de recherche.
     *
     * @param query Le terme de recherche.
     * @return Une liste de {@link PharmacienAdjoint} correspondant au terme de recherche, ou une liste vide en cas d'erreur.
     */
    public List<PharmacienAdjoint> searchPharmaciensAdjoints(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PHARMACIEN_ADJOINT_URL + "/search?term=" + encodedQuery))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parsePharmaciensAdjointsFromJson(response.body());
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
     * Supprime un pharmacien adjoint par son ID.
     *
     * @param idPersonne L'ID du pharmacien adjoint à supprimer.
     * @return {@code true} si la suppression a réussi, {@code false} sinon.
     */
    public boolean deletePharmacienAdjoint(UUID idPersonne) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PHARMACIEN_ADJOINT_URL + "/" + idPersonne))
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
     * Met à jour un pharmacien adjoint existant.
     *
     * @param idPersonne L'ID du pharmacien adjoint à mettre à jour.
     * @param updateRequest L'objet {@link PharmacienAdjointUpdateRequest} contenant les informations de mise à jour.
     * @return {@code true} si la mise à jour a réussi, {@code false} sinon.
     */
    public boolean updatePharmacienAdjoint(UUID idPersonne, PharmacienAdjointUpdateRequest updateRequest) {
        try {
            ObjectMapper requestMapper = new ObjectMapper();
            requestMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String requestBody = requestMapper.writeValueAsString(updateRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PHARMACIEN_ADJOINT_URL + "/" + idPersonne))
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
     * Analyse une chaîne JSON et la convertit en une liste d'objets {@link PharmacienAdjoint}.
     *
     * @param json La chaîne JSON à analyser.
     * @return Une liste d'objets {@link PharmacienAdjoint}.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de l'analyse.
     */
    private List<PharmacienAdjoint> parsePharmaciensAdjointsFromJson(String json) throws IOException {
        List<PharmacienAdjoint> pharmaciens = new ArrayList<>();
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
                String poste = node.has("poste") ? node.get("poste").asText() : "PHARMACIEN_ADJOINT";
                String statutContrat = node.has("statutContrat") ? node.get("statutContrat").asText() : "";
                String diplome = node.has("diplome") ? node.get("diplome").asText() : "";
                String emailPro = node.has("emailPro") ? node.get("emailPro").asText() : "";

                pharmaciens.add(new PharmacienAdjoint(
                        idPersonne, nom, prenom, email, telephone, adresse,
                        matricule, dateEmbauche, salaire, poste, statutContrat,
                        diplome, emailPro
                ));
            }
        }
        return pharmaciens;
    }

    /**
     * Crée un nouveau pharmacien adjoint.
     *
     * @param createRequest L'objet {@link PharmacienAdjointCreateRequest} contenant les informations du nouveau pharmacien adjoint.
     * @return {@code true} si la création a réussi (statut 201), {@code false} sinon.
     */
    public boolean createPharmacienAdjoint(PharmacienAdjointCreateRequest createRequest) {
        try {
            String requestBody = objectMapper.writeValueAsString(createRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PHARMACIEN_ADJOINT_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Global.getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("Erreur création: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recherche des médecins en fonction d'un terme de recherche.
     * Cette méthode n'est pas strictement nécessaire dans cette classe, mais ajoutée pour cohérence avec la structure existante.
     * Elle pourrait être déplacée dans une classe MedecinApi dédiée si cette dernière est créée plus tard.
     *
     * @param query Le terme de recherche pour les médecins.
     * @return Une liste de {@link Medecin} correspondant à la recherche, ou une liste vide en cas d'erreur.
     */
    public List<Medecin> searchMedecins(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/medecins/search?term=" + encodedQuery))
                    .header("Authorization", "Bearer " + Global.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseMedecinsFromJson(response.body());
            } else {
                System.err.println("Erreur recherche médecins: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Exception recherche médecins: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Analyse une chaîne JSON et la convertit en une liste d'objets {@link Medecin}.
     *
     * @param json La chaîne JSON à analyser.
     * @return Une liste d'objets {@link Medecin}.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de l'analyse.
     */
    private List<Medecin> parseMedecinsFromJson(String json) throws IOException {
        List<Medecin> medecins = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(json);

        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                String civilite = node.has("civilite") ? node.get("civilite").asText() : "";
                String nomExercice = node.has("nomExercice") ? node.get("nomExercice").asText() : "";
                String prenomExercice = node.has("prenomExercice") ? node.get("prenomExercice").asText() : "";
                String rppsMedecin = node.has("rppsMedecin") ? node.get("rppsMedecin").asText() : "";
                String profession = node.has("profession") ? node.get("profession").asText() : "";
                String modeExercice = node.has("modeExercice") ? node.get("modeExercice").asText() : "";
                String qualifications = node.has("qualifications") ? node.get("qualifications").asText() : "";
                String structureExercice = node.has("structureExercice") ? node.get("structureExercice").asText() : "";
                String fonctionActivite = node.has("fonctionActivite") ? node.get("fonctionActivite").asText() : "";
                String genreActivite = node.has("genreActivite") ? node.get("genreActivite").asText() : "";

                Medecin medecin = new Medecin(
                    civilite, nomExercice, prenomExercice, rppsMedecin,
                    profession, modeExercice, qualifications,
                    structureExercice, fonctionActivite, genreActivite
                );
                
                if (node.has("categorieProfessionnelle")) {
                    medecin.setCategorieProfessionnelle(node.get("categorieProfessionnelle").asText());
                }
                
                medecins.add(medecin);
            }
        }
        return medecins;
    }
}
