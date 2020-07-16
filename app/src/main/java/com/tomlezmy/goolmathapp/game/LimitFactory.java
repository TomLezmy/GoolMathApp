package com.tomlezmy.goolmathapp.game;

public class LimitFactory {
    public static LevelValueLimits getLevelValues(ECategory category, int level) {
        LevelValueLimits levelValues = new LevelValueLimits();

        switch (category) {
            case SUBTRACTION:
            case ADDITION:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(0,9);
                        levelValues.setSecondNumberLimit(0,9);
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(10,99);
                        levelValues.setSecondNumberLimit(10,99);
                        break;
                    case 3:
                        if (category == ECategory.SUBTRACTION) {
                            levelValues.setFirstNumberLimit(new int[]{1000});
                            levelValues.setSecondNumberLimit(0,1000);
                        }
                        break;
                }
                break;
            case MULTIPLICATION:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(0,5);
                        levelValues.setSecondNumberLimit(0,5);
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(0,10);
                        levelValues.setSecondNumberLimit(0,10);
                        break;
                    case 3:
                        levelValues.setFirstNumberLimit(new int[]{11});
                        levelValues.setSecondNumberLimit(10,99);
                        break;
                    case 4:
                        levelValues.setFirstNumberLimit(new int[]{10,20,30,40,50,60,70,80,90});
                        levelValues.setSecondNumberLimit(0,9);
                        break;
                    case 5:
                        levelValues.setFirstNumberLimit(new int[]{100,200,300,400,500,600,700,800,900});
                        levelValues.setSecondNumberLimit(0,9);
                        break;
                }
                break;
            case DIVISION:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(0,5);
                        levelValues.setSecondNumberLimit(1,5);
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(0,10);
                        levelValues.setSecondNumberLimit(1,10);
                        break;
                    case 3:
                        int[] array = new int[10000];
                        int index = 0;
                        for (int i = 10; i <= 100000; i+=10) {
                            array[index++] = i;
                        }
                        levelValues.setFirstNumberLimit(array);
                        //levelValues.setFirstNumberLimit(new int[]{10,20,30,40,50,60,70,80,90});
                        levelValues.setSecondNumberLimit(new int[]{10});
                        break;
                    case 4:
                        levelValues.setFirstNumberLimit(new int[]{10,20,30,40,50,60,70,80,90});
                        levelValues.setSecondNumberLimit(1,9);
                        break;
                    case 5:
                        levelValues.setFirstNumberLimit(new int[]{100,200,300,400,500,600,700,800,900});
                        levelValues.setSecondNumberLimit(1,9);
                        break;
                }
                break;
            case PERCENTS:
                switch (level) {
                    case 1:
                        levelValues.setFirstNumberLimit(1,10000);
                        levelValues.setSecondNumberLimit(new int[] {1});
                        break;
                    case 2:
                        levelValues.setFirstNumberLimit(1,10000);
                        levelValues.setSecondNumberLimit(new int[] {10});
                        break;
                    case 3:
                        levelValues.setFirstNumberLimit(1,10000);
                        levelValues.setSecondNumberLimit(new int[] {50});
                        break;
                    case 4:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(new int[] {20,25,33/*need to be 33.333333*/});
                        break;
                    case 5:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(new int[] {2,5});
                        break;
                    case 6:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(1,100);
                        break;
                    case 7:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(new int[] {101,110,150});
                        break;
                    case 8:
                        levelValues.setFirstNumberLimit(1,1000);
                        levelValues.setSecondNumberLimit(new int[] {120,250});
                        break;
                }
                break;
            case FRACTIONS:
                switch (level) {
                    case 2:
                        levelValues.setFirstNumberLimit(new int[]{1});
                        levelValues.setSecondNumberLimit(2,10);
                        break;
                }
                break;
        }


        return levelValues;
    }
}
