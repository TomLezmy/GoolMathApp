package com.tomlezmy.goolmathapp.model;

import com.tomlezmy.goolmathapp.game.CategoryProgressData;
import com.tomlezmy.goolmathapp.game.ECategory;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.List;

/**
 * This class holds the data of the user and their game progress
 */
public class UserData implements Serializable {
    private String firstName;
    private int birthYear;
    private Dictionary<ECategory, List<CategoryProgressData>> levelsProgressData;

    /**
     * Class constructor
     * @param firstName The user's name
     * @param birthYear The user's birth year
     * @param levelsProgressData The user's game progress data
     */
    public UserData(String firstName, int birthYear, Dictionary<ECategory, List<CategoryProgressData>> levelsProgressData) {
        this.firstName = firstName;
        this.birthYear = birthYear;
        this.levelsProgressData = levelsProgressData;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
