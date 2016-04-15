package com.bignerdranch.android.twitchraffle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class ListActivity extends ActionBarActivity {

    ArrayList<String> users;
    RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Button undoButton;
    private LinearLayout headLayout;
    //EditText to input the name to add to the raffle
    private EditText textEntry;
    //Button to add a name to the raffle
    private Button addButton;
    private FrameLayout viewHolder;
    private LinearLayout mDivider;
    int theme;


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
            theme = intent.getIntExtra("theme", 0);
        }
        else{
            int size = savedInstanceState.getInt("User Count");
            for(int i = 0; i < size; i++){
                users.add(savedInstanceState.getString("contestant" + i));
            }
            Collections.sort(users);

            theme = savedInstanceState.getInt("theme");
        }
        setContentView(R.layout.activity_list);

        int primaryColor = getColorResourceByName("primary_color" + theme);
        int secondaryColor = getColorResourceByName("secondary_color" + theme);
        int tertiaryColor = getColorResourceByName("tertiary_color" + theme);

        viewHolder = (FrameLayout)findViewById(R.id.view_holder);
        viewHolder.setBackgroundColor(primaryColor);

        mDivider = (LinearLayout) findViewById(R.id.divider);
        mDivider.setBackgroundColor(secondaryColor);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(users,primaryColor, secondaryColor,tertiaryColor);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setBackgroundColor(primaryColor);

        headLayout = (LinearLayout) findViewById(R.id.head_layout);
        headLayout.setBackgroundColor(primaryColor);


        undoButton = (Button) findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAdapter.undo()){
                    Toast.makeText(getApplicationContext(), "Nothing to Undo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        undoButton.setBackgroundColor(secondaryColor);
        undoButton.setTextColor(tertiaryColor);



        textEntry = (EditText) findViewById(R.id.text_entry);
        textEntry.setBackgroundColor(secondaryColor);
        textEntry.setTextColor(tertiaryColor);

        addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textEntry.getText().toString().equals("")){
                    Toast.makeText(ListActivity.this, R.string.noNameError,Toast.LENGTH_SHORT ).show();
                }
                else{
                    mAdapter.add(textEntry.getText().toString(), false);
                    textEntry.setText("");
                }
            }
        });

        addButton.setBackgroundColor(secondaryColor);
        addButton.setTextColor(tertiaryColor);


    }

    private void setTheme(){

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
        savedInstanceState.putInt("theme", theme);
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

    private int getColorResourceByName(String aString) {
        String packageName = getPackageName();
        return getResources().getColor(getResources().getIdentifier(aString, "color", packageName));
    }
}
