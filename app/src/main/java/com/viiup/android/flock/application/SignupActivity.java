package com.viiup.android.flock.application;

import android.app.ProgressDialog;
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
import com.viiup.android.flock.services.IAsyncRequestResponse;
import com.viiup.android.flock.services.UserService;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private Button buttonSignup;
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.signup_activity);

        context = this;
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new SignUpButtonClickHandler());
    }

    private class SignUpButtonClickHandler implements Button.OnClickListener, IAsyncRequestResponse {

        @Override
        public void responseHandler(String response) {
            if (progressDialog != null) progressDialog.dismiss();

            if (response != null) {
                Toast.makeText(getApplicationContext(), R.string.msg_password_sent, Toast.LENGTH_LONG).show();

                Intent signinActivityIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(signinActivityIntent);
            }
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {

            if (progressDialog != null) progressDialog.dismiss();

            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(getApplicationContext(), R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            UserModel user = new UserModel();
            user.setFirstName(editTextFirstName.getText().toString());
            user.setLastName(editTextLastName.getText().toString());
            user.setEmailAddress(editTextEmail.getText().toString());

            progressDialog = ProgressDialog.show(context, "SIGN UP",
                    getString(R.string.msg_processing_request));

            UserService userService = new UserService();
            userService.signup(user, this);
        }
    }
}
