package com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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
    public static final String UPDATE_POST = "com.anatoliyadamitskiy.airball.UPDATE_POST";

    Handler mHandler = new Handler();
    com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata.Network network =
            new com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata.Network(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (network.checkNetwork()) {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "mgZqRjcCPjoyOfCGv8bmwHENpehZYoSsnvgsMUpe",
                    "u6aZbalHSzB79uxXR2AsQmYaZYcANA2n0rUiaxAv");
        } else {
            Toast.makeText(this, "Please Reconnect Network", Toast.LENGTH_LONG).show();
        }

        notesList = (ListView) findViewById(R.id.noteListView);
        parseArrayList = new ArrayList<>();

        FloatingActionButton addButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_add))
                .withButtonColor(Color.rgb(33, 150, 243))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (network.checkNetwork()) {
                    Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Reconnect Network", Toast.LENGTH_LONG).show();
                }

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

    public Runnable updater = new Runnable() {
        @Override
        public void run() {
            refreshNoteList();
            mHandler.postDelayed(updater,30000); // 5 seconds
        }
    };

    void startRepeating()
    {
        updater.run();
    }

    void stopRepeating()
    {
        mHandler.removeCallbacks(updater);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (network.checkNetwork()) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                stopRepeating();
                startRepeating();
            } else {
                Intent intent = new Intent(this, LogInActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        } else {
            // No Network, Pull from Local

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if (prefs.getBoolean("loggedIn", false) == false) {
                Intent intent = new Intent(this, LogInActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(DELETE_POST);
        filter.addAction(UPDATE_POST);
        this.registerReceiver(broadcastReceiver, filter);

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

        if (network.checkNetwork()) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
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
        } else {
            // Refresh Note List when no Network

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRepeating();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            if (intent.getAction().equals(DELETE_POST)) {
                parseArrayList.get(listPosition).deleteInBackground();
                refreshNoteList();
            } else if (intent.getAction().equals(UPDATE_POST)) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
                query.getInBackground(parseArrayList.get(listPosition).getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject note, ParseException e) {
                        if (e == null) {
                            note.put("title", intent.getStringExtra("title"));
                            note.put("hours", intent.getStringExtra("hours"));
                            note.put("content", intent.getStringExtra("content"));
                            note.saveInBackground();
                        }
                    }
                });

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

            if (network.checkNetwork()) {
                stopRepeating();

                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                // Manually log user out

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("loggedIn", false);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Toast.makeText(this, "NO BACK", Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        // Back?
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            // Back
//            moveTaskToBack(true);
//            return true;
//        }
//        else {
//            // Return
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Signout your app");


        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dialog.cancel();
            }
        });


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {



            }
        });


        alertDialog.show();



    }
}
