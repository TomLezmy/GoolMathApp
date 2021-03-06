package com.tomlezmy.goolmathapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.tomlezmy.goolmathapp.R;

import java.util.Calendar;

/**
 * This fragment displays a {@link NumberPicker} to pick a birth year
 */
public class NumberPickerFragmentDialog extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final NumberPicker numberPicker = new NumberPicker(getActivity());

        // Users up to age 50
        numberPicker.setMinValue(Calendar.getInstance().get(Calendar.YEAR) - 50);
        numberPicker.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView tvSetBirthDay = new TextView(getContext());
        tvSetBirthDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainHeaderPurple));
        tvSetBirthDay.setPadding(20,20,20,20);
        tvSetBirthDay.setText(R.string.set_birth_year);
        tvSetBirthDay.setTypeface(ResourcesCompat.getFont(getContext(), R.font.rubik_black));
        builder.setCustomTitle(tvSetBirthDay);
        
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
