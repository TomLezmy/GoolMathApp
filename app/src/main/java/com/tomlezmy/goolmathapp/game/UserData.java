package com.tomlezmy.goolmathapp.game;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.List;

public class UserData implements Serializable {
    private String firstName;
    private String lastName;
    private int birthYear;
    private Dictionary<ECategory, List<CategoryProgressData>> levelsProgressData;

    public UserData(String firstName, String lastName, int birthYear, Dictionary<ECategory, List<CategoryProgressData>> levelsProgressData) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.levelsProgressData = levelsProgressData;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public Dictionary<ECategory, List<CategoryProgressData>> getLevelsProgressData() {
        return levelsProgressData;
    }

    public void setLevelsProgressData(Dictionary<ECategory, List<CategoryProgressData>> levelsProgressData) {
        this.levelsProgressData = levelsProgressData;
    }
}
