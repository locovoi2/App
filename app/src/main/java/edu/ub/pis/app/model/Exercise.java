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
    private boolean completed;

    //Constructors
    public Exercise() {}

    public Exercise(String name, String series, String reps, String weight) {
        this.name = name;
        this.series = series;
        this.reps = reps;
        this.weight = weight;
        this.completed = false;
    }

    public Exercise(String name, String series, String reps, String weight, boolean completed) {
        this.name = name;
        this.series = series;
        this.reps = reps;
        this.weight = weight;
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

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
