package com.basbaer.runcleconnect;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.parse.Parse.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {



    //here are the items/views handled, which every entry of the RecyclerView contains
    //this example only contains a TextView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        //in this constructor, the views are referenced
        public MyViewHolder(View incomingTextView) {
            super(incomingTextView);
            //Option 1
            //textView = (TextView) incomingTextView;

            //Option 2
            textView = incomingTextView.findViewById(R.id.textViewForRecyclerView);
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

        Log.i("Position", String.valueOf(position));

        holder.textView.setText(arrayList.get(position));

        //you can also add a onClickListener, here with an intent to the 'UsersFeedActivity'
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToUsersFeedActivity = new Intent(getApplicationContext(), FeedActivity.class);

                v.getContext().startActivity(intentToUsersFeedActivity);

            }
        });

    }

    //defines how many times the onBindViewHolder()-Method is called
    @Override
    public int getItemCount() {

        Log.i("ArraySize", String.valueOf(arrayList.size()));

        return arrayList.size();
    }

}
