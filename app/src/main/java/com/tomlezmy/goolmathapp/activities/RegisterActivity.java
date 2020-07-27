package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText firstNameEt = findViewById(R.id.first_name);
        final EditText lastNameEt = findViewById(R.id.last_name);
        final EditText birthYearEt = findViewById(R.id.birth_year);
        Button register = findViewById(R.id.btn_register);
        register.setOnTouchListener(new ButtonTouchAnimation());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEt.getText().toString();
                String lastName = lastNameEt.getText().toString();
                String birthYear = birthYearEt.getText().toString();
                if (firstName.isEmpty() || lastName.isEmpty() || birthYear.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(RegisterActivity.this, FirstDiagnosisActivity.class);
                    intent.putExtra("first_name", firstName);
                    intent.putExtra("last_name", lastName);
                    intent.putExtra("birth_year", Integer.parseInt(birthYear));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}