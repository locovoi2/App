package edu.ub.pis.app.model;

import java.io.Serializable;

public class UserMailFirebase implements Serializable{
    private String mail;
    public UserMailFirebase(String mail){
        this.mail = mail;
    }

    public UserMailFirebase(){
    }

    public String getMail() {
        return mail;
    }

    public void setMail( String mail) {
        this.mail = mail;
    }
}
