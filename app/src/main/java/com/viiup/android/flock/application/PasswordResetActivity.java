package com.viiup.android.flock.application;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.helpers.CommonHelper;
import com.viiup.android.flock.services.IAsyncRequestResponse;
import com.viiup.android.flock.services.UserService;

public class PasswordResetActivity extends AppCompatActivity {

    private TextView textViewCancel;
    private EditText editTextEmail;
    private Button buttonReset;
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.password_reset_activity);

        context = this;
        textViewCancel = (TextView) findViewById(R.id.textViewCancel);
        textViewCancel.setText(Iconify.compute(this, getString(R.string.icon_fa_cancel)));
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextEmail.setText(R.string.fmt_email_domain);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new ResetButtonClickHandler());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /*
        On click handler for the button click event for Reset In button.
     */
    private class ResetButtonClickHandler implements Button.OnClickListener, IAsyncRequestResponse {

        @Override
        public void onClick(View view) {

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            if (!CommonHelper.isEmailValid(context, editTextEmail.getText().toString()))
                editTextEmail.setError(getString(R.string.error_invalid_email));
            else {
                progressDialog = ProgressDialog.show(context, getString(R.string.title_reset_password), getString(R.string.msg_processing_request));
                UserService userService = new UserService();
                userService.resetUserPassword(editTextEmail.getText().toString(), this);
            }
        }

        @Override
        public void responseHandler(String responseJson) {

            if (progressDialog != null) progressDialog.dismiss();

            Toast.makeText(getApplicationContext(), R.string.msg_password_sent, Toast.LENGTH_LONG).show();

            Intent signinIntent = new Intent(getApplicationContext(), SigninActivity.class);
            signinIntent.putExtra("emailAddress", editTextEmail.getText().toString());
            signinIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signinIntent);
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {

            if (progressDialog != null) progressDialog.dismiss();

            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(getApplicationContext(), R.string.error_password_reset_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
