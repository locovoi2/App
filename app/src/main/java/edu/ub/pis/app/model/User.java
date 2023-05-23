package edu.ub.pis.app.model;


import java.io.Serializable;

/**
 * Classe contenidor de la informació de l'usuari.
 */
public class User implements Serializable {
    private String mId;
    private String mName;
    private String mSurname;
    private boolean mTrainer;
    private String mDescription;
    private String mPrice;
    private int mUserCode;
    private String mContactPhoneNumber;
    private boolean mUserPremium;

    // Constructor
    public User(){

    }

    public User(
            String id,
            String name,
            String surname,
            boolean trainer,
            String description,
            String price,
            int userCode,
            String contactPhoneNumber,
            boolean userPremium
    ) {
        this.mId = id;
        this.mName = name;
        this.mSurname = surname;
        this.mTrainer = trainer;
        this.mDescription = description;
        this.mPrice = price;
        this.mUserCode = userCode;
        this.mContactPhoneNumber = contactPhoneNumber;
        this.mUserPremium = userPremium;
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
    public String getId() { return this.mId;}
    public String getDescription() {
        return this.mDescription;
    }
    public String getPrice() {
        return this.mPrice;
    }
    public int getUserCode() {
        return this.mUserCode;
    }
    public String getContactPhoneNumber() {
        return this.mContactPhoneNumber;
    }
    public boolean getPremium() {
        return this.mUserPremium;
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
    public void setUserPremium(boolean userPremium){ this.mUserPremium = userPremium;}
}
