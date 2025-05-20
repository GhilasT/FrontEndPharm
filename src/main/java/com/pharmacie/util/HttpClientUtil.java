package com.pharmacie.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacie.model.Client;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utilitaire pour effectuer des requêtes HTTP, notamment pour interagir avec l'API backend.
 * Configure un {@link ObjectMapper} pour la sérialisation/désérialisation JSON,
 * y compris la gestion des mixins pour les classes parentes.
 */
public class HttpClientUtil {
    private static final String BASE_URL = Global.getBaseUrl();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // On enregistre la mixin pour la superclasse de Client (la classe Personne)
        objectMapper.addMixIn(Client.class.getSuperclass(), PersonneMixin.class);
        // Pour ne pas échouer sur les propriétés inconnues
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Recherche un client par son numéro de téléphone. Si le client n'existe pas, il est créé.
     *
     * @param client L'objet {@link Client} contenant au moins le numéro de téléphone pour la recherche ou la création.
     * @return Le client trouvé ou nouvellement créé.
     * @throws Exception Si une erreur survient lors de la communication avec l'API ou lors de la (dé)sérialisation.
     */
    public static Client findOrCreateClient(Client client) throws Exception {
        Client existingClient = getClientByTelephone(client.getTelephone());
        if (existingClient != null) {
            System.out.println("Client trouvé par téléphone: " + existingClient);
            return existingClient;
        } else {
            Client newClient = createClient(client);
            System.out.println("Client créé: " + newClient);
            return newClient;
        }
    }

    /**
     * Récupère un client par son numéro de téléphone en interrogeant l'API.
     *
     * @param telephone Le numéro de téléphone du client à rechercher.
     * @return L'objet {@link Client} correspondant au numéro de téléphone, ou null si non trouvé.
     * @throws Exception Si une erreur survient lors de la communication avec l'API ou lors de la désérialisation.
     */
    public static Client getClientByTelephone(String telephone) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/client/telephone/" + telephone))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Global.getToken())
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Réponse GET (getClientByTelephone): " + response.body());
        if (response.statusCode() == 200) {
            Client client = objectMapper.readValue(response.body(), Client.class);
            System.out.println("Client désérialisé: " + client);
            return client;
        } else if(response.statusCode() == 404) {
            System.out.println("Aucun client trouvé pour le téléphone: " + telephone);
            return null;
        } else {
            throw new RuntimeException("Erreur lors de la recherche du client : " + response.statusCode());
        }
    }

    /**
     * Crée un nouveau client en envoyant ses informations à l'API.
     *
     * @param client L'objet {@link Client} à créer.
     * @return Le client créé, tel que retourné par l'API.
     * @throws Exception Si une erreur survient lors de la communication avec l'API, de la sérialisation ou de la désérialisation.
     */
    public static Client createClient(Client client) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(client);
        System.out.println("JSON envoyé pour création: " + jsonBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/client"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Global.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Réponse JSON (createClient): " + response.body());
        if (response.statusCode() == 201 || response.statusCode() == 200) {
            Client clientCreated = objectMapper.readValue(response.body(), Client.class);
            System.out.println("Client désérialisé après création: " + clientCreated);
            return clientCreated;
        } else {
            throw new RuntimeException("Erreur lors de la création du client : " + response.statusCode());
        }
    }
}