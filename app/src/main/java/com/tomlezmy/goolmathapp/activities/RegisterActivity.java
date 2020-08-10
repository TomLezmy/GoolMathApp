package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.NumberPickerFragmentDialog;

public class RegisterActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{
    TextView birthYearEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText firstNameEt = findViewById(R.id.first_name);
        birthYearEt = findViewById(R.id.birth_year);
        Button setBirthYear = findViewById(R.id.btn_set_birth_year);
        setBirthYear.setOnTouchListener(new ButtonTouchAnimation());
        setBirthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerFragmentDialog numberPickerFragmentDialog = new NumberPickerFragmentDialog();
                numberPickerFragmentDialog.setValueChangeListener(RegisterActivity.this);
                numberPickerFragmentDialog.show(getSupportFragmentManager(), "BIRTH_YEAR_DIALOG_TAG");
            }
        });
        Button register = findViewById(R.id.btn_register);
        register.setOnTouchListener(new ButtonTouchAnimation());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEt.getText().toString();
                String birthYear = birthYearEt.getText().toString();
                if (firstName.isEmpty() || birthYear.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.missing_fields), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(RegisterActivity.this, FirstDiagnosisActivity.class);
                    intent.putExtra("first_name", firstName);
                    intent.putExtra("birth_year", Integer.parseInt(birthYear));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        birthYearEt.setText(picker.getValue() + "");
    }
}