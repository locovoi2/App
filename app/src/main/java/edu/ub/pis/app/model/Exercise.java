package edu.ub.pis.app.model;

import java.io.Serializable;

/**
 * Classe contenidor de la informacio d'un exercici
 */
public class Exercise implements Serializable {
    private String name;
    private String series;
    private String reps;
    private String weight;

    //Constructors
    public Exercise() {}

    public Exercise(String name, String series, String reps, String weight) {
        this.name = name;
        this.series = series;
        this.reps = reps;
        this.weight = weight;
    }

    //Getters i setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
