package fr.rey.dev.sae402;

public class Rondelle {

    private float x;
    private float y;

    private String direction;

    private int radius;
    private float vitesse;

    private boolean available;

    private int compteurAvailable;

    public Rondelle(){
        x = 100;
        y = 100;
        radius = 50;
        vitesse = 1;
        available = true;
        compteurAvailable = 0;
    }

    public Rondelle(float x, float y){
        this.x = x;
        this.y = y;
        radius = 50;
        direction = "N";
        vitesse = 1;
        available = true;
        compteurAvailable = 0;
    }

    public Rondelle(float x, float y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        direction = "E";
        vitesse = 1;
        available = true;
        compteurAvailable = 0;
    }

    public Rondelle(float x, float y, int radius, String direction, float vitesse){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.direction = direction;
        this.vitesse = vitesse;
        available = true;
        compteurAvailable = 0;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public float getVitesse() {
        return vitesse;
    }

    public void setVitesse(float vitesse) {
        this.vitesse = vitesse;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCompteurAvailable() {
        return compteurAvailable;
    }

    public void setCompteurAvailable(int compteurAvailable) {
        this.compteurAvailable = compteurAvailable;
    }
}
