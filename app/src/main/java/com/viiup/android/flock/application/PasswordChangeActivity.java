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
import com.viiup.android.flock.models.UserPasswordChangeModel;
import com.viiup.android.flock.services.IAsyncRequestResponse;
import com.viiup.android.flock.services.UserService;

public class PasswordChangeActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private EditText editTextNewPassword;
    private EditText editTextRePassword;
    private Button buttonSubmit;
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.password_change_activity);

        context = this;
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
        editTextRePassword = (EditText) findViewById(R.id.editTextRePassword);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new OnSubmitButtonClicked());
    }

    private class OnSubmitButtonClicked implements Button.OnClickListener, IAsyncRequestResponse {

        @Override
        public void responseHandler(String response) {
            if (progressDialog != null) progressDialog.dismiss();

            Toast.makeText(getApplicationContext(), R.string.msg_password_changed, Toast.LENGTH_LONG).show();

            Intent homeActivityIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeActivityIntent);
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

            progressDialog = ProgressDialog.show(context, "CHANGE PASSWORD",
                    getString(R.string.msg_processing_request));

            // Async call
            UserService userService = new UserService();
            userService.changeUserPassword(userPassword, this);
        }
    }
}
