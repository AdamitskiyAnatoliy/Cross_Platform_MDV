package com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView notesList;
    ArrayList<Note> notesArrayList;
    ArrayList<ParseObject> parseArrayList;
    ParseObject parseNote;
    ArrayAdapter<Note> adapter;
    int listPosition;
    public static final String DELETE_POST = "com.anatoliyadamitskiy.airball.DELETE_POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "mgZqRjcCPjoyOfCGv8bmwHENpehZYoSsnvgsMUpe",
                "u6aZbalHSzB79uxXR2AsQmYaZYcANA2n0rUiaxAv");

        notesList = (ListView) findViewById(R.id.noteListView);
        parseArrayList = new ArrayList<>();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
        } else {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }

        FloatingActionButton addButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_add))
                .withButtonColor(Color.rgb(33, 150, 243))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                startActivity(intent);

            }
        });

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listPosition = position;
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("Note", notesArrayList.get(position));
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(DELETE_POST);
        this.registerReceiver(broadcastReceiver, intentFilter);

        notesArrayList = new ArrayList<>();
        adapter = new ArrayAdapter<Note>(this, R.layout.item_layout, notesArrayList);
        notesList.setAdapter(adapter);
        refreshNoteList();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void refreshNoteList() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
        query.whereEqualTo("author", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    notesArrayList.clear();
                    parseArrayList.clear();
                    for (ParseObject post : postList) {
                        parseNote = post;
                        parseArrayList.add(post);
                        Note note = new Note(post.getString("content"), post.getString("title"), Integer.parseInt(post.getString("hours")));
                        notesArrayList.add(note);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }

        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(DELETE_POST)) {
                parseArrayList.get(listPosition).deleteInBackground();
                refreshNoteList();
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("loggedIn", false);
            editor.commit();

            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();

            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
