package com.example.maneeshsagar.imload;

public class User {
    private String Name;
    private String Email;
    private String Password;
    private String Key;


    public User(String name, String email, String password,String key) {
        Name = name;
        Email = email;
        Password = password;
        Key=key;

    }

    public User() {
    }


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        this.Key = key;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
