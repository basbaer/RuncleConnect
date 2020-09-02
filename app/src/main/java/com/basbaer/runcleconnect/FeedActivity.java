package com.basbaer.runcleconnect;

import android.os.Bundle;

import com.basbaer.runcleconnect.databinding.ActivityFeedBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private ActivityFeedBinding activityFeedBinding;

    private ArrayList<String> tweetsOfFeedAL;
    private ArrayList<String> idOfTweetsAL;
    private ArrayList<String> usernamesOfTweetsAL;

    //RecyclerView
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter myAdapterForRecyclerView;

    private void setUpRecyclerView(ArrayList<String> arrayListToBeDisplayed, ArrayList<String> arrayListOfUsernames) {


        myRecyclerView = activityFeedBinding.feedRecyclerView;

        //setting up the LinearLayoutManager -> there are more possible, but the
        //LinearLayoutManager comes closest to the ListView
        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        //setting up the Adapter
        myAdapterForRecyclerView = new MyFeedAdapter(arrayListToBeDisplayed, arrayListOfUsernames);
        myRecyclerView.setAdapter(myAdapterForRecyclerView);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFeedBinding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = activityFeedBinding.getRoot();
        setContentView(view);

        //create a ArrayList with all tweets of the users the current user follows
        if (tweetsOfFeedAL == null) {
            tweetsOfFeedAL = new ArrayList<String>();
        }

        //ids
        if(idOfTweetsAL == null){
            idOfTweetsAL = new ArrayList<>();
        }

        //usernames
        if(usernamesOfTweetsAL == null){
            usernamesOfTweetsAL =  new ArrayList<>();
        }


        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Tweets");

        parseQuery.orderByAscending("updatedAt");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject foundTweets : objects) {

                        for (String idFollows : MainActivity.userFollows) {

                            if (foundTweets.get("userId").equals(idFollows)) {

                                tweetsOfFeedAL.add(foundTweets.get("message").toString());

                                idOfTweetsAL.add(idFollows);


                            }


                        }

                    }


                    //get the usernames
                    ParseQuery<ParseUser> usernamesQuery = ParseQuery.getQuery("_User");

                    usernamesQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                            if(e == null){

                                for(String id : idOfTweetsAL){

                                    Log.i("ids", idOfTweetsAL.toString());

                                    for(ParseUser foundUser : objects){

                                        if(id.equals(foundUser.getObjectId())){

                                            usernamesOfTweetsAL.add(foundUser.getUsername());

                                            Log.i("usernames", usernamesOfTweetsAL.toString());

                                            continue;

                                        }

                                    }

                                }

                                setUpRecyclerView(tweetsOfFeedAL, usernamesOfTweetsAL);



                            }

                        }
                    });




                }else{

                    Toast.makeText(FeedActivity.this, "Couldn't find any tweets", Toast.LENGTH_SHORT).show();

                }



            }
        });


    }

}
