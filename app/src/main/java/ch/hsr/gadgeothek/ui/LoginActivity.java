package ch.hsr.gadgeothek.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.service.SimpleLibraryServiceCallback;

public class LoginActivity extends BaseLoginSignupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (LibraryService.isLoggedIn() || LibraryService.handleAutomaticLogin(LoginActivity.this)) {
            startMainActivity(LoginActivity.this);
            return;
        }

        final EditText emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final Switch keepMeLoggedInSwitch = (Switch) findViewById(R.id.keepMeLoggedInSwitch);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(emailAddressEditText, passwordEditText, keepMeLoggedInSwitch, LoginActivity.this, new SimpleLibraryServiceCallback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        startMainActivity(LoginActivity.this);
                    }

                    @Override
                    public void onError(String message) {
                        showOverallErrorMsg(R.id.activity_login, getString(R.string.error_login));
                    }
                });
            }
        });

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra(Intent.EXTRA_EMAIL, emailAddressEditText.getText().toString());
                intent.putExtra(Constant.INTENT_PASSWORD, passwordEditText.getText().toString());
                startActivity(intent);
            }
        });
    }

}
