package fr.rey.dev.sae402;

import android.graphics.Color;

public class Poussoir {

    private float x;
    private float y;
    private int couleur;

    private int radius;

    public Poussoir(float x, float y, int couleur, int radius){
        this.x = x;
        this.y = y;
        this.couleur = couleur;
        this.radius = radius;
    }

    public Poussoir(float x, float y, int couleur){
        this.x = x;
        this.y = y;
        this.couleur = couleur;
    }

    public Poussoir(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
