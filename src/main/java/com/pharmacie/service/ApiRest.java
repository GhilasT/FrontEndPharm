package com.pharmacie.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacie.model.*;
import com.pharmacie.model.dto.MedecinResponse;
import com.pharmacie.model.dto.OrdonnanceCreateRequest;
import com.pharmacie.model.dto.PrescriptionCreateRequest;
import com.pharmacie.util.Global;

import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.pharmacie.model.dto.MedecinCreateRequest;

import static com.pharmacie.util.Global.BASE_URL;

/**
 * Service pour interagir avec l'API REST du backend.
 * Permet de récupérer les données des médicaments et des ventes depuis le
 * backend.
 */
public class ApiRest {

    private static final String API_BASE_URL = Global.getBaseUrl();
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    public static final Logger LOGGER = Logger.getLogger(ApiRest.class.getName());

    // Taille de page par défaut retournée par le backend
    private static final int DEFAULT_PAGE_SIZE = 50;

    public static boolean validerOrdonnance(UUID id){
        if(id != null){
            return true;
        }else {
            return false;
        }
    }

    public static Boolean OrdonnaceValide=false;

    /**
     * Récupère l'URL de base de l'API.
     * 
     * @return URL de base de l'API
     */
    public static String getApiBaseUrl() {
        return API_BASE_URL;
    }

    /**
     * Récupère tous les médicaments depuis l'API.
     * 
     * @return Liste des médicaments
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static List<Medicament> getMedicaments() throws Exception {
        return getMedicaments("");
    }




    public static List<Medicament> getAllMedicaments(String searchTerm) throws Exception {
        String url = API_BASE_URL + "/medicaments/search/all?searchTerm=" + searchTerm;

        LOGGER.log(Level.INFO, "Envoi d'une requête GET de recherche complète à {0}", url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return parseMedicamentsResponse(response.body());
        } else {
            throw new Exception("Erreur HTTP " + response.statusCode());
        }
    }

    /**
     * Recherche des médicaments par terme de recherche.
     * 
     * @param searchTerm Terme de recherche
     * @return Liste des médicaments correspondant au terme de recherche
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static List<Medicament> getMedicaments(String searchTerm) throws Exception {
        String url = API_BASE_URL + "/medicaments/1";

        if (searchTerm != null && !searchTerm.isEmpty()) {
            url = API_BASE_URL + "/medicaments/search?search=" + searchTerm;
        }

        LOGGER.log(Level.INFO, "Envoi d'une requête GET à {0}", url);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse reçue avec le code {0}", response.statusCode());

            if (response.statusCode() == 200) {
                List<Medicament> medicaments = parseMedicamentsResponse(response.body());
                LOGGER.log(Level.INFO, "Récupération réussie de {0} médicaments", medicaments.size());
                return medicaments;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode()
                        + " lors de la récupération des médicaments";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    /**
     * Récupère une page de médicaments depuis l'API.
     * 
     * @param page Numéro de page (commence à 0)
     * @return Réponse paginée contenant les médicaments et les métadonnées de
     *         pagination
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static PageResponse<Medicament> getMedicamentsPagines(int page) throws Exception {
        return getMedicamentsPagines(page, null);
    }

    /**
     * Recherche des médicaments par terme de recherche avec pagination.
     * 
     * @param page       Numéro de page (commence à 0)
     * @param searchTerm Terme de recherche (optionnel)
     * @return Réponse paginée contenant les médicaments et les métadonnées de
     *         pagination
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static PageResponse<Medicament> getMedicamentsPagines(int page, String searchTerm) throws Exception {
        String url = API_BASE_URL + "/medicaments/" + page;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            url += "?searchTerm=" + searchTerm;
        }

        LOGGER.log(Level.INFO, "Envoi d'une requête GET paginée à " + url);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse paginée reçue avec le code {0}", response.statusCode());

            if (response.statusCode() == 200) {
                System.out.println("🧾 JSON brut reçu :\n" + response.body());
                PageResponse<Medicament> pageResponse = parsePageResponse(response.body());
                LOGGER.log(Level.INFO, "Récupération réussie de la page {0}/{1} avec {2} médicaments",
                        new Object[] { pageResponse.getCurrentPage(), pageResponse.getTotalPages(),
                                pageResponse.getContent().size() });
                return pageResponse;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode()
                        + " lors de la récupération des médicaments paginés";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API paginée", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    /**
     * Récupère une page de médicaments depuis l'API de manière asynchrone.
     * 
     * @param page       Numéro de page (commence à 0)
     * @param searchTerm Terme de recherche (optionnel)
     * @return CompletableFuture contenant la réponse paginée
     */
    public static CompletableFuture<PageResponse<Medicament>> getMedicamentsPaginesAsync(int page, String searchTerm) {
        String url = API_BASE_URL + "/medicaments/" + page;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            url += "?search=" + searchTerm;
        }

        final String finalUrl = url;
        LOGGER.log(Level.INFO, "Envoi d'une requête GET paginée asynchrone à {0}", finalUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    LOGGER.log(Level.INFO, "Réponse paginée asynchrone reçue avec le code {0}", response.statusCode());

                    if (response.statusCode() != 200) {
                        String errorMessage = "Erreur HTTP " + response.statusCode()
                                + " lors de la récupération des médicaments paginés";
                        LOGGER.log(Level.SEVERE, errorMessage);
                        throw new RuntimeException(errorMessage);
                    }

                    PageResponse<Medicament> pageResponse = parsePageResponse(response.body());
                    LOGGER.log(Level.INFO, "Récupération asynchrone réussie de la page {0}/{1} avec {2} médicaments",
                            new Object[] { pageResponse.getCurrentPage(), pageResponse.getTotalPages(),
                                    pageResponse.getContent().size() });
                    return pageResponse;
                });
    }

    /**
     * Récupère les médicaments depuis l'API de manière asynchrone.
     * 
     * @param searchTerm Terme de recherche (optionnel)
     * @return CompletableFuture contenant la liste des médicaments
     */
    public static CompletableFuture<List<Medicament>> getMedicamentsAsync(String searchTerm) {
        String url = API_BASE_URL + "/medicaments/1";

        if (searchTerm != null && !searchTerm.isEmpty()) {
            url = API_BASE_URL + "/medicaments/search?searchTerm=" + searchTerm;
        }

        final String finalUrl = url;
        LOGGER.log(Level.INFO, "Envoi d'une requête GET asynchrone à {0}", finalUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    LOGGER.log(Level.INFO, "Réponse asynchrone reçue avec le code {0}", response.statusCode());

                    if (response.statusCode() != 200) {
                        String errorMessage = "Erreur HTTP " + response.statusCode()
                                + " lors de la récupération des médicaments";
                        LOGGER.log(Level.SEVERE, errorMessage);
                        throw new RuntimeException(errorMessage);
                    }

                    List<Medicament> medicaments = parseMedicamentsResponse(response.body());
                    LOGGER.log(Level.INFO, "Récupération asynchrone réussie de {0} médicaments", medicaments.size());
                    return medicaments;
                });
    }

    /**
     * Vérifie si un médicament nécessite une ordonnance.
     * 
     * @param medicamentId ID du médicament
     * @return true si le médicament nécessite une ordonnance, false sinon
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static boolean checkOrdonnanceRequise(Long medicamentId) throws Exception {
        String url = API_BASE_URL + "/medicaments/id/" + medicamentId + "/ordonnance";

        LOGGER.log(Level.INFO, "Vérification si ordonnance requise pour le médicament {0}", medicamentId);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                boolean ordonnanceRequise = Boolean.parseBoolean(response.body());
                LOGGER.log(Level.INFO, "Médicament {0} nécessite ordonnance: {1}",
                        new Object[] { medicamentId, ordonnanceRequise });
                return ordonnanceRequise;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la vérification d'ordonnance";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la vérification d'ordonnance", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    /**
     * Récupère toutes les ventes depuis l'API.
     * 
     * @return Liste des ventes
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static List<Vente> getVentes() throws Exception {
        String url = API_BASE_URL + "/ventes";

        LOGGER.log(Level.INFO, "Envoi d'une requête GET pour récupérer les ventes à {0}", url);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse reçue avec le code {0}", response.statusCode());

            if (response.statusCode() == 200) {
                List<Vente> ventes = parseVentesResponse(response.body());
                LOGGER.log(Level.INFO, "Récupération réussie de {0} ventes", ventes.size());
                return ventes;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la récupération des ventes";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    public static List<Medicament> searchForVente(String searchTerm) throws Exception {
    String url = API_BASE_URL + "/medicaments/search/all?searchTerm=" + URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .timeout(Duration.ofSeconds(15))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() == 200) {
        return parseVenteSearchResponse(response.body());
    } else {
        throw new Exception("Erreur HTTP " + response.statusCode());
    }
}

private static List<Medicament> parseVenteSearchResponse(String jsonResponse) {
    List<Medicament> medicaments = new ArrayList<>();
    try {
        JSONArray medicamentsArray = new JSONArray(jsonResponse);
        for (int i = 0; i < medicamentsArray.length(); i++) {
            JSONObject medJson = medicamentsArray.getJSONObject(i);
            Medicament m = new Medicament();
            m.setCodeCip13(medJson.getString("codeCip13"));
            m.setLibelle(medJson.optString("libelle", ""));
            m.setDenomination(medJson.optString("denomination", ""));
            String prixTTC = medJson.optString("prixTTC", "0.0");
            m.setPrixTTC(new BigDecimal(prixTTC));
            medicaments.add(m);
        }
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Erreur parsing réponse vente", e);
    }
    return medicaments;
}

    /**
     * Récupère une vente par son ID depuis l'API.
     * 
     * @param id ID de la vente
     * @return Vente correspondant à l'ID
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static Vente getVenteById(UUID id) throws Exception {
        String url = API_BASE_URL + "/ventes/" + id;

        LOGGER.log(Level.INFO, "Envoi d'une requête GET pour récupérer la vente {0}", id);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse reçue avec le code {0}", response.statusCode());

            if (response.statusCode() == 200) {
                Vente vente = parseVenteResponse(response.body());
                LOGGER.log(Level.INFO, "Récupération réussie de la vente {0}", id);
                return vente;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la récupération de la vente "
                        + id;
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    /**
     * Crée une nouvelle vente via l'API.
     * 
     * @param request Requête de création de vente
     * @return Vente créée
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static Vente createVente(VenteCreateRequest request) throws Exception {
        String url = API_BASE_URL + "/ventes";
        LOGGER.log(Level.INFO, "Envoi d'une requête POST pour créer une vente");

        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("pharmacienAdjointId", request.getPharmacienAdjointId());
            jsonRequest.put("clientId", request.getClientId());

            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedDate = isoFormat.format(request.getDateVente());
            jsonRequest.put("dateVente", formattedDate);
            jsonRequest.put("modePaiement", request.getModePaiement());
            jsonRequest.put("montantTotal", request.getMontantTotal());
            jsonRequest.put("montantRembourse", request.getMontantRembourse());
            jsonRequest.put("ordonnanceAjoutee", OrdonnaceValide);

            JSONArray medicamentsArray = new JSONArray();
            for (MedicamentPanier mp : request.getMedicaments()) {
                JSONObject medicamentJson = new JSONObject();
                medicamentJson.put("codeCip13", mp.getCodeCip13()); 
                medicamentJson.put("quantite", mp.getQuantite());
                medicamentsArray.put(medicamentJson);
            }
            jsonRequest.put("medicaments", medicamentsArray);

            System.out.println(" JSON envoyé à l'API :\n" + jsonRequest.toString(2));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(70))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            // 🔍 TEST ICI : Vérifier le statut et le contenu de la réponse
            System.out.println(" Code HTTP : " + response.statusCode());
            System.out.println(" Réponse brute : \n" + response.body());

            LOGGER.log(Level.INFO, "Réponse reçue avec le code {0}", response.statusCode());

            if (response.statusCode() == 201) {
                Vente vente = parseVenteResponse(response.body());
                LOGGER.log(Level.INFO, "Création réussie de la vente {0}", vente.getIdVente());
                return vente;
            } else {
                if (response.statusCode() == 428) {
                    String errorMessage= "Ordonnance requise pour finaliser la vente";
                    LOGGER.log(Level.INFO, "Ordonnance requise pour finaliser la vente");
                    throw new Exception(errorMessage + ": " + response.body());
                }else {
                    String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la création de la vente";
                    LOGGER.log(Level.SEVERE, errorMessage);
                    throw new Exception(errorMessage + ": " + response.body());
                }

            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }

    }

    /**
     * Supprime une vente via l'API.
     * 
     * @param id ID de la vente à supprimer
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static void deleteVente(UUID id) throws Exception {
        String url = API_BASE_URL + "/ventes/" + id;

        LOGGER.log(Level.INFO, "Envoi d'une requête DELETE pour supprimer la vente {0}", id);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse reçue avec le code {0}", response.statusCode());

            if (response.statusCode() != 204) {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la suppression de la vente "
                        + id;
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }

            LOGGER.log(Level.INFO, "Suppression réussie de la vente {0}", id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    /**
     * Parse la réponse JSON contenant une liste de médicaments.
     * 
     * @param jsonResponse Réponse JSON
     * @return Liste des médicaments
     */
    private static List<Medicament> parseMedicamentsResponse(String jsonResponse) {
        List<Medicament> medicaments = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray contentArray = jsonObject.getJSONArray("content");

            for (int i = 0; i < contentArray.length(); i++) {
                JSONObject medicamentJson = contentArray.getJSONObject(i);
                Medicament medicament = new Medicament();

                // Champs existants
                medicament.setId(medicamentJson.getLong("id"));
                medicament.setCodeCip13(medicamentJson.getString("codeCip13"));
                medicament.setLibelle(medicamentJson.getString("libelle"));
                medicament.setPrix(medicamentJson.getDouble("prix"));
                medicament.setQuantite(medicamentJson.getInt("quantite"));

                // Nouveau champ pour stockId
                if (medicamentJson.has("stockId")) {
                    medicament.setStockId(medicamentJson.getString("stockId"));
                }

                medicaments.add(medicament);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing JSON", e);
        }
        return medicaments;
    }

    /**
     * Vérifie si le backend est accessible.
     * 
     * @return true si le backend est accessible, false sinon
     */
    public static boolean isBackendAccessible() {
        try {
            String url = API_BASE_URL + "/medicaments/1";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Le backend n'est pas accessible", e);
            return false;
        }
    }

    /**
     * Parse la réponse JSON contenant une page de médicaments.
     * 
     * @param jsonResponse Réponse JSON
     * @return Réponse paginée contenant les médicaments
     */
    private static PageResponse<Medicament> parsePageResponse(String jsonResponse) {
        List<Medicament> medicaments = new ArrayList<>();
        int currentPage = 0;
        int totalPages = 0;
        long totalElements = 0;

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            LOGGER.log(Level.INFO, "Parsing JSON response: {0}", jsonResponse);

            JSONArray contentArray = jsonObject.getJSONArray("content");
            currentPage = jsonObject.getInt("currentPage");
            totalPages = jsonObject.getInt("totalPages");
            totalElements = jsonObject.getLong("totalElements");

            for (int i = 0; i < contentArray.length(); i++) {
                JSONObject medicamentJson = contentArray.getJSONObject(i);
                Medicament medicament = new Medicament();

                if (medicamentJson.has("id")) {
                    medicament.setId(medicamentJson.getLong("id"));
                    LOGGER.log(Level.INFO, "Parsed id: {0}", medicament.getId());
                } else {
                    LOGGER.log(Level.WARNING, "Champ 'id' manquant pour un médicament !");
                }

                // Parse and log each field
                if (medicamentJson.has("codeCIS")) {
                    medicament.setCodeCIS(medicamentJson.getString("codeCIS"));
                    LOGGER.log(Level.INFO, "Parsed codeCIS: {0}", medicament.getCodeCIS());
                } else {
                    LOGGER.log(Level.SEVERE, "Missing 'codeCIS' field in medicament JSON object");
                }

                if (medicamentJson.has("libelle")) {
                    medicament.setLibelle(medicamentJson.getString("libelle"));
                    LOGGER.log(Level.INFO, "Parsed libelle: {0}", medicament.getLibelle());
                }

                if (medicamentJson.has("denomination")) {
                    medicament.setDenomination(medicamentJson.getString("denomination"));
                    LOGGER.log(Level.INFO, "Parsed denomination: {0}", medicament.getDenomination());
                } else {
                    LOGGER.log(Level.SEVERE, "Missing 'denomination' field in medicament JSON object");
                }

                if (medicamentJson.has("dosage")) {
                    medicament.setDosage(medicamentJson.getString("dosage"));
                    LOGGER.log(Level.INFO, "Parsed dosage: {0}", medicament.getDosage());
                } else {
                    LOGGER.log(Level.SEVERE, "Missing 'dosage' field in medicament JSON object");
                }

                if (medicamentJson.has("reference")) {
                    medicament.setReference(medicamentJson.getString("reference"));
                    LOGGER.log(Level.INFO, "Parsed reference: {0}", medicament.getReference());
                } else {
                    LOGGER.log(Level.SEVERE, "Missing 'reference' field in medicament JSON object");
                }

                if (medicamentJson.has("surOrdonnance")) {
                    medicament.setSurOrdonnance(medicamentJson.getString("surOrdonnance"));
                    LOGGER.log(Level.INFO, "Parsed surOrdonnance: {0}", medicament.getSurOrdonnance());
                } else {
                    LOGGER.log(Level.SEVERE, "Missing 'surOrdonnance' field in medicament JSON object");
                }

                // Check if "prixTTC" is present and is a number
                if (medicamentJson.has("prixTTC") && !medicamentJson.isNull("prixTTC")) {
                    try {
                        Object prixTTC = medicamentJson.get("prixTTC");
                        if (prixTTC instanceof BigDecimal) {
                            medicament.setPrixTTC((BigDecimal) prixTTC);
                        } else {
                            medicament.setPrixTTC(new BigDecimal(prixTTC.toString()));
                        }
                        LOGGER.log(Level.INFO, "Parsed prixTTC: {0}", medicament.getPrixTTC());
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE, "Invalid 'prixTTC' value: " + medicamentJson.get("prixTTC"));
                    }
                }

                // Similarly, handle other fields like "prixHT" and "taxe"
                if (medicamentJson.has("prixHT") && !medicamentJson.isNull("prixHT")) {
                    try {
                        Object prixHT = medicamentJson.get("prixHT");
                        if (prixHT instanceof BigDecimal) {
                            medicament.setPrixHT((BigDecimal) prixHT);
                        } else {
                            medicament.setPrixHT(new BigDecimal(prixHT.toString()));
                        }
                        LOGGER.log(Level.INFO, "Parsed prixHT: {0}", medicament.getPrixHT());
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE, "Invalid 'prixHT' value: " + medicamentJson.get("prixHT"));
                    }
                }

                if (medicamentJson.has("taxe") && !medicamentJson.isNull("taxe")) {
                    try {
                        Object taxe = medicamentJson.get("taxe");
                        if (taxe instanceof BigDecimal) {
                            medicament.setTaxe((BigDecimal) taxe);
                        } else {
                            medicament.setTaxe(new BigDecimal(taxe.toString()));
                        }
                        LOGGER.log(Level.INFO, "Parsed taxe: {0}", medicament.getTaxe());
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE, "Invalid 'taxe' value: " + medicamentJson.get("taxe"));
                    }
                }

                if (medicamentJson.has("stock")) {
                    medicament.setStock(medicamentJson.getInt("stock"));
                    LOGGER.log(Level.INFO, "Parsed stock: {0}", medicament.getStock());
                }

                medicaments.add(medicament);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing de la réponse JSON paginée", e);
        }

        return new PageResponse<>(medicaments, currentPage, totalPages, totalElements);
    }

    /**
     * Parse la réponse JSON contenant une liste de ventes.
     * 
     * @param jsonResponse Réponse JSON
     * @return Liste des ventes
     */
    private static List<Vente> parseVentesResponse(String jsonResponse) {
        List<Vente> ventes = new ArrayList<>();

        try {
            JSONArray ventesArray = new JSONArray(jsonResponse);

            for (int i = 0; i < ventesArray.length(); i++) {
                JSONObject venteJson = ventesArray.getJSONObject(i);
                ventes.add(parseVenteJson(venteJson));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing de la réponse JSON des ventes", e);
        }

        return ventes;
    }

    /**
     * Parse la réponse JSON contenant une vente.
     * 
     * @param jsonResponse Réponse JSON
     * @return Vente
     */
    private static Vente parseVenteResponse(String jsonResponse) {
        try {
            JSONObject venteJson = new JSONObject(jsonResponse);
            return parseVenteJson(venteJson);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing de la réponse JSON d'une vente", e);
            return null;
        }
    }

    /**
     * Parse un objet JSON représentant une vente.
     * 
     * @param venteJson Objet JSON
     * @return Vente
     */
    private static Vente parseVenteJson(JSONObject venteJson) {
        Vente vente = new Vente();

        try {
            vente.setIdVente(UUID.fromString(venteJson.getString("idVente")));

            // Conversion de la date à partir d'une chaîne ISO
            String dateStr = venteJson.getString("dateVente");
            vente.setDateVente(Date.from(Instant.parse(dateStr)));

            vente.setModePaiement(venteJson.getString("modePaiement"));
            vente.setMontantTotal(venteJson.getDouble("montantTotal"));
            vente.setMontantRembourse(venteJson.getDouble("montantRembourse"));
            vente.setPharmacienAdjointId(UUID.fromString(venteJson.getString("pharmacienAdjointId")));
            vente.setClientId(UUID.fromString(venteJson.getString("clientId")));

            if (venteJson.has("notification")) {
                vente.setNotification(venteJson.getString("notification"));
            }

            List<Medicament> medicaments = new ArrayList<>();
            JSONArray medicamentsArray = venteJson.getJSONArray("medicamentIds");

            for (int j = 0; j < medicamentsArray.length(); j++) {
                JSONObject medicamentJson = medicamentsArray.getJSONObject(j);
                Medicament medicament = new Medicament();

                medicament.setId(medicamentJson.getLong("id"));
                medicament.setCodeCip13(medicamentJson.getString("codeCip13"));
                medicament.setLibelle(medicamentJson.getString("libelle"));
                medicament.setPrix(medicamentJson.getDouble("prix"));
                medicament.setQuantite(medicamentJson.getInt("quantite"));

                medicaments.add(medicament);
            }

            vente.setMedicaments(medicaments);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing d'un objet JSON vente", e);
        }

        return vente;
    }

    public static JSONObject getMedicamentInfosAdmin(String codeCip13) {
        // Implémentation: récupérer les infos admin d'un médicament
        // Logique pour récupérer les informations administratives d'un médicament
        return null; // À remplacer par votre logique
    }

    public static JSONObject getMedicamentInfosDispo(String codeCip13) {
        // Implémentation: récupérer les infos de disponibilité d'un médicament
        // Logique pour récupérer les informations de disponibilité d'un médicament
        return null; // À remplacer par votre logique
    }

    public static JSONObject getMedicamentInfosPrescription(String codeCip13) {
        // Implémentation: récupérer les infos de prescription d'un médicament
        // Logique pour récupérer les informations de prescription d'un médicament
        return null; // À remplacer par votre logique
    }

    public static JSONObject getMedicamentInfosGeneriques(String codeCip13) {
        // Implémentation: récupérer les infos des génériques d'un médicament
        // Logique pour récupérer les informations des génériques d'un médicament
        return null; // À remplacer par votre logique
    }

    public static boolean updateMedicamentStock(String codeCip13, Integer quantite) {
        // Implémentation: mettre à jour le stock d'un médicament
        // Logique pour mettre à jour le stock d'un médicament
        return false; // À remplacer par votre logique
    }

    // Méthodes pour récupérer les médecins avec pagination
    public static PageResponse<Medecin> getMedecinsPagines(int page) throws Exception {
        return getMedecinsPagines(page, ""); // Appel avec un terme de recherche vide
    }

    public static PageResponse<Medecin> getMedecinsPagines(int page, String searchTerm) throws Exception {
        String url = API_BASE_URL + "/medecins/page";
        if (searchTerm != null && !searchTerm.isEmpty()) {
            url += "?searchTerm=" + searchTerm; // Ajout du terme de recherche dans l'URL
        }

        LOGGER.log(Level.INFO, "Envoi d'une requête GET paginée à " + url);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse paginée reçue avec le code {0}", response.statusCode());

            if (response.statusCode() == 200) {
                PageResponse<Medecin> pageResponse = parsePageResponseMedecins(response.body());
                LOGGER.log(Level.INFO, "Récupération réussie de la page {0}/{1} avec {2} médecins",
                        new Object[]{pageResponse.getCurrentPage(), pageResponse.getTotalPages(),
                                pageResponse.getContent().size()});
                return pageResponse;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode()
                        + " lors de la récupération des médecins paginés";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API paginée", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    // Méthode pour récupérer tous les médecins
    public static List<Medecin> getMedecins(String s) throws Exception {
        return getMedecins(""); // Appel avec un terme de recherche vide
    }

    // Méthode pour créer un médecin
    public static MedecinResponse createMedecin(MedecinCreateRequest request) throws Exception {
        String url = API_BASE_URL + "/medecins";
        LOGGER.log(Level.INFO, "Envoi d'une requête POST pour créer un médecin");

        try {
            // Préparation du JSON de la requête
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("civilite", request.getCivilite());
            jsonRequest.put("nomExercice", request.getNomExercice());
            jsonRequest.put("prenomExercice", request.getPrenomExercice());
            jsonRequest.put("rppsMedecin", request.getRppsMedecin());
            jsonRequest.put("categorieProfessionnelle", request.getCategorieProfessionnelle());
            jsonRequest.put("profession", request.getProfession());
            jsonRequest.put("modeExercice", request.getModeExercice());
            jsonRequest.put("qualifications", request.getQualifications());
            jsonRequest.put("structureExercice", request.getStructureExercice());
            jsonRequest.put("fonctionActivite", request.getFonctionActivite());
            jsonRequest.put("genreActivite", request.getGenreActivite());

            // Envoi de la requête HTTP
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(70))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                    .build();

            // Récupération de la réponse de l'API
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            LOGGER.log(Level.INFO, "Réponse brute de l'API : " + response.body());
            LOGGER.log(Level.INFO, "Réponse reçue avec le code {0}", response.statusCode());

            // Vérification du code de statut HTTP
            if (response.statusCode() == 201) {
                // Si la création a réussi, on parse la réponse JSON en un MedecinResponse
                return parseMedecinResponse(response.body());
            } else {
                // Si la création échoue, on génère une exception avec le message d'erreur
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la création du médecin";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    // Méthode pour parser la réponse JSON contenant les médecins
    private static List<Medecin> parseMedecinsResponse(String jsonResponse) {
        List<Medecin> medecins = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray contentArray = jsonObject.getJSONArray("content");

            for (int i = 0; i < contentArray.length(); i++) {
                JSONObject medJson = contentArray.getJSONObject(i);
                Medecin medecin = new Medecin();
                medecin.setCivilite(medJson.getString("civilite"));
                medecin.setNomExercice(medJson.getString("nomExercice"));
                medecin.setPrenomExercice(medJson.getString("prenomExercice"));
                medecin.setRppsMedecin(medJson.getString("rppsMedecin"));
                medecin.setCategorieProfessionnelle(medJson.getString("categorieProfessionnelle"));
                medecin.setProfession(medJson.getString("profession"));
                medecin.setModeExercice(medJson.getString("modeExercice"));
                medecin.setQualifications(medJson.getString("qualifications"));
                medecin.setStructureExercice(medJson.getString("structureExercice"));
                medecin.setFonctionActivite(medJson.getString("fonctionActivite"));
                medecin.setGenreActivite(medJson.getString("genreActivite"));
                medecins.add(medecin);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing JSON des médecins", e);
        }
        return medecins;
    }

    // Méthode pour parser une page JSON des médecins
    private static PageResponse<Medecin> parsePageResponseMedecins(String jsonResponse) {
        List<Medecin> medecins = new ArrayList<>();
        int currentPage = 0;
        int totalPages = 0;
        long totalElements = 0;

        try {
            // La réponse reçue est une liste directe d'objets Medecin, donc on ne s'attend pas à un objet avec une clé "content"
            JSONArray contentArray = new JSONArray(jsonResponse);

            // En fonction de votre structure de réponse, il faudrait peut-être aussi prendre les informations de pagination
            // En ajoutant ces informations à votre réponse, par exemple :
            currentPage = 0;  // A vous de gérer la page courante si vous en avez besoin
            totalPages = 1;   // Idem, à ajuster selon votre API
            totalElements = contentArray.length();

            // Maintenant, on parse les médecins dans la réponse
            for (int i = 0; i < contentArray.length(); i++) {
                JSONObject medJson = contentArray.getJSONObject(i);
                Medecin medecin = new Medecin();
                medecin.setCivilite(medJson.getString("civilite"));
                medecin.setNomExercice(medJson.getString("nomExercice"));
                medecin.setPrenomExercice(medJson.getString("prenomExercice"));
                medecin.setRppsMedecin(medJson.getString("rppsMedecin"));
                medecin.setCategorieProfessionnelle(medJson.getString("categorieProfessionnelle"));
                medecin.setProfession(medJson.getString("profession"));
                medecin.setModeExercice(medJson.getString("modeExercice"));
                medecin.setQualifications(medJson.getString("qualifications"));
                medecin.setStructureExercice(medJson.getString("structureExercice"));
                medecin.setFonctionActivite(medJson.getString("fonctionActivite"));
                medecin.setGenreActivite(medJson.getString("genreActivite"));
                medecins.add(medecin);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing de la page JSON des médecins", e);
        }
        return new PageResponse<>(medecins, currentPage, totalPages, totalElements);
    }

    // Méthode pour analyser la réponse JSON d'un médecin et la convertir en objet MedecinResponse
    private static MedecinResponse parseMedecinResponse(String jsonResponse) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extraire les informations du médecin à partir de la réponse JSON
            String civilite = jsonObject.getString("civilite");
            String nomExercice = jsonObject.getString("nomExercice");
            String prenomExercice = jsonObject.getString("prenomExercice");
            String rppsMedecin = jsonObject.getString("rppsMedecin");
            String categorieProfessionnelle = jsonObject.getString("categorieProfessionnelle");
            String profession = jsonObject.getString("profession");
            String modeExercice = jsonObject.getString("modeExercice");
            String qualifications = jsonObject.getString("qualifications");
            String structureExercice = jsonObject.getString("structureExercice");
            String fonctionActivite = jsonObject.getString("fonctionActivite");
            String genreActivite = jsonObject.getString("genreActivite");

            // Créer et retourner l'objet MedecinResponse
            return new MedecinResponse(
                    null,  // L'id du médecin peut être assigné à partir de la réponse API si disponible
                    civilite,
                    nomExercice,
                    prenomExercice,
                    rppsMedecin,
                    categorieProfessionnelle,
                    profession,
                    modeExercice,
                    qualifications,
                    structureExercice,
                    fonctionActivite,
                    genreActivite
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du parsing de la réponse JSON du médecin", e);
            throw new Exception("Erreur lors de l'analyse de la réponse JSON du médecin: " + e.getMessage(), e);
        }
    }

    public static MedecinResponse checkMedecinByRpps(String rpps) throws Exception {
        // Créer l'URL de la requête pour vérifier l'existence du médecin par RPPS
        String url = API_BASE_URL + "/medecins/rpps/" + rpps;

        LOGGER.log(Level.INFO, "Envoi de la requête GET pour vérifier le médecin avec RPPS: " + rpps);

        try {
            // Création de la requête HTTP GET
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            // Envoi de la requête et récupération de la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse reçue avec le code " + response.statusCode());

            if (response.statusCode() == 200) {
                // Si le médecin existe, on retourne les informations du médecin
                return parseMedecinResponse(response.body());
            } else if (response.statusCode() == 404) {
                // Si le médecin n'existe pas, on retourne null
                return null;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la vérification du médecin.";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la communication avec l'API pour vérifier le médecin", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    /**
     * Supprime un médecin via l'API en utilisant son RPPS.
     *
     * @param rpps Le RPPS du médecin à supprimer
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static void deleteMedecin(String rpps) throws Exception {
        String url = API_BASE_URL + "/medecins/rpps/" + rpps;  // Utiliser le RPPS pour supprimer le médecin

        LOGGER.log(Level.INFO, "Envoi d'une requête DELETE pour supprimer le médecin avec RPPS: " + rpps);

        try {
            // Création de la requête HTTP DELETE
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .DELETE()
                    .build();

            // Envoi de la requête et récupération de la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.log(Level.INFO, "Réponse reçue avec le code " + response.statusCode());

            if (response.statusCode() == 204) {
                LOGGER.log(Level.INFO, "Suppression réussie du médecin avec RPPS: " + rpps);
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la suppression du médecin avec RPPS: " + rpps;
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception lors de la communication avec l'API pour supprimer le médecin", e);
            throw new Exception("Erreur de communication avec le serveur: " + e.getMessage(), e);
        }
    }

    public static List<Medecin> searchMedecins(String searchTerm) throws Exception {
        String url = API_BASE_URL + "/medecins/search?term=" + searchTerm;  // URL de l'API avec le terme de recherche

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()  // Utiliser GET pour récupérer les données
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Convertir la réponse JSON en une liste de Medecin
            return new ObjectMapper().readValue(response.body(), new TypeReference<List<Medecin>>() {});
        } else {
            throw new Exception("Erreur de recherche: " + response.statusCode());
        }
    }


    public static UUID createOrdonnance(OrdonnanceCreateRequest req) throws Exception {
        String url = API_BASE_URL + "/ordonnances";
        LOGGER.log(Level.INFO, "Envoi d'une requête POST pour créer une ordonnance");

        // 1) Construis le JSON de la requête
        JSONObject json = new JSONObject();
        // Date au format ISO
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = fmt.format(req.getDateEmission());
        json.put("dateEmission", dateStr);
        System.out.println("1 : " + dateStr);
        json.put("rppsMedecin", req.getRppsMedecin());
        System.out.println("2 : " + req.getRppsMedecin());
        json.put("clientId", req.getClientId().toString());
        System.out.println("3 : " + req.getClientId());


        // Tableau des prescriptions
        JSONArray prescArray = new JSONArray();
        for (PrescriptionCreateRequest p : req.getPrescriptions()) {
            JSONObject o = new JSONObject();
            o.put("medicament", p.getMedicament());
            o.put("quantitePrescrite", p.getQuantitePrescrite());
            o.put("duree", p.getDuree());
            o.put("posologie", p.getPosologie());
            prescArray.put(o);
        }
        json.put("prescriptions", prescArray);

        System.out.println("4 : " + prescArray);

        // 2) Prépare et envoie la requête HTTP
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response =
                client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        LOGGER.log(Level.INFO, "Réponse ordonnance création code {0}", response.statusCode());
        LOGGER.log(Level.FINE, "Corps de réponse : {0}", response.body());

        // 3) Traite la réponse
        if (response.statusCode() == 201) {
            // On s'attend à recevoir l'UUID en plain-text dans le body
            String body = response.body().replace("\"", "");
            validerOrdonnance(UUID.fromString(body));
            OrdonnaceValide = true;

            return UUID.fromString(body);
        } else {
            if (response.statusCode() == 409) {
                // Lire le message d’erreur renvoyé par le serveur, le logguer ou le remonter à l'utilisateur
                throw new RuntimeException("Conflit à la création d'ordonnance: " + response.body());
            } else {
                throw new RuntimeException(
                        "Échec création ordonnance: HTTP " + response.statusCode() + " / " + response.body());
            }
        }
    }
}
