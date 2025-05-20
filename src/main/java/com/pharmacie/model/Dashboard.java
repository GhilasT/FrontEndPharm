package com.pharmacie.model;

/**
 * Modèle représentant les données agrégées pour le tableau de bord de l'interface utilisateur.
 * Contient des informations statistiques clés sur l'activité de la pharmacie.
 * @author raphaelcharoze
 */
public class Dashboard {

    private double CA;
    private double benefices;
    private int nbEmployes;
    private int nbClients;
    private int nbMedecins;
    private int nbMedicaments;
    private int nbMedicamentsRuptureStock;
    private int nbMedicamentsPerimes;
    private int nbMedicamentsAlerte;
    private int nbMedicamentsAlerteBientotPerimee;


    /**
     * Obtient le chiffre d'affaires.
     * @return Le chiffre d'affaires.
     */
    public double getCA() {
        return CA;
    }

    /**
     * Définit le chiffre d'affaires.
     * @param CA Le nouveau chiffre d'affaires.
     */
    public void setCA(double CA) {
        this.CA = CA;
    }

    /**
     * Obtient les bénéfices.
     * @return Les bénéfices.
     */
    public double getBenefices() {
        return benefices;
    }

    /**
     * Définit les bénéfices.
     * @param benefices Les nouveaux bénéfices.
     */
    public void setBenefices(double benefices) {
        this.benefices = benefices;
    }

    /**
     * Obtient le nombre d'employés.
     * @return Le nombre d'employés.
     */
    public int getNbEmployes() {
        return nbEmployes;
    }

    /**
     * Définit le nombre d'employés.
     * @param nbEmployes Le nouveau nombre d'employés.
     */
    public void setNbEmployes(int nbEmployes) {
        this.nbEmployes = nbEmployes;
    }

    /**
     * Obtient le nombre de clients.
     * @return Le nombre de clients.
     */
    public int getNbClients() {
        return nbClients;
    }

    /**
     * Définit le nombre de clients.
     * @param nbClients Le nouveau nombre de clients.
     */
    public void setNbClients(int nbClients) {
        this.nbClients = nbClients;
    }

    /**
     * Obtient le nombre de médecins.
     * @return Le nombre de médecins.
     */
    public int getNbMedecins() {
        return nbMedecins;
    }

    /**
     * Définit le nombre de médecins.
     * @param nbMedecins Le nouveau nombre de médecins.
     */
    public void setNbMedecins(int nbMedecins) {
        this.nbMedecins = nbMedecins;
    }

    /**
     * Obtient le nombre total de médicaments.
     * @return Le nombre de médicaments.
     */
    public int getNbMedicaments() {
        return nbMedicaments;
    }

    /**
     * Définit le nombre total de médicaments.
     * @param nbMedicaments Le nouveau nombre de médicaments.
     */
    public void setNbMedicaments(int nbMedicaments) {
        this.nbMedicaments = nbMedicaments;
    }

    /**
     * Obtient le nombre de médicaments en rupture de stock.
     * @return Le nombre de médicaments en rupture de stock.
     */
    public int getNbMedicamentsRuptureStock() {
        return nbMedicamentsRuptureStock;
    }

    /**
     * Définit le nombre de médicaments en rupture de stock.
     * @param nbMedicamentsRuptureStock Le nouveau nombre de médicaments en rupture de stock.
     */
    public void setNbMedicamentsRuptureStock(int nbMedicamentsRuptureStock) {
        this.nbMedicamentsRuptureStock = nbMedicamentsRuptureStock;
    }

    /**
     * Obtient le nombre de médicaments périmés.
     * @return Le nombre de médicaments périmés.
     */
    public int getNbMedicamentsPerimes() {
        return nbMedicamentsPerimes;
    }

    /**
     * Définit le nombre de médicaments périmés.
     * @param nbMedicamentsPerimes Le nouveau nombre de médicaments périmés.
     */
    public void setNbMedicamentsPerimes(int nbMedicamentsPerimes) {
        this.nbMedicamentsPerimes = nbMedicamentsPerimes;
    }

    /**
     * Obtient le nombre de médicaments en alerte (seuil bas atteint).
     * @return Le nombre de médicaments en alerte.
     */
    public int getNbMedicamentsAlerte() {
        return nbMedicamentsAlerte;
    }

    /**
     * Définit le nombre de médicaments en alerte.
     * @param nbMedicamentsAlerte Le nouveau nombre de médicaments en alerte.
     */
    public void setNbMedicamentsAlerte(int nbMedicamentsAlerte) {
        this.nbMedicamentsAlerte = nbMedicamentsAlerte;
    }

    /**
     * Obtient le nombre de médicaments dont la date de péremption est proche.
     * @return Le nombre de médicaments bientôt périmés.
     */
    public int getNbMedicamentsAlerteBientotPerimee() {
        return nbMedicamentsAlerteBientotPerimee;
    }

    /**
     * Définit le nombre de médicaments dont la date de péremption est proche.
     * @param nbMedicamentsAlerteBientotPerimee Le nouveau nombre de médicaments bientôt périmés.
     */
    public void setNbMedicamentsAlerteBientotPerimee(int nbMedicamentsAlerteBientotPerimee) {
        this.nbMedicamentsAlerteBientotPerimee = nbMedicamentsAlerteBientotPerimee;
    }


    /**
     * Constructeur par défaut pour Dashboard.
     */
    public Dashboard() {
    }

}
