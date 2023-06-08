package fr.rey.dev.sae402;

public class Rondelle {

    private float x;
    private float y;

    private String direction;

    private int radius;

    public Rondelle(){
        x = 100;
        y = 100;
        radius = 50;
    }

    public Rondelle(float x, float y){
        this.x = x;
        this.y = y;
        radius = 50;
        direction = "N";
    }

    public Rondelle(float x, float y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        direction = "N";
    }

    public Rondelle(float x, float y, int radius, String direction){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.direction = direction;
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
}
