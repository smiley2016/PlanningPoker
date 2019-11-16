package com.example.admin.service;

public class FireBaseDataManager {
    private static FireBaseDataManager sInstance;

    public static FireBaseDataManager getInstance(){
        if(sInstance == null){
            sInstance = new FireBaseDataManager();
        }
        return sInstance;
    }


    public void loginUser(String email, String password) {
        
    }
}
