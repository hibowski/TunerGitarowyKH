package com.example.tunergitarowy;

import java.util.ArrayList;

public class Profile {
    private int id;
    private String name;
    private ArrayList<Integer> tones;

    public Profile(int id, String name) {
        this.id = id;
        this.name = name;
        this.tones = new ArrayList<Integer>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getTones() {
        return tones;
    }

    public void setTones(ArrayList<Integer> tones) {
        this.tones = tones;
    }

    public void addTone(int tone){
        this.tones.add(tone);
    }

    public void changeTone(int tone, int newValue) {
        this.tones.set(tone, newValue);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}