package com.konstantin_romashenko.databasekonstantin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity
{
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<User> listTemp;

    private DatabaseReference myDatabase;
    private String USER_KEY = "USER";
    ValueEventListener vListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);
        init();
        getDataFromDB();
        setOnClickItem();
    }

    private void init()
    {
        listView = findViewById(R.id.listViewRead);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData );
        listView.setAdapter(adapter);
        myDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    private void getDataFromDB()
    {
        vListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if(listData.size() > 0)
                    listData.clear();

                if(listTemp.size() > 0)
                    listTemp.clear();

                for(DataSnapshot ds : snapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    listData.add(user.name);
                    listTemp.add(user);
                }


                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error)
            {

            }

        };
        myDatabase.addValueEventListener(vListener);
    }

    private void  setOnClickItem()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                User user = listTemp.get(position);
                Intent i = new Intent(ReadActivity.this, ShowActivity.class);
                i.putExtra(Constants.USER_NAME, user.name);
                i.putExtra(Constants.USER_SEC_NAME, user.second_name);
                i.putExtra(Constants.USER_EMAIL, user.email);
                startActivity(i);
            }
        });
    }

}
