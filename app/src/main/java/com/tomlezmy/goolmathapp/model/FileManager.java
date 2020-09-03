package com.tomlezmy.goolmathapp.model;

import android.content.Context;

import com.tomlezmy.goolmathapp.game.CategoryProgressData;
import com.tomlezmy.goolmathapp.game.ECategory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * This class manages all file access for user data and level weights
 */
public class FileManager {
    /**
     * Singleton instance
     */
    private static FileManager instance;
    private Dictionary<ECategory,List<List<Integer>>> levelWeights;
    private UserData userData;
    private File levelWeightsFile;
    private File userDataFile;

    /**
     * Private constructor, called by {@link #getInstance} on first creation<br/>
     * The constructor creates pointers to the userData and levelWeights files if they exist. if they don't then new files are created
     * @param context The current context
     */
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

    /**
     * This method checks if a user data file exists
     * @param context The current context
     * @return True if user data file exists
     */
    public static boolean userFileExists(Context context) {
        return new File(context.getFilesDir().getAbsolutePath(),"user_data").exists();
    }

    /**
     * Singleton design pattern implementation
     * @param context The current context
     * @return The {@link FileManager} instance
     */
    public static FileManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileManager(context);
        }
        return instance;
    }

    /**
     * This method reads the data from the levelWeights file and stores it in a {@link Dictionary}
     * of type <{@link ECategory},{@link List}<{@link List}><{@link Integer}>>.<br/>
     * Each game category is a key and the value is a list of levels where each level is divided into a list of sub-levels that hold the weight of each sub level
     */
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

    /**
     * This method reads the data from the userData file and stores it in a {@link Dictionary}
     * of type <{@link UserData}
     */
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

    /**
     * This method overrides the levelWeights file with the content of {@link #levelWeights}
     */
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

    /**
     * This method overrides the user data file with the content of {@link #userData}
     */
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

    /**
     * This method updates a specific sub level weight and then writes it to the levelWeights file
     * @param category The current categroy
     * @param level The current level
     * @param subLevel The current sub level
     * @param newValue The new weight value
     */
    public void updateSubLevelWeight(ECategory category, int level, int subLevel, int newValue) {
        levelWeights.get(category).get(level).set(subLevel, newValue);
        writeToWeightsFile();
    }

    /**
     * @param category The required category
     * @param level The required level
     * @param subLevel The required sub level
     * @return The sub level weight
     */
    public int getSubLevelWeight(ECategory category, int level, int subLevel) {
        return levelWeights.get(category).get(level).get(subLevel);
    }

    public Dictionary<ECategory,List<List<Integer>>> getLevelWeights() {return  levelWeights;};

    public UserData getUserData() { return userData; }

    /**
     * This method creates a new user data file containing the user information and the level progress.<br/>
     * The method will open specific levels if the user is older than 10
     * @param firstName The user's name
     * @param birthYear The user's birth year
     */
    public void createNewUserDataFile(String firstName, int birthYear) {
        boolean userOverTen = (Calendar.getInstance().get(Calendar.YEAR) - birthYear > 10);
        Dictionary<ECategory, List<CategoryProgressData>> levelProgressData = new Hashtable<>();
        levelProgressData.put(ECategory.ADDITION,Arrays.asList(new CategoryProgressData(true), new CategoryProgressData(userOverTen)));
        levelProgressData.put(ECategory.SUBTRACTION,Arrays.asList(new CategoryProgressData(userOverTen), new CategoryProgressData(userOverTen), new CategoryProgressData(userOverTen)));
        levelProgressData.put(ECategory.MULTIPLICATION,Arrays.asList(new CategoryProgressData(userOverTen),new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false)));
        levelProgressData.put(ECategory.DIVISION,Arrays.asList(new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false),new CategoryProgressData(false)));
        levelProgressData.put(ECategory.FRACTIONS,Arrays.asList(new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false)));
        levelProgressData.put(ECategory.PERCENTS,Arrays.asList(new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false)));
        levelProgressData.put(ECategory.DECIMALS,Arrays.asList(new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false), new CategoryProgressData(false)));
        userData = new UserData(firstName,birthYear,levelProgressData);
        updateUserDataFile();
    }

    /**
     * This method creates a new weight file where all the sub levels have an equal weight of 1 for each category and level
     */
    public void createNewWeightsFile() {
        levelWeights = new Hashtable<>();
        levelWeights.put(ECategory.ADDITION,Arrays.asList(Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1)));
        levelWeights.put(ECategory.SUBTRACTION,Arrays.asList(Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1), Arrays.asList(1,1,1)));
        levelWeights.put(ECategory.MULTIPLICATION,Arrays.asList(Arrays.asList(1),Arrays.asList(1,1,1,1,1,1),Arrays.asList(1,1,1,1,1,1,1,1,1,1),Arrays.asList(1,1,1,1),Arrays.asList(1,1,1,1)));
        levelWeights.put(ECategory.DIVISION,Arrays.asList(Arrays.asList(1,1),Arrays.asList(1),Arrays.asList(1,1),Arrays.asList(1,1)));
        levelWeights.put(ECategory.FRACTIONS,Arrays.asList(Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1,1,1)));
        levelWeights.put(ECategory.PERCENTS,Arrays.asList(Arrays.asList(1), Arrays.asList(1), Arrays.asList(1,1), Arrays.asList(1,1,1,1,1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1), Arrays.asList(1,1,1), Arrays.asList(1,1)));
        levelWeights.put(ECategory.DECIMALS,Arrays.asList(Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1)));
        writeToWeightsFile();
    }
}
