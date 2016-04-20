package com.bignerdranch.android.twitchraffle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;


public class MainActivity extends ActionBarActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    //holds the name of contestants
    //contestests don't have to be distinct, so multiple chances to win is possible
    private ArrayList<String> contestants;
    //Key to find out how many contestants were saved in save in savedInstanceState
    private String COUNT_KEY = "COUNT";


    //LinearLayout (vertical) for whole activity
    private LinearLayout masterLayout;
    //Prompt for user
    private TextView information;
    //LinearLayout (horizontal) for inputs
    private LinearLayout inputLayout;
    //Just a pound sign
    private TextView numberInfo;
    //EditText to input the number of chances
    private EditText numberEntry;
    //EditText to input the name to add to the raffle
    private EditText textEntry;
    //Button to add a contestant
    private Button addButton;
    //button to draw a name from the raffle
    private Button raffleButton;
    //button to empty the raffle
    private Button resetButton;
    //Button to change to ListActivity
    private Button listButton;
    //Toggles the theme of the app
    private Button toggleButton;

    //variables to take keep track of the theme
    int theme;
    int themeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int size = 0;
        themeCount = getResources().getInteger(R.integer.theme_count);
        Log.d(TAG, "Theme Count: " + themeCount);
        masterLayout = (LinearLayout) findViewById(R.id.master_layout);
        information = (TextView) findViewById(R.id.information);
        numberInfo = (TextView) findViewById(R.id.number_info);
        inputLayout = (LinearLayout) findViewById(R.id.input_layout);


        contestants = new ArrayList<String>();
        if (savedInstanceState != null) {
            size = savedInstanceState.getInt(COUNT_KEY);
            for (int i = 0; i < size; i++) {
                contestants.add(savedInstanceState.getString(i + ""));
            }
            theme = savedInstanceState.getInt("theme");
        } else {
            theme = 0;
        }
        numberEntry = (EditText) findViewById(R.id.number_entry);

        textEntry = (EditText) findViewById(R.id.text_entry);
        addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contestant = textEntry.getText().toString();
                int chances = Integer.valueOf(numberEntry.getText().toString());

                if (contestant.equals("")) {
                    Toast.makeText(MainActivity.this, R.string.noNameError, Toast.LENGTH_SHORT).show();
                } else if(chances == 0){
                    Toast.makeText(MainActivity.this, contestant + " can't be added zero times!", Toast.LENGTH_SHORT).show();
                } else {
                    for(int i = 0; i < chances; i++) {
                            contestants.add(contestant);
                    }
                    Toast.makeText(MainActivity.this, contestant + " added " + chances + " times!", Toast.LENGTH_SHORT).show();
                    textEntry.setText("");

                }
            }
        });

        raffleButton = (Button) findViewById(R.id.raffle_button);
        raffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contestants.size() == 0) {
                    resetButtons();
                    Toast.makeText(MainActivity.this, R.string.noContestantError, Toast.LENGTH_SHORT).show();
                } else if (raffleButton.getText().toString().equals(getString(R.string.confirm))) {
                    resetButtons();
                    Random random = new Random();
                    String winner = contestants.remove(random.nextInt(contestants.size()));

                    Toast.makeText(MainActivity.this, winner + " wins!", Toast.LENGTH_LONG).show();
                } else {
                    resetButtons();
                    raffleButton.setText(R.string.confirm);

                }
            }
        });

        resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resetButton.getText().toString().equals(getString(R.string.confirm))) {
                    contestants = new ArrayList<String>();
                    resetButtons();
                    Toast.makeText(MainActivity.this, R.string.reseted, Toast.LENGTH_LONG).show();
                } else {
                    resetButtons();
                    resetButton.setText(R.string.confirm);
                }
            }
        });

        listButton = (Button) findViewById(R.id.list_button);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("User Count", contestants.size());
                for (int i = 0; i < contestants.size(); i++) {
                    intent.putExtra("contestant" + i, contestants.get(i));
                }
                intent.putExtra("theme", theme);
                startActivityForResult(intent, 0);
            }
        });

        toggleButton = (Button) findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme++;
                theme = theme % themeCount;
                toggleTheme();
            }
        });

        toggleTheme();
    }

    /* Changes the current theme */
    private void toggleTheme() {
        Log.d(TAG, "toggleTheme number: " + theme);

        /*Gets theme name */
        String name = getString(getResources().getIdentifier("theme" + theme, "string", getPackageName()));
        Log.d(TAG, "toggleTheme name: " + name);

        /* Gets the colors for the theme */
        int primaryColor = getColorResourceByName("primary_color" + theme);
        int secondaryColor = getColorResourceByName("secondary_color" + theme);
        int tertiaryColor = getColorResourceByName("tertiary_color" + theme);


        /* Applying the theme START */
        masterLayout.setBackgroundColor(primaryColor);

        inputLayout.setBackgroundColor(primaryColor);

        information.setTextColor(secondaryColor);
        numberInfo.setTextColor(secondaryColor);

        textEntry.setBackgroundColor(secondaryColor);
        textEntry.setTextColor(tertiaryColor);

        numberEntry.setBackgroundColor(secondaryColor);
        numberEntry.setTextColor(tertiaryColor);

        addButton.setBackgroundColor(secondaryColor);
        addButton.setTextColor(tertiaryColor);

        raffleButton.setBackgroundColor(secondaryColor);
        raffleButton.setTextColor(tertiaryColor);

        resetButton.setBackgroundColor(secondaryColor);
        resetButton.setTextColor(tertiaryColor);

        listButton.setBackgroundColor(secondaryColor);
        listButton.setTextColor(tertiaryColor);

        toggleButton.setBackgroundColor(secondaryColor);
        toggleButton.setTextColor(tertiaryColor);
        /* applying the theme STOP */


        Toast.makeText(MainActivity.this, "Theme changed to " + name, Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(COUNT_KEY, contestants.size());
        for(int i = 0;i < contestants.size(); i++){
            savedInstanceState.putString(i + "", contestants.get(i));
        }
        savedInstanceState.putInt("theme", theme);
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
    private int getColorResourceByName(String aString) {
        String packageName = getPackageName();
        return getResources().getColor(getResources().getIdentifier(aString, "color", packageName));
    }

    //removes confirm text
    private void resetButtons(){
        raffleButton.setText(R.string.draw);
        resetButton.setText(R.string.reset);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0) {
            contestants = new ArrayList<String>();
            int size = data.getIntExtra("User Count", 0);
            for( int i = 0; i < size; i++){
                contestants.add(data.getStringExtra("contestant" + i));
            }
        }
    }
}
