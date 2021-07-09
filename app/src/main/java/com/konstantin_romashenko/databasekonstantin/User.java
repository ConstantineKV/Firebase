package com.konstantin_romashenko.databasekonstantin;

public class User
{
    public String id, name, second_name, email;

    private void User()
    {

    }

    public User(String id, String name, String second_name, String email) {
        this.id = id;
        this.name = name;
        this.second_name = second_name;
        this.email = email;
    }
}
