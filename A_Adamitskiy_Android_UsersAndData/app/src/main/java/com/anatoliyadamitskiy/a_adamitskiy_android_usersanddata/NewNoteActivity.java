package com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Anatoliy on 3/31/15.
 */
public class NewNoteActivity extends ActionBarActivity {

    Note noteObject;
    EditText noteTitle, noteContent, noteHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        noteTitle = (EditText) findViewById(R.id.titleTextField);
        noteContent = (EditText) findViewById(R.id.noteTextField);
        noteHours = (EditText) findViewById(R.id.hoursTextField);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {

            ParseObject note = new ParseObject("Note");
            note.put("title", noteTitle.getText().toString());
            note.put("content", noteContent.getText().toString());
            note.put("hours", noteHours.getText().toString());
            note.put("author", ParseUser.getCurrentUser());
            note.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), "User update error: " + e);
                    }
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
