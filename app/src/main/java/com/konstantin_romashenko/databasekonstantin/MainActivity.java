package com.konstantin_romashenko.databasekonstantin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etSecondName, etEmail;
    private DatabaseReference myDatabase;
    private String USER_KEY = "USER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void onClickSave(View view)
    {
        String id = myDatabase.getKey();
        String name = etName.getText().toString();
        String second_name = etSecondName.getText().toString();
        String email = etEmail.getText().toString();
        User newUser = new User(id, name, second_name, email);
        if((!TextUtils.isEmpty(id))&&(!TextUtils.isEmpty(name))&&(!TextUtils.isEmpty(second_name))&&(!TextUtils.isEmpty(email)))
        {
            myDatabase.push().setValue(newUser);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();


    }

    public void onClickRead(View view)
    {
    }

    public void init()
    {
        etName = findViewById(R.id.etName);
        etSecondName = findViewById(R.id.etSecondName);
        etEmail = findViewById(R.id.etEmail);
        myDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);

    }
}