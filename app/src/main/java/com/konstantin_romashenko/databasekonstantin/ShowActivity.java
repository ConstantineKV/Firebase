package com.konstantin_romashenko.databasekonstantin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity
{
    String name;
    String second_name;
    String email;
    TextView tvName;
    TextView tvSec_name;
    TextView tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        init();
        getIntentMain();
    }

    private void init()
    {
        tvName = findViewById(R.id.tvName);
        tvSec_name = findViewById(R.id.tvSecondName);
        tvEmail = findViewById(R.id.tvEmail);

    }

    private void getIntentMain() {
        Intent i = getIntent();

        if (i != null)
        {
            tvName.setText(i.getStringExtra(Constants.USER_NAME));
            tvSec_name.setText(i.getStringExtra(Constants.USER_SEC_NAME));
            tvEmail.setText(i.getStringExtra(Constants.USER_EMAIL));

        }
    }
}
