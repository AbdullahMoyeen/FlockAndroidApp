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
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.models.UserModel;
import com.viiup.android.flock.services.IAsyncRequestResponse;
import com.viiup.android.flock.services.UserService;

public class SignupActivity extends AppCompatActivity {

    private Context context;
    private TextView textViewCancel;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private Button buttonSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.signup_activity);

        textViewCancel = (TextView) findViewById(R.id.textViewCancel);
        textViewCancel.setText(Iconify.compute(this, getString(R.string.icon_fa_cancel)));
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextEmail.setText(R.string.fmt_email_domain);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new SignupButtonClickHandler());
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinActivityIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(signinActivityIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private class SignupButtonClickHandler implements Button.OnClickListener, IAsyncRequestResponse {

        @Override
        public void onClick(View v) {

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (editTextFirstName.getText().toString().trim().equals(""))
                editTextFirstName.setError(getString(R.string.error_field_required));
            else if (editTextLastName.getText().toString().trim().equals(""))
                editTextLastName.setError(getString(R.string.error_field_required));
            else if (!CommonHelper.isEmailValid(context, editTextEmail.getText().toString()) || !CommonHelper.isDomainValid(context, editTextEmail.getText().toString()))
                editTextEmail.setError(getString(R.string.error_invalid_domain));
            else {
                UserModel user = new UserModel();
                user.setFirstName(editTextFirstName.getText().toString());
                user.setLastName(editTextLastName.getText().toString());
                user.setEmailAddress(editTextEmail.getText().toString());

                progressDialog = ProgressDialog.show(context, getString(R.string.title_signup), getString(R.string.msg_processing_request));

                UserService userService = new UserService();
                userService.signup(user, this);
            }
        }

        @Override
        public void responseHandler(String response) {
            if (progressDialog != null) progressDialog.dismiss();

            if (response != null) {
                Toast.makeText(context, R.string.msg_password_sent, Toast.LENGTH_LONG).show();

                Intent signinActivityIntent = new Intent(context, SigninActivity.class);
                startActivity(signinActivityIntent);
            }
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {

            if (progressDialog != null) progressDialog.dismiss();

            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(context, R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}
