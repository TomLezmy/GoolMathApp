package com.tomlezmy.goolmathapp.interfaces;

/**
 * This interface is used to notify the need to switch fragments in {@link com.tomlezmy.goolmathapp.activities.MainActivity}
 */
public interface IFragmentChangeListener {
    /**
     * Called when user needs to navigate to a new fragment
     * @param dest The destination
     * @param arguments Additional arguments if needed
     */
    void onChange(String dest, int... arguments);
}
