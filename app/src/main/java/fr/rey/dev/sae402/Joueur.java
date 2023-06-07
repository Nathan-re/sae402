package fr.rey.dev.sae402;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity // Création de la table (Joueur) avec @Entity
public class Joueur {

    // ATTRIBUTS
    @PrimaryKey(autoGenerate = true) @NonNull // Création de la clé primaire de la table (id) avec @Entity
    private int _id;
    private String playerPseudo;
    private String playerColor;
    private int playerNbDefaite;
    private int playerNbVictoire;

    // CONSTRUCTEUR
    public Joueur (String playerPseudo, String playerColor) {
        this.playerPseudo = playerPseudo;
        this.playerColor = playerColor;
        // On initialise le nombre de victoires et défaites à 0
        this.playerNbDefaite = 0;
        this.playerNbVictoire = 0;
    }

    // GETTERS
    public int get_id() {
        return _id;
    }
    public String getPlayerPseudo() {
        return playerPseudo;
    }
    public String getPlayerColor() {
        return playerColor;
    }
    public int getPlayerNbDefaite() {
        return playerNbDefaite;
    }
    public int getPlayerNbVictoire() {
        return playerNbVictoire;
    }

    // SETTERS
    public void setPlayerPseudo(String playerPseudo) {
        this.playerPseudo = playerPseudo;
    }
    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }
    public void setPlayerNbDefaite(int playerNbDefaite) {
        this.playerNbDefaite = playerNbDefaite;
    }
    public void setPlayerNbVictoire(int playerNbVictoire) {
        this.playerNbVictoire = playerNbVictoire;
    }
}
