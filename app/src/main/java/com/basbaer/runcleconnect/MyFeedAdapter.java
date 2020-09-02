package com.basbaer.runcleconnect;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basbaer.runcleconnect.databinding.FeedRecyclerViewLayoutBinding;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder> {



    private ArrayList<String> adapterAL;
    private ArrayList<String> usernamesAL;

    public MyFeedAdapter(ArrayList<String> arrayListToBeDisplayed, ArrayList<String> usernamesAL) {

        adapterAL = arrayListToBeDisplayed;

        this.usernamesAL = usernamesAL;

        Log.i("givenAL", adapterAL.toString());

        Log.i("givenUsernamesAL", usernamesAL.toString());

    }

    @NonNull
    @Override
    public MyFeedAdapter.MyFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewDisplayed = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_view_layout, parent, false);

        MyFeedViewHolder viewHolder = new MyFeedViewHolder(viewDisplayed);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyFeedViewHolder holder, int position) {

        holder.textView.setText(adapterAL.get(position));



        holder.userNameTextView.setText(usernamesAL.get(position));

    }


    @Override
    public int getItemCount() {
        return adapterAL.size();
    }



    //here are the items/views handled, which every entry of the RecyclerView contains
    //this example only contains a TextView
    public static class MyFeedViewHolder extends RecyclerView.ViewHolder{

        private FeedRecyclerViewLayoutBinding feedRecyclerViewLayoutBinding;

        public TextView textView;
        public TextView userNameTextView;

        //in this constructor, the views are referenced
        public MyFeedViewHolder(View incomingTextView){
            super(incomingTextView);


            textView = incomingTextView.findViewById(R.id.textViewForFeedRecyclerView);
            userNameTextView = incomingTextView.findViewById(R.id.textViewUserNameForFeedRecyclerView);

        }
    }

}
