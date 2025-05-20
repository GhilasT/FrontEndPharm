package com.pharmacie.model;

import java.util.List;

/**
 * Classe représentant une réponse paginée du serveur.
 * Contient les données de la page ainsi que les métadonnées de pagination.
 * @param <T> Le type des éléments contenus dans la page.
 */
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size; // Taille de la page (nombre d'éléments par page)
    
    /**
     * Constructeur par défaut.
     */
    public PageResponse() {
    }

    /**
     * Constructeur avec tous les paramètres, y compris la taille de la page.
     * @param content La liste des éléments de la page actuelle.
     * @param currentPage Le numéro de la page actuelle.
     * @param totalPages Le nombre total de pages.
     * @param totalElements Le nombre total d'éléments sur toutes les pages.
     * @param size La taille de la page (nombre d'éléments par page).
     */
    public PageResponse(List<T> content, int currentPage, int totalPages, long totalElements, int size) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
    }

    /**
     * Constructeur avec paramètres, sans la taille explicite de la page.
     * @param content La liste des éléments de la page actuelle.
     * @param currentPage Le numéro de la page actuelle.
     * @param totalPages Le nombre total de pages.
     * @param totalElements Le nombre total d'éléments sur toutes les pages.
     */
    public PageResponse(List<T> content, int currentPage, int totalPages, long totalElements) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
    
    /**
     * Obtient le contenu de la page actuelle.
     * @return La liste des éléments de la page.
     */
    public List<T> getContent() {
        return content;
    }
    
    /**
     * Définit le contenu de la page actuelle.
     * @param content La nouvelle liste des éléments de la page.
     */
    public void setContent(List<T> content) {
        this.content = content;
    }
    
    /**
     * Obtient le numéro de la page actuelle.
     * @return Le numéro de la page actuelle.
     */
    public int getCurrentPage() {
        return currentPage;
    }
    
    /**
     * Définit le numéro de la page actuelle.
     * @param currentPage Le nouveau numéro de la page actuelle.
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    /**
     * Obtient le nombre total de pages.
     * @return Le nombre total de pages.
     */
    public int getTotalPages() {
        return totalPages;
    }
    
    /**
     * Définit le nombre total de pages.
     * @param totalPages Le nouveau nombre total de pages.
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    /**
     * Obtient le nombre total d'éléments sur toutes les pages.
     * @return Le nombre total d'éléments.
     */
    public long getTotalElements() {
        return totalElements;
    }
    
    /**
     * Définit le nombre total d'éléments sur toutes les pages.
     * @param totalElements Le nouveau nombre total d'éléments.
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Obtient la taille de la page (nombre d'éléments par page).
     * @return La taille de la page.
     */
    public int getSize() {
        return size;
    }

    /**
     * Définit la taille de la page (nombre d'éléments par page).
     * @param size La nouvelle taille de la page.
     */
    public void setSize(int size) {
        this.size = size;
    }
}
