package com.unnc.zy18717.jiaodelivery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void signIn (View view) {
        EditText usernameField = (EditText)findViewById(R.id.username);
        EditText passwordField = (EditText)findViewById(R.id.password);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        String[] projection = new String[] {"username"};
        String[] selectionArgs = new String[] {username, password};

        Cursor cursor = getContentResolver().query(MyProviderContract.USERS_URI, projection, "username=? AND password=?", selectionArgs, null);
        if (cursor.getCount() != 0) {
            Toast.makeText(this, "LogIn success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
