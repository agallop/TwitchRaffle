package com.bignerdranch.android.twitchraffle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;


public class ListActivity extends ActionBarActivity {

    ArrayList<String> users;
    RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        users = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            int size = intent.getIntExtra("User Count", 0);
            for(int i = 0; i < size; i++){
                users.add(intent.getStringExtra("contestant" + i));
            }
            Collections.sort(users);
        }
        else{
            int size = savedInstanceState.getInt("User Count");
            for(int i = 0; i < size; i++){
                users.add(savedInstanceState.getString("contestant" + i));
            }
            Collections.sort(users);
        }
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(users);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("User Count", mAdapter.getItemCount());
        for(int i = 0; i < users.size(); i++){
            savedInstanceState.putString("contestant" + i, mAdapter.getItem(i));
        }
    }

    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("User Count", mAdapter.getItemCount());
        for(int i = 0; i < users.size(); i++){
            returnIntent.putExtra("contestant" + i, mAdapter.getItem(i));
        }
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
