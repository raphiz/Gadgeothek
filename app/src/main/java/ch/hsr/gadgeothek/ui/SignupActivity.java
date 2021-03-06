package ch.hsr.gadgeothek.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.service.SimpleLibraryServiceCallback;
import ch.hsr.gadgeothek.util.ErrorHandler;

public class SignupActivity extends BaseLoginSignupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final EditText studentNumberEditText = (EditText) findViewById(R.id.studentNumberEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final Switch keepMeLoggedInSwitch = (Switch) findViewById(R.id.keepMeLoggedInSwitch);

        emailAddressEditText.setText(getIntent().getStringExtra(Intent.EXTRA_EMAIL));
        passwordEditText.setText(getIntent().getStringExtra(Constant.INTENT_PASSWORD));

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignUp(emailAddressEditText, nameEditText, studentNumberEditText,
                        passwordEditText, keepMeLoggedInSwitch, SignupActivity.this, new SimpleLibraryServiceCallback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                startMainActivity(SignupActivity.this);
                            }

                            @Override
                            public void onError(String message) {
                                ErrorHandler.showOverallErrorMsg(SignupActivity.this, R.id.activity_signup, getString(R.string.error_login));
                            }
                        });
            }
        });

        findViewById(R.id.changeServerAddress).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showServerAddressDialog();
            }
        });

    }

}
