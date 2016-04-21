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
import java.util.List;

/**
 * Created by Anthony on 4/7/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Pair<String, Integer>> mUsers;
   // LinkedList<Pair<Boolean, Pair<String, Integer>>> history;
    int mPrimaryColor, mSecondaryColor, mTertiaryColor;
    boolean chanceSort;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mRemoveButton;
        public TextView mName;
        public LinearLayout mLayout;
        public TextView mChances;

        public ViewHolder(View v) {
            super(v);
            mChances = (TextView) v.findViewById(R.id.chances_text_view);
            mRemoveButton = (ImageView) v.findViewById(R.id.remove_button);
            mName = (TextView) v.findViewById(R.id.user_text_view);
            mLayout = (LinearLayout) v.findViewById(R.id.list_item_layout);
        }
    }

   /* public void add(Pair<String, Integer> user, Boolean undo) {
        if(!undo){
            history.push(new Pair<Boolean, Pair<String, Integer>>(true, user));
        }
        mUsers.add(user);
        quickSort(mUsers, 0, mUsers.size(), true);
        notifyDataSetChanged();
    } */
  /*  public boolean undo(){
        if(history.isEmpty()){
            return false;
        } else {
            Pair<Boolean, Pair<String, Integer>> operation = history.pop();
            if(operation.first){
                remove(operation.second);
            } else {
                add(operation.second, true);
            }
            return true;
        }
    } */
    private void remove(String user){
        mUsers.remove(user);
        notifyDataSetChanged();
    }

    public void remove(int position) {
     /*  String user = */ mUsers.remove(position);
     //  history.push(new Pair<Boolean, String>(false, user));
        notifyDataSetChanged();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Pair<String, Integer>> users, int primaryColor,int secondaryColor,int tertiaryColor) {
        mUsers = users;
        chanceSort = true;
        quickSort(mUsers, 0, mUsers.size() - 1);
       // history = new LinkedList<Pair<Boolean, String>>();
        mPrimaryColor = primaryColor;
        mSecondaryColor = secondaryColor;
        mTertiaryColor = tertiaryColor;
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
        holder.mName.setText(mUsers.get(position).first);
        holder.mName.setBackgroundColor(mSecondaryColor);
        holder.mName.setTextColor(mTertiaryColor);

        holder.mChances.setText(mUsers.get(position).second + "");
        holder.mChances.setBackgroundColor(mSecondaryColor);
        holder.mChances.setTextColor(mTertiaryColor);

        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
                quickSort(mUsers, 0, mUsers.size() - 1);
                notifyDataSetChanged();
            }
        });

        holder.mLayout.setBackgroundColor(mSecondaryColor);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public Pair<String, Integer> getItem(int index){ return mUsers.get(index); }

    private void quickSort(ArrayList<Pair<String, Integer>> list, int lo, int hi){
      if(lo < hi){
        int p = 0;
        if(chanceSort) {
            p = partitionByChance(list, lo, hi);
        }
        else {
            p = partitionByName(list, lo, hi);
        }

        quickSort(list, lo, p - 1);
        quickSort(list, p + 1, hi);
      }
    } 
    
    private int partitionByChance(ArrayList<Pair<String, Integer>> list, int lo, int hi) {
      Pair <String, Integer> pivot = list.get(hi);
      int i = lo;
      for (int j = lo; j < hi; j++){
        if(compareByChance(list.get(j), pivot) < 0){
          Pair <String, Integer> holder = list.get(j);
          list.set(j, list.get(i));
          list.set(i, holder);
          i = i + 1;
        }
      }
      Pair<String, Integer> holder = list.get(i);
      list.set(i, list.get(hi));
      list.set(hi, holder);
      return i;
    }
    
    private int compareByChance(Pair<String, Integer> a, Pair <String, Integer> b) {
      if(a.second == b.second)
        return a.first.compareTo(b.first);
      return(b.second - a.second);
    }
    
    private int partitionByName(ArrayList<Pair<String, Integer>> list, int lo, int hi){
        String pivot = list.get(hi).first;
        int i = lo;
        for (int j = lo; j < hi; j++){
            if(list.get(j).first.compareTo(pivot) < 0){
                Pair <String, Integer> holder = list.get(j);
                list.set(j, list.get(i));
                list.set(i, holder);
                i = i + 1;
            }
        }
        Pair<String, Integer> holder = list.get(i);
        list.set(i, list.get(hi));
        list.set(hi, holder);
        return i;
    }

}
