package com.basbaer.runcleconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.basbaer.runcleconnect.databinding.ActivityMainBinding;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding activityMainBinding;
    ParseUser currentUser;
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter myAdapterForRecyclerView;
    private ArrayList<String> userArrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(R.layout.activity_main);

        //connecting with ParseServer
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        //getting current User
        currentUser = ParseUser.getCurrentUser();

        //setting up ArrayList for users
        userArrayList = new ArrayList<>();

        //get all Users
        ParseQuery<ParseUser> userParseQuery = ParseQuery.getQuery("User");


        userParseQuery.findInBackground(new FindCallback<ParseUser>(){
            @Override
            public void done(List<ParseUser> objects, ParseException e){

                if(e == null){

                    Log.i("findInBackground", "Retrieved " + objects.size() + " objects");

                    if(objects.size() > 0){

                        for(ParseObject object : objects){

                            Log.i("QueryResults", object.toString());

                            userArrayList.add(object.toString());


                        }
                    }
                }

                userArrayList.add("Basti");
                userArrayList.add("Bastian");
                myAdapterForRecyclerView.notifyDataSetChanged();

            }


        });



        usersRecyclerView = findViewById(R.id.usersRecyclerView);

        //if the view has a fixed size, add this for better performance
        //myRecyclerView.setHasFixedSize(true);

        //setting up the LinearLayoutManager -> there are more possible, but the
        //LinearLayoutManager comes closest to the ListView
        myLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(myLayoutManager);

        //setting up the Adapter
        myAdapterForRecyclerView = new MyAdapter(userArrayList);
        usersRecyclerView.setAdapter(myAdapterForRecyclerView);









    }
}
