package edu.ub.pis.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe contenidora de la informacio d'una rutina
 */
public class Routine {
    private String name;
    private ArrayList<Exercise> exercises;
    private ArrayList<Boolean> day; // 0 - Dilluns, 1 - Dimarts, etc.
                     // -1 - Indefinit

    //Constructors
    public Routine() {}

    public Routine(String name, ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
        this.day = new ArrayList<>();
    }

    public Routine(String name, ArrayList<Exercise> exercises, ArrayList<Boolean> day) {
        this.name = name;
        this.exercises = exercises;
        this.day = day;
    }

    //Getters i setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public ArrayList<Boolean> getDay() {
        return day;
    }

    public void addDay(int day) {
        this.day.set(day, true);
    }

    public void removeDay(int day) {
        this.day.set(day, false);
    }

    public void setDay(ArrayList<Boolean> day) {
        this.day = day;
    }

}
