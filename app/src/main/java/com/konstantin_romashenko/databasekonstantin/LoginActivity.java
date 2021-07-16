package com.konstantin_romashenko.databasekonstantin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity
{
    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button bStart, bSignUp, bSignIn, bSignOut;
    private TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
    }

    private void init()
    {
        edLogin = findViewById(R.id.etEmail);
        edPassword = findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();
        bStart = findViewById(R.id.bStart);
        bSignUp = findViewById(R.id.bSignUp);
        bSignIn = findViewById(R.id.bSignIn);
        bSignOut = findViewById(R.id.bSignOut);
        tvUserName = findViewById(R.id.tvUserName);
    }

    public void onClickSignIn(View view)
    {
        if((!TextUtils.isEmpty(edLogin.getText().toString())) && (!TextUtils.isEmpty(edPassword.getText().toString())))
        {
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NotNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        Toast.makeText(getApplicationContext(), "User has been authorized", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        showNotSigned();
                        Toast.makeText(getApplicationContext(), "User has NOT been authorized", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignUp(View view)
    {
        if((!TextUtils.isEmpty(edLogin.getText().toString())) && (!TextUtils.isEmpty(edPassword.getText().toString())))
        {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NotNull Task<AuthResult> task)
                {

                    if(task.isSuccessful())
                    {
                        showSigned();

                    }
                    else
                    {
                        showNotSigned();
                        Toast.makeText(getApplicationContext(), "User has NOT been registered", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            showSigned();
            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            showNotSigned();
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUI(FirebaseUser currentUser)
    {
        if((!TextUtils.isEmpty(edLogin.getText().toString())) && (!TextUtils.isEmpty(edPassword.getText().toString())))
        {
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "User log on", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "User not log on", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public void onClickStart(View view)
    {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void onClickSignOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        bStart.setVisibility(View.GONE);
        bSignOut.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);

    }

    private void showSigned()
    {
        currentUser = mAuth.getCurrentUser();
        assert(currentUser != null);

        if(currentUser.isEmailVerified())
        {
            bStart.setVisibility(View.VISIBLE);
            bSignOut.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            edLogin.setVisibility(View.GONE);
            edPassword.setVisibility(View.GONE);
            bSignIn.setVisibility(View.GONE);
            bSignUp.setVisibility(View.GONE);
            if(currentUser != null)
            {
                String userName = "Вы вошли как: " + currentUser.getEmail();
                tvUserName.setText(userName);
            }
        }
        else
        {
            bStart.setVisibility(View.GONE);
            bSignOut.setVisibility(View.GONE);
            tvUserName.setVisibility(View.GONE);
            edLogin.setVisibility(View.VISIBLE);
            edPassword.setVisibility(View.VISIBLE);
            bSignIn.setVisibility(View.VISIBLE);
            bSignUp.setVisibility(View.VISIBLE);
            sendEmailVer();
            Toast.makeText(getApplicationContext(), "Проверьте вашу почту для подтверждения email адреса", Toast.LENGTH_SHORT).show();
        }

    }
    private void showNotSigned()
    {
        bStart.setVisibility(View.GONE);
        bSignOut.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);
    }

    private void sendEmailVer()
    {
        currentUser = mAuth.getCurrentUser();
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Проверьте вашу почту для подтверждения email адреса", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Ошибка отправки письма на почту", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
