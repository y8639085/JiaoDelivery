package com.unnc.zy18717.jiaodelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // adaptation of portrait or landscape
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_signin);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_signin_landscape);

        usernameField = (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = sp.getString(MyProviderContract.USERNAME, "");
        String password = sp.getString(MyProviderContract.PASSWORD, "");
        if (name.equals("") || password.equals("")) {
            Log.e("jiao","wrong");
        } else {
            usernameField.setText(name);
            passwordField.setText(password);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signIn (View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        String[] projection = new String[] {"username"};
        String[] selectionArgs = new String[] {username, password};

        Cursor cursor = getContentResolver().query(MyProviderContract.USERS_URI, projection, "username=? AND password=?", selectionArgs, null);
        if (cursor.getCount() != 0) {
            editor = sp.edit();
            editor.putString(MyProviderContract.USERNAME, username);
            editor.putString(MyProviderContract.PASSWORD,password);
            editor.commit();
            Toast.makeText(this, "LogIn success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Username or Password wrong, try again", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void signUp (View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
