package edu.ub.pis.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe contenidor de la informacio d'un exercici
 */
public class Exercise implements Serializable {
    private String name;
    private String series;
    private String reps;
    private String weight;
    private String pr;
    private ArrayList<Boolean> completed;

    //Constructors
    public Exercise() {}

    public Exercise(String name, String series, String reps, String weight, String pr) {
        this.name = name;
        this.series = series;
        this.reps = reps;
        this.weight = weight;
        this.pr = pr;
        this.completed = new ArrayList<Boolean>();
        for(int i = 0; i < 7; i++) {
            completed.add(false);
        }
    }

    public Exercise(String name, String series, String reps, String weight, String pr, ArrayList<Boolean> completed) {
        this.name = name;
        this.series = series;
        this.reps = reps;
        this.weight = weight;
        this.pr = pr;
        this.completed = completed;
    }

    //Getters i setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getReps() {
        return this.reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWeight() {
        return this.weight;
    }
    public String getPr() {
        return this.pr;
    }


    public void setWeight(String weight) {
        this.weight = weight;
    }
    public void setPr(String pr) {
        this.pr = pr;
    }

    public ArrayList<Boolean> getCompleted() {
        return completed;
    }

    public void setCompleted(ArrayList<Boolean> completed) {
        this.completed = completed;
    }
}
