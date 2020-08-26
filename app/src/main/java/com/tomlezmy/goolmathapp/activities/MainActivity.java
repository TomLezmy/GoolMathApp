package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.LearnFragment;
import com.tomlezmy.goolmathapp.fragments.LearnSelectFragment;
import com.tomlezmy.goolmathapp.fragments.MainMenuFragment;
import com.tomlezmy.goolmathapp.fragments.PracticeSelectFragment;
import com.tomlezmy.goolmathapp.fragments.ProgressFragmentScreen;
import com.tomlezmy.goolmathapp.fragments.SubCategoriesFragment;
import com.tomlezmy.goolmathapp.fragments.SubjectsFragment;
import com.tomlezmy.goolmathapp.interfaces.IFragmentChangeListener;

/**
 * This activity is the main menu where the user navigates through the app
 */
public class MainActivity extends AppCompatActivity implements IFragmentChangeListener, SubjectsFragment.OnSelectedSubCategoryListener, SubCategoriesFragment.OnSubCategoryListener {

    boolean isLearnSelectFragment = false, isMainMenuFragment = true, isLearnFragment = false;
    LearnSelectFragment learnSelectFragment;
    PracticeSelectFragment practiceSelectFragment;
    RelativeLayout titleLayout;
    ImageView menuBackground;

    /**
     * The method sets the current fragment to {@link MainMenuFragment}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // references
        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        ImageView about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
        titleLayout = findViewById(R.id.title_layout);
        menuBackground = findViewById(R.id.menu_background);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom).add(R.id.fragment_layout, new MainMenuFragment(), "MAIN_MENU_TAG").commit();

        // Redirect to a specific fragment
        if (getIntent().hasExtra("go_to")) {
            onChange(getIntent().getStringExtra("go_to"));
        }
    }

    /**
     * Called when moving between menu fragments
     * @param dest The new fragment to load
     * @param arguments Additional arguments for destination fragment
     */
    @Override
    public void onChange(String dest, int... arguments) {
        switch (dest) {
            case "PracticeSelect":
                isMainMenuFragment = false;
                isLearnSelectFragment = false;
                practiceSelectFragment = new PracticeSelectFragment();
                changeBackground(R.drawable.practice_background_design);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom).replace(R.id.fragment_layout, practiceSelectFragment, "PRACTICE_SELECT_TAG").addToBackStack(null).commit();
                break;
            case "LearnSelect":
                isMainMenuFragment = false;
                isLearnSelectFragment = true;
                changeBackground(R.drawable.learn_background_design);
                learnSelectFragment = new LearnSelectFragment();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom).replace(R.id.fragment_layout, learnSelectFragment, "LEARN_SELECT_TAG").addToBackStack(null).commit();
                break;
            case "Progress":
                isMainMenuFragment = false;
                titleLayout.startAnimation(AnimationUtils.makeOutAnimation(MainActivity.this, true));
                titleLayout.setVisibility(View.GONE);
                changeBackground(R.drawable.progress_background_design);
                final int currentBackStack = getSupportFragmentManager().getBackStackEntryCount();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom).replace(R.id.fragment_layout, new ProgressFragmentScreen(), "PROGRESS_TAG").addToBackStack(null).commit();
                getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (currentBackStack == getSupportFragmentManager().getBackStackEntryCount()) {
                            titleLayout.startAnimation(AnimationUtils.makeInAnimation(MainActivity.this, false));
                            titleLayout.setVisibility(View.VISIBLE);
                            getSupportFragmentManager().removeOnBackStackChangedListener(this);
                        }
                    }
                });
                break;
            // When next button is pressed inside Learn Fragment
            case "ReloadLearn":
                getSupportFragmentManager().popBackStackImmediate();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom, R.anim.slide_in_top, R.anim.slide_out_bottom).replace(R.id.fragment_layout, new LearnFragment(arguments[0], arguments[1]), "LEARN_TAG").addToBackStack(null).commit();
                break;
            // When back button is pressed inside Learn Fragment
            case "LearnBack":
                isLearnFragment = false;
                getSupportFragmentManager().popBackStackImmediate();
                break;
        }
    }

    /**
     * Called when a sub category is chosen in {@link SubCategoriesFragment}
     * @param subCategoryId The index of the sub category
     */
    @Override
    public void onSubCategory(int subCategoryId) {
        if (isLearnSelectFragment) {
            isLearnFragment = true;
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom).replace(R.id.fragment_layout, new LearnFragment(learnSelectFragment.getCategorySelected(), subCategoryId), "LEARN_TAG").addToBackStack(null).commit();
        }
        else {
            practiceSelectFragment.onSubCategory(subCategoryId);
        }
    }

    /**
     * Called when a sub category is chosen in {@link SubjectsFragment}
     * @param categoryId The index of the category
     */
    @Override
    public void onSelectedSubCategory(int categoryId) {
        if (isLearnSelectFragment) {
            learnSelectFragment.onSelectedSubCategory(categoryId);
        }
        else {
            practiceSelectFragment.onSelectedSubCategory(categoryId);
        }
    }

    /**
     * This method changes the main background image with a fading effect
     * @param backgroundResource The new resource
     */
    private void changeBackground(final int backgroundResource) {
        Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        menuBackground.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                menuBackground.setBackgroundResource(backgroundResource);
                Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                menuBackground.startAnimation(fadeIn);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isMainMenuFragment) {
            if (isLearnFragment) {
                isLearnFragment = false;
            }
            else {
                changeBackground(R.drawable.main_background_design);
                isMainMenuFragment = true;
            }
        }
        super.onBackPressed();
    }
}
