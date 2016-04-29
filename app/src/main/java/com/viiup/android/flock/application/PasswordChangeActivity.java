package com.viiup.android.flock.application;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.models.UserPasswordChangeModel;
import com.viiup.android.flock.services.IAsyncRequestResponse;
import com.viiup.android.flock.services.UserService;

public class PasswordChangeActivity extends AppCompatActivity {

    private TextView textViewCancel;
    private EditText editTextPassword;
    private EditText editTextNewPassword;
    private EditText editTextRePassword;
    private Button buttonSubmit;
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.password_change_activity);

        String tempPassword = getIntent().getStringExtra("tempPassword");

        textViewCancel = (TextView) findViewById(R.id.textViewCancel);
        textViewCancel.setText(Iconify.compute(this, getString(R.string.icon_fa_cancel)));
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
        editTextRePassword = (EditText) findViewById(R.id.editTextRePassword);

        if (tempPassword != null) {
            editTextPassword.setText(tempPassword);
            editTextNewPassword.requestFocus();
        }

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new OnSubmitButtonClicked());
    }

    private class OnSubmitButtonClicked implements Button.OnClickListener, IAsyncRequestResponse {

        @Override
        public void onClick(View view) {

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (!editTextNewPassword.getText().toString().equals(editTextRePassword.getText().toString())) {
                Toast.makeText(context, R.string.error_password_change_match, Toast.LENGTH_SHORT).show();
                editTextNewPassword.getText().clear();
                editTextRePassword.getText().clear();
                editTextNewPassword.requestFocus();
            } else {

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

                progressDialog = ProgressDialog.show(context, getString(R.string.title_change_password), getString(R.string.msg_processing_request));

                // Async call
                UserService userService = new UserService();
                userService.changeUserPassword(userPassword, this);
            }
        }

        @Override
        public void responseHandler(String authenticatedUserJson) {

            if (progressDialog != null) progressDialog.dismiss();

            if (authenticatedUserJson != null && authenticatedUserJson.length() > 0) {

                SharedPreferences mPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor mPrefsEditor = mPref.edit();

                mPrefsEditor.putString("authenticatedUserJson", authenticatedUserJson);
                mPrefsEditor.apply();

                Toast.makeText(getApplicationContext(), R.string.msg_password_changed, Toast.LENGTH_LONG).show();

                Intent homeIntent = new Intent(context, HomeActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
            } else {
                Toast.makeText(context, R.string.error_password_change_wrong, Toast.LENGTH_SHORT).show();
                editTextPassword.getText().clear();
                editTextNewPassword.getText().clear();
                editTextRePassword.getText().clear();
                editTextPassword.requestFocus();
            }
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {

            if (progressDialog != null) progressDialog.dismiss();

            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(getApplicationContext(), R.string.error_password_change_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
