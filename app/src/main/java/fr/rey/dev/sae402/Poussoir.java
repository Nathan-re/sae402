package fr.rey.dev.sae402;

import android.graphics.Color;

public class Poussoir {

    private float x;
    private float y;
    private int couleur;
    private int radius;
    private float puissPoussoir;

    public Poussoir(float x, float y, int couleur, int radius){
        this.x = x;
        this.y = y;
        this.couleur = couleur;
        this.radius = radius;
        puissPoussoir = 0;
    }

    public Poussoir(float x, float y, int couleur){
        this.x = x;
        this.y = y;
        this.couleur = couleur;
        puissPoussoir = 0;
    }

    public Poussoir(float x, float y){
        this.x = x;
        this.y = y;
        puissPoussoir = 0;
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

    public float getPuissPoussoir() {
        return puissPoussoir;
    }

    public void setPuissPoussoir(float puissPoussoir) {
        this.puissPoussoir = puissPoussoir;
    }
}
