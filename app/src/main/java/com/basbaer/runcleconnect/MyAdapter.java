package com.basbaer.runcleconnect;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    //here are the items/views handled, which every entry of the RecyclerView contains
    //this example only contains a TextView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        //in this constructor, the views are referenced
        public MyViewHolder(View itemView) {
            super(itemView);

            this.textView = itemView.findViewById(R.id.textViewForRecyclerView);
            this.imageView = itemView.findViewById(R.id.hook);
        }
    }


    private ArrayList<String> arrayList;


    public MyAdapter(ArrayList<String> incomingArrayList) {
        arrayList = incomingArrayList;
    }


    //Sets up, how the Views in the ViewHolder have to look like and has to return the ViewHolder
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Option 1: Set up given 'look' from Android
        //View viewThatGetsDisplayed = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //Option 2: Set up your own 'look' via a TextView which functions as a template (therefore you have to create an other .xml-file)
        View viewThatGetsDisplayed = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(viewThatGetsDisplayed);

        return viewHolder;
    }

    //this method is called as many times as the getItemCount()-Result
    //-> for each item of the ArrayList once
    //here it sets the text for each entry of the RecyclerView
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TextView usernameTV;
        final ImageView hookIV;

        usernameTV = holder.textView;
        hookIV = holder.imageView;

        final Drawable checkboxWithHook = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.checkbox_on_background);
        final Drawable checkboxWithOutHook = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.checkbox_off_background);

        //sets the text for the textView (=username)
        usernameTV.setText(UsersDb.getUsernameFromId(arrayList.get(position)));

        //changes the checkbox for the users the current user follows
        if(MainActivity.currentUser.get("follows") != null) {

            MainActivity.userFollows = (ArrayList<String>) MainActivity.currentUser.get("follows");

        }

        for(String idstheUserFollows : MainActivity.userFollows){

            if (arrayList.get(position).equals(idstheUserFollows)){

                hookIV.setImageDrawable(checkboxWithHook);

            }

        }




        //you can also add a onClickListener, here with an intent to the 'UsersFeedActivity'
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToUsersFeedActivity = new Intent(getApplicationContext(), FeedActivity.class);

                v.getContext().startActivity(intentToUsersFeedActivity);

            }
        });

        final String idOfTappedUser = String.valueOf(arrayList.get(position));

        //onClickListener for checkbox
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //update the ParseStatus




                //Variable, die speichert, ob der user in der liste ist
                boolean userIsInList = false;

                //loppt durch die Liste der id's
                for (String idOfUsersFollowings : MainActivity.userFollows) {

                    //checkt ob der entsprechende User in der Liste vorhanden ist
                    if (idOfTappedUser.equals(idOfUsersFollowings)) {

                        Log.i("following status", "user is in list");

                        userIsInList = true;

                        break;

                    }


                }

                //entfernt die user id, wenn er vorher in der liste war
                if (userIsInList) {

                    //unchecks the checkbox
                    hookIV.setImageDrawable(checkboxWithOutHook);

                    //updating the ArrayList
                    MainActivity.userFollows.remove(idOfTappedUser);

                    Log.i("following List updated", MainActivity.userFollows.toString());

                    //save the updated list on parse
                    MainActivity.currentUser.put("follows", MainActivity.userFollows);

                    MainActivity.currentUser.saveInBackground();

                    //adds the user
                } else {

                    //checkbox gets checked
                    hookIV.setImageDrawable(checkboxWithHook);

                    MainActivity.userFollows.add(idOfTappedUser);

                    Log.i("following List updated", MainActivity.userFollows.toString());

                    //save the updated list on parse
                    MainActivity.currentUser.put("follows", MainActivity.userFollows);

                    MainActivity.currentUser.saveInBackground();

                }

            }


        });

        //update db
        UsersDb.updateFollowsAl();

    }


    //defines how many times the onBindViewHolder()-Method is called
    @Override
    public int getItemCount() {

        return arrayList.size();
    }

}
