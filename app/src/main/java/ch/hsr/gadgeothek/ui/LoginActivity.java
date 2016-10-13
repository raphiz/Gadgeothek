package ch.hsr.gadgeothek.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.service.Callback;
import ch.hsr.gadgeothek.service.LibraryService;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LibraryService.setServerAddress("http://mge1.dev.ifs.hsr.ch/public");

        if (LibraryService.isLoggedIn() ) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        final EditText emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final Switch keepMeLoggedInSwitch = (Switch) findViewById(R.id.keepMeLoggedInSwitch);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailAddress = emailAddressEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final boolean keepMeLoggedIn = keepMeLoggedInSwitch.isChecked();

                LibraryService.login(emailAddress, password, keepMeLoggedIn, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        emailAddressEditText.setError("wrong email or password");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LibraryService.keepMeLoggedIn()) {
            LibraryService.logout(new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    Log.d("logout", "logout completed");
                }

                @Override
                public void onError(String message) {
                    Log.d("logout", "error during logout: " + message);
                }
            });
        }
    }

}
