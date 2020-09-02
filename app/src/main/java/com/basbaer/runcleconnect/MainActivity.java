package com.basbaer.runcleconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.basbaer.runcleconnect.databinding.ActivityMainBinding;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding activityMainBinding;
    protected static ParseUser currentUser;
    protected static ArrayList<String> userFollows;
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter myAdapterForRecyclerView;
    private ArrayList<String> userIdArrayList;
    private UsersDb usersDb;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.main_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    //sets the options for the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:

                ParseUser.logOut();

                LogInActivity.updateSharedPreferences(false, null, null);

                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);

                startActivity(intent);

            case R.id.sendTweet:




        }

        return super.onOptionsItemSelected(item);
    }


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

        //setting up a SQLiteDatabase where the information are stored locally
        usersDb = new UsersDb(getApplicationContext());

        //setting up AL for the id's the user follows
        userFollows = new ArrayList<>();
        if(MainActivity.currentUser.get("follows") != null) {

            MainActivity.userFollows = (ArrayList<String>) MainActivity.currentUser.get("follows");

        }

        //setting up ArrayList for users
        userIdArrayList = new ArrayList<>();


        //get all Users
        ParseQuery<ParseUser> userParseQuery = ParseQuery.getQuery("_User");


        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userList, ParseException e) {

                if (e == null) {


                    if (userList.size() > 0) {

                        for (ParseUser user : userList) {

                            userIdArrayList.add(user.getObjectId());

                            //inserting Users in the Db
                            usersDb.insertUserIntoDb(user.getObjectId(), user.getUsername(), (boolean) user.get("following"));



                        }
                    }
                } else {
                    e.printStackTrace();
                }


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
        myAdapterForRecyclerView = new MyAdapter(userIdArrayList);
        usersRecyclerView.setAdapter(myAdapterForRecyclerView);








    }
}
