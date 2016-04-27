package com.viiup.android.flock.application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.viiup.android.flock.services.IAsyncRequestResponse;
import com.viiup.android.flock.services.UserService;

public class PasswordResetActivity extends AppCompatActivity {

    private TextView textViewCancel;
    private EditText editTextEmail;
    private Button buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        setContentView(R.layout.password_reset_activity);

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

    /*
        On click handler for the button click event for Reset In button.
     */
    private class ResetButtonClickHandler implements Button.OnClickListener, IAsyncRequestResponse {

        @Override
        public void onClick(View view) {
            UserService userService = new UserService();
            userService.resetUserPassword(editTextEmail.getText().toString());
        }

        @Override
        public void responseHandler(String authenticatedUserJson) {
            Toast.makeText(getApplicationContext(), R.string.msg_password_sent, Toast.LENGTH_LONG).show();
        }

        @Override
        public void backGroundErrorHandler(Exception ex) {
            // Print stack trace...may be add logging in future releases
            ex.printStackTrace();

            // display error message
            Toast.makeText(getApplicationContext(), R.string.error_something_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}
