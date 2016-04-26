package com.viiup.android.flock.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.UserService;

public class SigninActivity extends AppCompatActivity {

    private TextView textViewCancel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.signin_activity);

        textViewCancel = (TextView) findViewById(R.id.textViewCancel);
        textViewCancel.setText(Iconify.compute(this, getString(R.string.icon_fa_cancel)));
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignin = (Button) findViewById(R.id.buttonSignin);
        buttonSignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                UserService userService = new UserService();
                UserModel authenticatedUser = userService.signin(editTextEmail.getText().toString(), editTextPassword.getText().toString());

                if (authenticatedUser != null) {
                    SharedPreferences mPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                    SharedPreferences.Editor mPrefsEditor = mPref.edit();

                    Gson gson = new Gson();
                    String authenticatedUserJson = gson.toJson(authenticatedUser);
                    mPrefsEditor.putString("authenticatedUserJson", authenticatedUserJson);
                    mPrefsEditor.apply();

                    Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeActivityIntent);
                } else {
                    Toast.makeText(view.getContext(), R.string.error_invalid_login, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: apply business logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: apply business logic
        return password.length() >= 8;
    }
}
