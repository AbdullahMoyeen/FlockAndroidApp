package com.viiup.android.flock.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.UserService;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.signup_activity);

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserModel user = new UserModel();
                user.setFirstName(editTextFirstName.getText().toString());
                user.setLastName(editTextLastName.getText().toString());
                user.setEmailAddress(editTextEmail.getText().toString());

                UserService userService = new UserService();
                // replace with async call
//                userService.signup(user);

                Toast.makeText(view.getContext(), R.string.msg_password_sent, Toast.LENGTH_LONG).show();

                Intent signinActivityIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(signinActivityIntent);
            }
        });
    }
}
