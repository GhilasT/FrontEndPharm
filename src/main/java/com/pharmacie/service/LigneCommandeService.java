package com.pharmacie.service;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.pharmacie.model.LigneCommande;
import com.pharmacie.model.Medicament;

/**
 * Service pour la gestion et l'analyse des objets {@link LigneCommande}.
 * Fournit des méthodes utilitaires pour parser les données JSON en objets LigneCommande.
 */
public class LigneCommandeService {
    

    /**
     * Analyse un objet JSON pour créer une instance de {@link LigneCommande}.
     *
     * @param obj L'objet {@link JSONObject} représentant une ligne de commande.
     * @return Une instance de {@link LigneCommande} si l'analyse réussit, sinon {@code null}.
     */
    public static LigneCommande parseLigneCommande(JSONObject obj) {
        if (obj == null) {
            System.out.println("JSONObject null, impossible de parser la ligne de commande");
            return null;
        }
                
        try {
            // Parsing de StockMedicamentDTO
            Medicament medicament = null;
            if (obj.has("stockMedicamentDTO") && !obj.isNull("stockMedicamentDTO")) {
                JSONObject stockMedicamentObj = obj.getJSONObject("stockMedicamentDTO");
                medicament = parseMedicament(stockMedicamentObj);
            }
            
            // Extraction de stockMedicamentId
            Long stockMedicamentId = null;
            if (obj.has("stockMedicamentId") && !obj.isNull("stockMedicamentId")) {
                stockMedicamentId = obj.getLong("stockMedicamentId");
            }
            
            // Parsing de la quantité
            int quantite = obj.optInt("quantite", 0);
            
            // Parsing du prix unitaire
            BigDecimal prixUnitaire = BigDecimal.ZERO;
            try {
                String prixStr = obj.optString("prixUnitaire");
                if (!prixStr.isEmpty()) {
                    prixUnitaire = new BigDecimal(prixStr);
                }
            } catch (NumberFormatException e) {
                System.err.println("Erreur de parsing du prix unitaire: " + e.getMessage());
            }
            
            // Création de la ligne de commande 
            LigneCommande ligne = new LigneCommande(stockMedicamentId, quantite, prixUnitaire, medicament);
            
            ligne.calculerMontantLigneAvantSauvegarde();
            
            return ligne;
        } catch (Exception e) {
            System.err.println("Erreur de parsing d'une ligne de commande: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    
    /**
     * Analyse un objet JSON représentant les données d'un médicament (StockMedicamentDTO)
     * pour créer une instance de {@link Medicament}.
     *
     * @param stockMedicamentDTO L'objet {@link JSONObject} contenant les informations du médicament.
     * @return Une instance de {@link Medicament} si l'analyse réussit, sinon {@code null}.
     */
    public static Medicament parseMedicament(JSONObject stockMedicamentDTO) {
        if (stockMedicamentDTO == null) {
            System.out.println("JSONObject null, impossible de parser le médicament");
            return null;
        }

        Medicament medicament = new Medicament();
        medicament.setId(stockMedicamentDTO.optLong("id"));
        medicament.setCodeCip13(stockMedicamentDTO.optString("codeCip13"));
        medicament.setCodeCIS(stockMedicamentDTO.optString("codeCIS"));
        medicament.setLibelle(stockMedicamentDTO.optString("libelle"));
        medicament.setDenomination(stockMedicamentDTO.optString("denomination"));
        medicament.setDosage(stockMedicamentDTO.optString("dosage"));
        medicament.setReference(stockMedicamentDTO.optString("reference"));
        medicament.setSurOrdonnance(stockMedicamentDTO.optString("surOrdonnance"));
        medicament.setStock(stockMedicamentDTO.optInt("stock"));
        medicament.setPrixHT(new BigDecimal(stockMedicamentDTO.optString("prixHT", "0")));
        medicament.setPrixTTC(new BigDecimal(stockMedicamentDTO.optString("prixTTC", "0")));
        medicament.setTaxe(new BigDecimal(stockMedicamentDTO.optString("taxe", "0")));
        medicament.setAgrementCollectivites(stockMedicamentDTO.optString("agrementCollectivites"));
        medicament.setTauxRemboursement(stockMedicamentDTO.optString("tauxRemboursement"));

        return medicament;
    }


}
