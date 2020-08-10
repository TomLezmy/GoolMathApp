package com.tomlezmy.goolmathapp;

import android.content.Context;

import com.tomlezmy.goolmathapp.game.CategoryProgressData;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.LevelSubProbabilities;
import com.tomlezmy.goolmathapp.game.UserData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class FileManager {
    private static FileManager instance;
    private Dictionary<ECategory,List<List<Integer>>> levelWeights;
    private UserData userData;
    private File levelWeightsFile;
    private File userDataFile;

    private FileManager(Context context) {
        // Read levelProbabilityWeights from file or create new file if doesn't exist
        levelWeights = new Hashtable<>();
        levelWeightsFile = new File(context.getFilesDir().getAbsolutePath(),"level_weights");
        if (levelWeightsFile.exists()) {
            readFromWeightsFile();
        }
        else {
            try {
                levelWeightsFile.createNewFile();
                createNewWeightsFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userDataFile = new File(context.getFilesDir().getAbsolutePath(),"user_data");
        if (userDataFile.exists()) {
            readFromUserDataFile();
        }
    }

    public static boolean userFileExists(Context context) {
        return new File(context.getFilesDir().getAbsolutePath(),"user_data").exists();
    }

    public static FileManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileManager(context);
        }
        return instance;
    }

    private void readFromWeightsFile() {
        try {
            FileInputStream fis = new FileInputStream(levelWeightsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            levelWeights = (Dictionary<ECategory,List<List<Integer>>>)ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFromUserDataFile() {
        try {
            FileInputStream fis = new FileInputStream(userDataFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            userData = (UserData)ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeToWeightsFile() {
        try {
            FileOutputStream fos = new FileOutputStream(levelWeightsFile,false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(levelWeights);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUserDataFile() {
        try {
            FileOutputStream fos = new FileOutputStream(userDataFile,false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userData);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSubLevelWeight(ECategory category, int level, int subLevel, int newValue) {
        levelWeights.get(category).get(level).set(subLevel, newValue);
        writeToWeightsFile();
    }

    public int getSubLevelWeight(ECategory category, int level, int subLevel) {
        return levelWeights.get(category).get(level).get(subLevel);
    }

    public Dictionary<ECategory,List<List<Integer>>> getLevelWeights() {return  levelWeights;};

    public UserData getUserData() { return userData; }

    public void createNewUserDataFile(String firstName, int birthYear) {
        boolean userOverTen = (Calendar.getInstance().get(Calendar.YEAR) - birthYear > 10);
        Dictionary<ECategory, List<CategoryProgressData>> levelProgressData = new Hashtable<>();
        levelProgressData.put(ECategory.ADDITION,Arrays.asList(new CategoryProgressData(true), new CategoryProgressData(userOverTen)));
        levelProgressData.put(ECategory.SUBTRACTION,Arrays.asList(new CategoryProgressData(userOverTen), new CategoryProgressData(userOverTen), new CategoryProgressData(userOverTen)));
        levelProgressData.put(ECategory.MULTIPLICATION,Arrays.asList(new CategoryProgressData(userOverTen),new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false)));
        levelProgressData.put(ECategory.DIVISION,Arrays.asList(new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false)));
        levelProgressData.put(ECategory.FRACTIONS,Arrays.asList(new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false)));
        levelProgressData.put(ECategory.PERCENTS,Arrays.asList(new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false)));
        levelProgressData.put(ECategory.DECIMALS,Arrays.asList(new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false)));
        userData = new UserData(firstName,birthYear,levelProgressData);
        updateUserDataFile();
    }

    public void createNewWeightsFile() {
        levelWeights = new Hashtable<>();
        levelWeights.put(ECategory.ADDITION,Arrays.asList(Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1)));
        levelWeights.put(ECategory.SUBTRACTION,Arrays.asList(Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1), Arrays.asList(1,1,1)));
        levelWeights.put(ECategory.MULTIPLICATION,Arrays.asList(Arrays.asList(1,1,1),Arrays.asList(1,1,1,1,1,1),Arrays.asList(1,1,1,1,1,1,1,1,1,1),Arrays.asList(1,1,1,1),Arrays.asList(1,1,1,1)));
        levelWeights.put(ECategory.DIVISION,Arrays.asList(Arrays.asList(1,1),Arrays.asList(1,1,1),Arrays.asList(1),Arrays.asList(1,1),Arrays.asList(1,1)));
        levelWeights.put(ECategory.FRACTIONS,Arrays.asList(Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1)));
        levelWeights.put(ECategory.PERCENTS,Arrays.asList(Arrays.asList(1), Arrays.asList(1), Arrays.asList(1,1), Arrays.asList(1,1,1,1,1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1), Arrays.asList(1,1,1), Arrays.asList(1,1)));
        levelWeights.put(ECategory.DECIMALS,Arrays.asList(Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1)));
        writeToWeightsFile();
    }
}
