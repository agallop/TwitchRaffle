package com.bignerdranch.android.twitchraffle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
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


public class MainActivity extends ActionBarActivity {

    //holds the name of contestants
    //contestests don't have to be distinct, so multiple chances to win is possible
    private ArrayList<String> contestants;
    //Key to find out how many contestants were saved in save in savedInstanceState
    private String COUNT_KEY = "COUNT";
    //Button to add a name to the raffle
    private Button addButton;
    //button to draw a name from the raffle
    private Button raffleButton;
    //button to empty the raffle
    private Button resetButton;
    //EditText to input the name to add to the raffle
    private EditText textEntry;
    //Button to change to ListActivity
    private Button listButton;

    private Button toggleButton;

    int theme;
    int themeCount;

    private LinearLayout masterLayout;

    private TextView information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int size = 0;
        theme = 0;
        themeCount = R.integer.theme_count;
        masterLayout = (LinearLayout) findViewById(R.id.master_layout);
        information = (TextView) findViewById(R.id.information);

        contestants = new ArrayList<String>();
        if (savedInstanceState != null) {
            size = savedInstanceState.getInt(COUNT_KEY);
            for (int i = 0; i < size; i++) {
                contestants.add(savedInstanceState.getString(i + ""));
            }
        }
        textEntry = (EditText) findViewById(R.id.text_entry);
        addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textEntry.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.noNameError, Toast.LENGTH_SHORT).show();
                } else {
                    String added = textEntry.getText().toString();
                    contestants.add(textEntry.getText().toString());
                    textEntry.setText("");
                    Toast.makeText(MainActivity.this, added + " added!", Toast.LENGTH_SHORT).show();
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

    private void toggleTheme() {

        int primaryColor = getColorResourceByName("primary_color" + theme);
        int secondaryColor = getColorResourceByName("secondary_color" + theme);
        int tertiaryColor = getColorResourceByName("tertiary_color" + theme);

        masterLayout.setBackgroundColor(primaryColor);

        information.setTextColor(secondaryColor);

        textEntry.setBackgroundColor(secondaryColor);
        textEntry.setTextColor(tertiaryColor);

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
