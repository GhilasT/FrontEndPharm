package com.pharmacie.service;

import com.pharmacie.model.PharmacienAdjoint;
import com.pharmacie.model.dto.PharmacienAdjointCreateRequest;
import com.pharmacie.model.dto.PharmacienAdjointUpdateRequest;
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

public class PharmacienAdjointApi {
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String PHARMACIEN_ADJOINT_ENDPOINT = "/pharmaciens-adjoints";
    private static final String PHARMACIEN_ADJOINT_URL = BASE_URL + PHARMACIEN_ADJOINT_ENDPOINT;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PharmacienAdjointApi() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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
}
