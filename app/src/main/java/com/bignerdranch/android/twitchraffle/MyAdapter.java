package com.bignerdranch.android.twitchraffle;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Anthony on 4/7/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<String> mUsers;
    LinkedList<Pair<Boolean, String>> history;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mRemoveButton;
        public TextView mName;
        public LinearLayout mLayout;

        public ViewHolder(View v) {
            super(v);
            mRemoveButton = (ImageView) v.findViewById(R.id.remove_button);
            mName = (TextView) v.findViewById(R.id.user_text_view);
            mLayout = (LinearLayout) v.findViewById(R.id.list_item_layout);
        }
    }

    public void add(String user, Boolean undo) {
        if(!undo){
            history.push(new Pair<Boolean, String>(true, user));
        }
        mUsers.add(user);
        Collections.sort(mUsers);
        notifyDataSetChanged();
    }
    public boolean undo(){
        if(history.isEmpty()){
            return false;
        } else {
            Pair<Boolean, String> operation = history.pop();
            if(operation.first){
                remove(operation.second);
            } else {
                add(operation.second, true);
            }
            return true;
        }
    }
    private void remove(String user){
        mUsers.remove(user);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        String user = mUsers.remove(position);
        history.push(new Pair<Boolean, String>(false, user));
        notifyDataSetChanged();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String > users) {
        mUsers = users;
        history = new LinkedList<Pair<Boolean, String>>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mName.setText(mUsers.get(position));

        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
                notifyDataSetChanged();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public String getItem(int index){ return mUsers.get(index); }

}
