package com.pharmacie.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.text.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pharmacie.model.Commande;
import com.pharmacie.model.LigneCommande;
import com.pharmacie.model.Medicament;
import com.pharmacie.util.Global;

public class CommandeService {

    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();
    private static final String BASE_URL = Global.getBaseUrl();
    private static final String COMMANDES_URL = BASE_URL + "/commandes";
    

    public static List<Commande> getCommandes() throws Exception {
        List<Commande> resultats = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(COMMANDES_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Global.getToken())
                .GET()
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        
        if (response.statusCode() == 200) {
            JSONArray array = new JSONArray(response.body());
            
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                
                Commande commande = parseCommande(obj);
                if (commande != null) {
                    resultats.add(commande);
                } else {
                    System.out.println("La commande est null après parsing");
                }
            }
        } else {
            throw new RuntimeException("Erreur HTTP: " + response.statusCode());
        }
        
        return resultats;
    }



    
    public static Commande parseCommande(JSONObject obj) throws ParseException {
        if (obj == null) {
            System.out.println("JSONObject null, impossible de parser la commande");
            return null;
        }
        
        
        // Parsing de la date
        Date dateCommande = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
            String dateStr = obj.optString("dateCommande");
            if (!dateStr.isEmpty()) {
                dateCommande = formatter.parse(dateStr);
            }
        } catch (ParseException e) {
            System.err.println("Erreur de parsing de la date: " + e.getMessage());
            try {
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                String dateStr = obj.optString("dateCommande");
                if (!dateStr.isEmpty()) {
                    dateCommande = formatter2.parse(dateStr);
                }
            } catch (ParseException e2) {
                System.err.println("Échec du parsing de la date avec le format alternatif: " + e2.getMessage());
                try {
                    SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String dateStr = obj.optString("dateCommande");
                    if (!dateStr.isEmpty()) {
                        if (dateStr.contains(".")) {
                            dateStr = dateStr.substring(0, dateStr.indexOf("."));
                        }
                        dateCommande = formatter3.parse(dateStr);
                    }
                } catch (ParseException e3) {
                    System.err.println("Échec du parsing de la date avec le troisième format: " + e3.getMessage());
                }
            }
        }
        
        // Parsing du montant total
        BigDecimal montantTotal = BigDecimal.ZERO;
        try {
            String montantStr = obj.optString("montantTotal");
            if (!montantStr.isEmpty()) {
                montantTotal = new BigDecimal(montantStr);
            }
        } catch (NumberFormatException e) {
            System.err.println("Erreur de parsing du montant: " + e.getMessage());
            try {
                if (obj.has("montantTotal")) {
                    montantTotal = new BigDecimal(obj.get("montantTotal").toString());
                }
            } catch (Exception e2) {
                System.err.println("Échec du parsing du montant avec méthode alternative: " + e2.getMessage());
            }
        }
        
        // Parsing des UUID
        UUID reference = null;
        try {
            String refStr = obj.optString("reference");
            if (!refStr.isEmpty()) {
                reference = UUID.fromString(refStr);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("UUID de référence invalide: " + e.getMessage());
        }
        
        UUID fournisseurId = null;
        try {
            // Vérifier les deux noms possibles pour l'ID du fournisseur
            String fournisseurStr = obj.optString("idFournisseur");
            if (fournisseurStr.isEmpty()) {
                fournisseurStr = obj.optString("fournisseurId");
            }
            if (!fournisseurStr.isEmpty()) {
                fournisseurId = UUID.fromString(fournisseurStr);
            } else {
                System.out.println("ID fournisseur manquant");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("UUID de fournisseur invalide: " + e.getMessage());
        }
        
        UUID pharmacienAdjointId = null;
        try {
            String pharmacienStr = obj.optString("pharmacienAdjointId");
            if (!pharmacienStr.isEmpty()) {
                pharmacienAdjointId = UUID.fromString(pharmacienStr);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("UUID de pharmacien adjoint invalide: " + e.getMessage());
        }
        
        // Parsing des lignes de commande
        List<LigneCommande> ligneCommandes = new ArrayList<>();
        try {
            if (obj.has("ligneCommandes") && !obj.isNull("ligneCommandes")) {
                JSONArray lignesArray = obj.getJSONArray("ligneCommandes");
                
                for (int i = 0; i < lignesArray.length(); i++) {
                    JSONObject ligneObj = lignesArray.getJSONObject(i);
                    
                    LigneCommande ligne = LigneCommandeService.parseLigneCommande(ligneObj);
                    if (ligne != null) {
                        ligneCommandes.add(ligne);
                    }
                }
            } else {
                System.out.println("Pas de lignes de commande trouvées dans le JSON");
            }
        } catch (Exception e) {
            System.err.println("Erreur de parsing des lignes de commande: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Création et retour de l'objet Commande
        String statut = obj.optString("statut", "");
        
        try {
            Commande commande = new Commande(
                reference,
                dateCommande,
                montantTotal,
                statut,
                fournisseurId,
                pharmacienAdjointId,
                ligneCommandes
            );
            
            return commande;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'objet Commande: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static HttpResponse<String> mettreAJourStatutCommandeRecu(UUID idCommande) throws Exception {
        String url = COMMANDES_URL + "/StatusRecu/" + idCommande.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Global.getToken())
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        
        
        return response;
    }
    
}


