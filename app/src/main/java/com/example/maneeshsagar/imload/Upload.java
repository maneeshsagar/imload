package com.example.maneeshsagar.imload;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Upload{

    public String name;
    public String url;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String key;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url,String key) {
        this.name = name;
        this.url= url;
        this.key=key;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
