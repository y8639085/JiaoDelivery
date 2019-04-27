package com.unnc.zy18717.jiaodelivery;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void register(View view) {
        EditText usernameField = (EditText)findViewById(R.id.username);
        EditText passwordField = (EditText)findViewById(R.id.password);
        EditText passwordConField = (EditText)findViewById(R.id.passwordCon);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String passwordCon = passwordConField.getText().toString();

        String[] projection = new String[] {MyProviderContract.USERNAME};
        String[] selectionArgs = new String[] {username};

        if (username.length() != 0 && password.length() != 0 && passwordCon.length() != 0) {
            if (password.equals(passwordCon)) {
                Cursor cursor = getContentResolver().query(MyProviderContract.USERS_URI, projection, "username=?", selectionArgs, null);
                if (cursor.getCount() == 0) {
                    ContentValues newValues = new ContentValues();
                    newValues.put(MyProviderContract.USERNAME, username);
                    newValues.put(MyProviderContract.PASSWORD, password);
                    getContentResolver().insert(MyProviderContract.USERS_URI, newValues);
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Username exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Passwords must be same!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "Input at least one character", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void cancel (View view) {
        finish();
    }
}
