package ch.hsr.gadgeothek.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.service.Callback;

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
                        passwordEditText, keepMeLoggedInSwitch, SignupActivity.this, new Callback<Boolean>() {
                            @Override
                            public void onCompletion(Boolean input) {
                                startMainActivity(SignupActivity.this);
                                finish();
                            }

                            @Override
                            public void onError(String message) {
                                showOverallErrorMsg(R.id.activity_login, getString(R.string.error_login));
                            }
                        });
            }
        });


    }

}
