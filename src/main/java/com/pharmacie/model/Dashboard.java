package com.pharmacie.model;

/**
 * Modèle représentant le tableau de bord dans le frontend.
 * Contient des informations statistiques sur la pharmacie.
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


    public double getCA() {
        return CA;
    }

    public void setCA(double CA) {
        this.CA = CA;
    }

    public double getBenefices() {
        return benefices;
    }

    public void setBenefices(double benefices) {
        this.benefices = benefices;
    }

    public int getNbEmployes() {
        return nbEmployes;
    }

    public void setNbEmployes(int nbEmployes) {
        this.nbEmployes = nbEmployes;
    }

    public int getNbClients() {
        return nbClients;
    }

    public void setNbClients(int nbClients) {
        this.nbClients = nbClients;
    }

    public int getNbMedecins() {
        return nbMedecins;
    }

    public void setNbMedecins(int nbMedecins) {
        this.nbMedecins = nbMedecins;
    }

    public int getNbMedicaments() {
        return nbMedicaments;
    }

    public void setNbMedicaments(int nbMedicaments) {
        this.nbMedicaments = nbMedicaments;
    }

    public int getNbMedicamentsRuptureStock() {
        return nbMedicamentsRuptureStock;
    }

    public void setNbMedicamentsRuptureStock(int nbMedicamentsRuptureStock) {
        this.nbMedicamentsRuptureStock = nbMedicamentsRuptureStock;
    }

    public int getNbMedicamentsPerimes() {
        return nbMedicamentsPerimes;
    }

    public void setNbMedicamentsPerimes(int nbMedicamentsPerimes) {
        this.nbMedicamentsPerimes = nbMedicamentsPerimes;
    }

    public int getNbMedicamentsAlerte() {
        return nbMedicamentsAlerte;
    }

    public void setNbMedicamentsAlerte(int nbMedicamentsAlerte) {
        this.nbMedicamentsAlerte = nbMedicamentsAlerte;
    }

    public int getNbMedicamentsAlerteBientotPerimee() {
        return nbMedicamentsAlerteBientotPerimee;
    }

    public void setNbMedicamentsAlerteBientotPerimee(int nbMedicamentsAlerteBientotPerimee) {
        this.nbMedicamentsAlerteBientotPerimee = nbMedicamentsAlerteBientotPerimee;
    }


    // Constructeur par défaut
    public Dashboard() {
    }

}
