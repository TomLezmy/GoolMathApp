package com.tomlezmy.goolmathapp;

import android.content.Context;

import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.LevelSubProbabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class FileManager {
    private static FileManager instance;
    private Dictionary<ECategory,List<List<Integer>>> levelWeights;
    private File levelWeightsFile;

    private FileManager(Context context) {
        // Read levelProbabilityWeights from file or create new file if doesn't exist
        levelWeights = new Hashtable<>();
        levelWeightsFile = new File(context.getFilesDir().getAbsolutePath(),"level_weights");
        if (levelWeightsFile.exists()) {
            readFile();
        }
        else {
            try {
                levelWeightsFile.createNewFile();
                fillNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static FileManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileManager(context);
        }
        return instance;
    }

    private void readFile() {
        try {
            FileInputStream fis = new FileInputStream(levelWeightsFile);
            ObjectInputStream ois =new ObjectInputStream(fis);
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

    private void writeToFile() {
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

    public void updateSubLevelWeight(ECategory category, int level, int subLevel, int newValue) {
        levelWeights.get(category).get(level).set(subLevel, newValue);
        writeToFile();
    }

    public int getSubLevelWeight(ECategory category, int level, int subLevel) {
        return levelWeights.get(category).get(level).get(subLevel);
    }

    public Dictionary<ECategory,List<List<Integer>>> getLevelWeights() {return  levelWeights;};

    private void fillNewFile() {
        levelWeights = new Hashtable<>();
        levelWeights.put(ECategory.ADDITION,Arrays.asList(Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1)));
        levelWeights.put(ECategory.SUBTRACTION,Arrays.asList(Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1), Arrays.asList(1,1,1)));
        levelWeights.put(ECategory.MULTIPLICATION,Arrays.asList(Arrays.asList(1,1,1),Arrays.asList(1,1,1,1,1,1),Arrays.asList(1,1,1,1,1,1,1,1,1,1),Arrays.asList(1,1,1,1),Arrays.asList(1,1,1,1)));
        levelWeights.put(ECategory.DIVISION,Arrays.asList(Arrays.asList(1,1),Arrays.asList(1,1,1),Arrays.asList(1),Arrays.asList(1,1),Arrays.asList(1,1)));
        levelWeights.put(ECategory.FRACTIONS,Arrays.asList(Arrays.asList(1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1), Arrays.asList(1,1,1,1,1,1,1,1,1,1)));
        levelWeights.put(ECategory.PERCENTS,Arrays.asList(Arrays.asList(1), Arrays.asList(1), Arrays.asList(1,1), Arrays.asList(1,1,1,1,1,1), Arrays.asList(1,1,1,1), Arrays.asList(1,1), Arrays.asList(1,1,1), Arrays.asList(1,1)));
        levelWeights.put(ECategory.DECIMALS,Arrays.asList(Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1), Arrays.asList(1,1)));
        writeToFile();
    }
}
