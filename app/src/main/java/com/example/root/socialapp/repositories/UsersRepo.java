package com.example.root.socialapp.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.example.root.socialapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersRepo {

    private static UsersRepo instance;
    private ArrayList<User> dataSet = new ArrayList<>();
    private DatabaseReference reference;
    private FirebaseAuth auth;

    public static UsersRepo getInstance(){
        if (instance == null) {
            instance = new UsersRepo();
        }
        return instance;
    }

    public MutableLiveData<List<User>> getUsers(int num_items){
        getAllUsers(num_items);
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void getAllUsers(int num_items) {
        auth = FirebaseAuth.getInstance();
        final String id = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        final List<User> list = new ArrayList<>();
        Query query = reference.limitToLast(num_items);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    list.clear();
                    dataSet.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        list.add(user);
                    }
                    Collections.reverse(list);
                    dataSet.addAll(list);
                }else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}