package com.pharmacie.model;

import java.util.List;

/**
 * Classe représentant une réponse paginée du serveur.
 * Contient les données de la page ainsi que les métadonnées de pagination.
 */
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size; // Taille de la page (nombre d'éléments par page)
    
    // Constructeur par défaut
    public PageResponse() {
    }
    public PageResponse(List<T> content, int currentPage, int totalPages, long totalElements, int size) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
    }
    // Constructeur avec paramètres
    public PageResponse(List<T> content, int currentPage, int totalPages, long totalElements) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
    
    // Getters et setters
    public List<T> getContent() {
        return content;
    }
    
    public void setContent(List<T> content) {
        this.content = content;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
