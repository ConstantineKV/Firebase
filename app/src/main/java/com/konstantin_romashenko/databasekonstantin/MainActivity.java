package com.konstantin_romashenko.databasekonstantin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etSecondName, etEmail;
    private DatabaseReference myDatabase;
    private StorageReference mStorageRef;
    private ImageView ivImage;
    private String USER_KEY = "USER";
    private Uri uploadUri;
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
        Intent intent = new Intent(MainActivity.this, ReadActivity.class);
        startActivity(intent);
    }

    public void init()
    {
        etName = findViewById(R.id.etName);
        etSecondName = findViewById(R.id.etSecondName);
        etEmail = findViewById(R.id.etEmail);
        myDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        ivImage = findViewById(R.id.ivImage);
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB");

    }

    public void onClickСhooseImage(View view)
    {
        getImage();
    }

    public void getImage()
    {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null && data.getData() != null)
        {
            if(resultCode == RESULT_OK)
            {
                ivImage.setImageURI(data.getData());
                uploadImage();
                Log.d("MyLog", "Image URI: " + data.getData());
            }
        }
    }

    private void uploadImage()
    {
        ImageView imImage;
        Bitmap bitMap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "my_image");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NotNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                uploadUri = task.getResult();
            }
        });
    }
}