package com.pharmacie.service;

import com.pharmacie.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service pour interagir avec l'API REST du backend.
 * Permet de récupérer les données des médicaments et des ventes depuis le backend.
 */
public class ApiRest {
    
    private static final String API_BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();
    private static final Logger LOGGER = Logger.getLogger(ApiRest.class.getName());
    
    // Taille de page par défaut retournée par le backend
    private static final int DEFAULT_PAGE_SIZE = 50;
    
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
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la récupération des médicaments";
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
     * @return Réponse paginée contenant les médicaments et les métadonnées de pagination
     * @throws Exception En cas d'erreur lors de la communication avec l'API
     */
    public static PageResponse<Medicament> getMedicamentsPagines(int page) throws Exception {
        return getMedicamentsPagines(page, null);
    }
    
    /**
     * Recherche des médicaments par terme de recherche avec pagination.
     * 
     * @param page Numéro de page (commence à 0)
     * @param searchTerm Terme de recherche (optionnel)
     * @return Réponse paginée contenant les médicaments et les métadonnées de pagination
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
                PageResponse<Medicament> pageResponse = parsePageResponse(response.body());
                LOGGER.log(Level.INFO, "Récupération réussie de la page {0}/{1} avec {2} médicaments", 
                        new Object[]{pageResponse.getCurrentPage(), pageResponse.getTotalPages(), pageResponse.getContent().size()});
                return pageResponse;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la récupération des médicaments paginés";
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
     * @param page Numéro de page (commence à 0)
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
                        String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la récupération des médicaments paginés";
                        LOGGER.log(Level.SEVERE, errorMessage);
                        throw new RuntimeException(errorMessage);
                    }
                    
                    PageResponse<Medicament> pageResponse = parsePageResponse(response.body());
                    LOGGER.log(Level.INFO, "Récupération asynchrone réussie de la page {0}/{1} avec {2} médicaments", 
                            new Object[]{pageResponse.getCurrentPage(), pageResponse.getTotalPages(), pageResponse.getContent().size()});
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
                        String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la récupération des médicaments";
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
                LOGGER.log(Level.INFO, "Médicament {0} nécessite ordonnance: {1}", new Object[]{medicamentId, ordonnanceRequise});
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
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la récupération de la vente " + id;
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
            // On fixe directement le JSON à envoyer
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("pharmacienAdjointId", "178ee63d-fcf4-4db1-a63c-bfdfa84bdd6e");
            jsonRequest.put("clientId", "567b0d52-108e-4ecf-a19a-4e60c50a85d5");
            jsonRequest.put("modePaiement", "Carte");
            jsonRequest.put("montantTotal", 50.0);
            jsonRequest.put("montantRembourse", 10.0);
    
            // Tableau de médicaments
            JSONArray medicamentsArray = new JSONArray();
    
            JSONObject medicamentJson = new JSONObject();
            JSONObject stockMedicamentJson = new JSONObject();
            stockMedicamentJson.put("id", "98042");
            medicamentJson.put("stockMedicament", stockMedicamentJson);
            medicamentJson.put("quantite", 2);
    
            medicamentsArray.put(medicamentJson);
            jsonRequest.put("medicaments", medicamentsArray);
    
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                    .build();
    
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            LOGGER.log(Level.INFO, "Réponse reçue avec le code {0}", response.statusCode());
            
            if (response.statusCode() == 201) {
                Vente vente = parseVenteResponse(response.body());
                LOGGER.log(Level.INFO, "Création réussie de la vente {0}", vente.getIdVente());
                return vente;
            } else {
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la création de la vente";
                LOGGER.log(Level.SEVERE, errorMessage);
                throw new Exception(errorMessage + ": " + response.body());
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
                String errorMessage = "Erreur HTTP " + response.statusCode() + " lors de la suppression de la vente " + id;
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
            vente.setDateVente(new Date(venteJson.getLong("dateVente")));
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

}
