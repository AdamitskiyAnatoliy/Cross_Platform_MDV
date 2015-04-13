package com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Anatoliy on 3/31/15.
 */
public class DetailActivity extends ActionBarActivity {

    TextView title, content, hours;
    String mainTitle;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = (TextView) findViewById(R.id.titleTextFieldDetail);
        content = (TextView) findViewById(R.id.noteTextFieldDetail);
        hours = (TextView) findViewById(R.id.hoursTextFieldDetail);

        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("Note");

        title.setText(note.getTitle());
        content.setText(note.getNote());
        hours.setText(Integer.toString(note.getTimeToComplete()));
        mainTitle = note.getTitle();
        setTitle(mainTitle);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {

            Intent intent = new Intent(MainActivity.DELETE_POST);
            getApplicationContext().sendBroadcast(intent);

            finish();

            return true;
        } else if (id == R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
