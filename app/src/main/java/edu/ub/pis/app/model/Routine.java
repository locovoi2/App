package edu.ub.pis.app.model;

import java.util.ArrayList;

/**
 * Classe contenidora de la informacio d'una rutina
 */
public class Routine {
    private String name;
    private ArrayList<Exercise> exercises;
    private ArrayList<Boolean> days;
    //Constructors
    public Routine() {}

    public Routine(String name, ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
        this.days = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            this.days.add(false);
        }
    }

    public Routine(String name, ArrayList<Exercise> exercises, ArrayList<Boolean> days) {
        this.name = name;
        this.exercises = exercises;
        this.days = days;
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

    public ArrayList<Boolean> getDays() {
        return days;
    }

    public void addDay(int day) {
        this.days.set(day, true);
    }

    public void removeDay(int day) {
        this.days.set(day, false);
    }

    public void setDay(ArrayList<Boolean> day) {
        this.days = day;
    }

}
