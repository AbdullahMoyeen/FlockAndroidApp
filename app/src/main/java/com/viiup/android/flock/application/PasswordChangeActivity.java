package com.viiup.android.flock.application;

import android.content.Context;
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
import com.viiup.android.flock.models.UserPasswordChangeModel;
import com.viiup.android.flock.services.UserService;

public class PasswordChangeActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private EditText editTextNewPassword;
    private EditText editTextRePassword;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.password_change_activity);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
        editTextRePassword = (EditText) findViewById(R.id.editTextRePassword);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences mPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                String authenticatedUserJson = mPref.getString("authenticatedUserJson", null);

                Gson gson = new Gson();
                UserModel authenticatedUser = gson.fromJson(authenticatedUserJson, UserModel.class);

                UserPasswordChangeModel userPassword = new UserPasswordChangeModel();
                userPassword.setUserId(authenticatedUser.getUserId());
                userPassword.setEmailAddress(authenticatedUser.getEmailAddress());
                userPassword.setPassword(editTextPassword.getText().toString());
                userPassword.setNewPassword(editTextNewPassword.getText().toString());
                userPassword.setReEnteredPassword(editTextRePassword.getText().toString());

                UserService userService = new UserService();
                // replace with async call
//                userService.changeUserPassword(userPassword);

                Toast.makeText(view.getContext(), R.string.msg_password_changed, Toast.LENGTH_LONG).show();

                Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeActivityIntent);
            }
        });
    }
}
