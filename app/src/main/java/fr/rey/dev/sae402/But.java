package fr.rey.dev.sae402;

public class But {

    private float left;
    private float top;
    private float right;
    private float bottom;
    private int couleur;
    private int idEquipe;

    public But(float left, float top, float right, float bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public But(float left, float top, float right, float bottom,int couleur){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.couleur = couleur;
    }

    public But(float left, float top, float right, float bottom,int couleur, int idEquipe){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.couleur = couleur;
        this.idEquipe = idEquipe;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public int getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }
}
