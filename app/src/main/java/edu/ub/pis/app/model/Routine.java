package edu.ub.pis.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe contenidora de la informacio d'una rutina
 */
public class Routine {
    private String name;
    private ArrayList<Exercise> exercises;

    //Constructors
    public Routine() {}

    public Routine(String name, ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    //Getters i setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}