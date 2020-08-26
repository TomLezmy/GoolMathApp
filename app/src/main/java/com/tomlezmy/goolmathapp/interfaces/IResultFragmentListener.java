package com.tomlezmy.goolmathapp.interfaces;

/**
 * This interface is used to notify user actions in {@link com.tomlezmy.goolmathapp.fragments.GameFinishedFragment}
 */
public interface IResultFragmentListener {
    /**
     * Called when user pressed the return button
     */
    void onPressBack();

    /**
     * Called when user pressed the "Next Level" or "Start Again" buttons
     * @param moveToNextLevel True if user pressed "Next Level"
     */
    void onPressContinue(boolean moveToNextLevel);
}
