package com.anatoliyadamitskiy.a_adamitskiy_android_usersanddata;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Anatoliy on 3/31/15.
 */
public class LogInActivity extends ActionBarActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, "mgZqRjcCPjoyOfCGv8bmwHENpehZYoSsnvgsMUpe",
                "u6aZbalHSzB79uxXR2AsQmYaZYcANA2n0rUiaxAv");

        username = (EditText) findViewById(R.id.usernameTextField);
        password = (EditText) findViewById(R.id.passwordTextField);

        findViewById(R.id.logInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logInInBackground(username.getText().toString(),
                        password.getText().toString(), new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {

                                    finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Login, Please Try Again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });

    }

}
