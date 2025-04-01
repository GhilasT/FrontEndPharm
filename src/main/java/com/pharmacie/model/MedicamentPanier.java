package com.pharmacie.model;

/**
 * Extension de la classe MedicamentPanier pour ajouter le nom du médicament
 * afin de l'afficher dans la liste du panier.
 */
public class MedicamentPanier {
    private Long medicamentId;
    private int quantite;
    private double prixUnitaire;
    private String nomMedicament;
    private String stockId;
    // Constructeur par défaut
    public MedicamentPanier() {
    }
    
    // Constructeur avec tous les paramètres
    public MedicamentPanier(Long medicamentId, int quantite, double prixUnitaire) {
        this.medicamentId = medicamentId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }
    
    // Getters et Setters
    public Long getMedicamentId() {
        return medicamentId;
    }
    
    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    public String getNomMedicament() {
        return nomMedicament;
    }
    
    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }
    
    // Méthode pour calculer le prix total
    public double getPrixTotal() {
        return quantite * prixUnitaire;
    }
    
    @Override
    public String toString() {
        return "MedicamentPanier{" +
                "medicamentId=" + medicamentId +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", nomMedicament='" + nomMedicament + '\'' +
                '}';
    }
}
