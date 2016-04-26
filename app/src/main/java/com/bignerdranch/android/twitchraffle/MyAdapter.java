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

import java.lang.annotation.Target;
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

    public void add(Pair<String, Integer> user, Boolean undo) {
        Pair <String, Integer> contestant;
         if(chanceSort)
             contestant = sequentalSearch(user.first);
        else
             contestant = binarySearch(user.first, 0, mUsers.size());
        if(contestant != null) {
            user = new Pair<String, Integer>(user.first, user.second + contestant.second)
            ;mUsers.remove(contestant);
            mUsers.add(user);
        } else {
            mUsers.add(user);
        }
        quickSort(0, mUsers.size());
         notifyDataSetChanged();
     }
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
    private void remove(String user) {
        mUsers.remove(user);
        notifyDataSetChanged();
    }

    public void remove(int position) {
     /*  String user = */
        mUsers.remove(position);
        //  history.push(new Pair<Boolean, String>(false, user));
        notifyDataSetChanged();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Pair<String, Integer>> users, int primaryColor, int secondaryColor, int tertiaryColor) {
        mUsers = users;
        chanceSort = true;
        quickSort(0, mUsers.size() - 1);
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
                quickSort(0, mUsers.size() - 1);
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


    public Pair<String, Integer> getItem(int index) {
        return mUsers.get(index);
    }

    private void quickSort(int lo, int hi) {
        if (lo < hi) {
            int p = 0;
            if (chanceSort) {
                p = partitionByChance(lo, hi);
            } else {
                p = partitionByName(lo, hi);
            }

            quickSort(lo, p - 1);
            quickSort(p + 1, hi);
        }
    }

    private int partitionByChance(int lo, int hi) {
        Pair<String, Integer> pivot = mUsers.get(hi);
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (compareByChance(mUsers.get(j), pivot) < 0) {
                Pair<String, Integer> holder = mUsers.get(j);
                mUsers.set(j, mUsers.get(i));
                mUsers.set(i, holder);
                i = i + 1;
            }
        }
        Pair<String, Integer> holder = mUsers.get(i);
        mUsers.set(i, mUsers.get(hi));
        mUsers.set(hi, holder);
        return i;
    }

    private int compareByChance(Pair<String, Integer> a, Pair<String, Integer> b) {
        if (a.second == b.second)
            return a.first.compareTo(b.first);
        return (b.second - a.second);
    }

    private int partitionByName(int lo, int hi) {
        String pivot = mUsers.get(hi).first.toLowerCase();
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (mUsers.get(j).first.toLowerCase().compareTo(pivot) < 0) {
                Pair<String, Integer> holder = mUsers.get(j);
                mUsers.set(j, mUsers.get(i));
                mUsers.set(i, holder);
                i = i + 1;
            }
        }
        Pair<String, Integer> holder = mUsers.get(i);
        mUsers.set(i, mUsers.get(hi));
        mUsers.set(hi, holder);
        return i;
    }

    Pair<String, Integer> binarySearch(String target, int lo, int hi) {
        if (lo > hi)
            return null;
        int mid = (lo + hi) / 2;
        int comparison = target.toLowerCase().compareTo(mUsers.get(mid).first.toLowerCase());
        if (comparison == 0) {
            //found
            return mUsers.get(mid);
        } else if (comparison > 0) {
            //front half
            return binarySearch(target, lo, mid - 1);

        }
        //back half
        return binarySearch(target, mid + 1, hi);
    }

    Pair<String, Integer> sequentalSearch(String target){
        int size = mUsers.size();
        for(int i = 0; i < size; i++){
            Pair<String , Integer> current = mUsers.get(i);
            if(current.first.compareTo(target) == 0){
                return current;
            }
        }

        return null;
    }
}
