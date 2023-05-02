package edu.ub.pis.app.model;


/**
 * Classe contenidor de la informació de l'usuari.
 */
public class User {
    private String mId; // Per exemple, el mail
    private String mName;
    private String mSurname;
    private boolean mTrainer;

    // Constructor
    public User(
            String id,
            String name,
            String surname,
            boolean trainer
    ) {
        this.mId = id;
        this.mName = name;
        this.mSurname = surname;
        this.mTrainer = trainer;
    }

    // Getters
    public String getName () {
        return this.mName;
    }
    public String getSurname () {
        return this.mSurname;
    }
    public boolean getTrainer() {
        return this.mTrainer;
    }

    // Setters
    public void setName (String name) {
        this.mName = name;
    }
    public void setSurname (String surname) {
        this.mSurname = surname;
    }
    public void setTrainer(boolean trainer) {
        this.mTrainer = trainer;
    }
}
