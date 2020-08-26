package com.tomlezmy.goolmathapp.interfaces;

/**
 * This interface is used to notify when an answer was selected in {@link com.tomlezmy.goolmathapp.fragments.ButtonsFragment}
 */
public interface IButtonFragmentAnswerListener {
    /**
     * Called when an answer was selected
     * @param answer The selected answer
     */
    void onReturn(float answer);
}