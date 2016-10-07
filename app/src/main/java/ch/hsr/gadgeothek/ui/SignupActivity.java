package ch.hsr.gadgeothek.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.service.Callback;
import ch.hsr.gadgeothek.service.LibraryService;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final EditText studentNumberEditText = (EditText) findViewById(R.id.studentNumberEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        emailAddressEditText.setText(getIntent().getStringExtra(Intent.EXTRA_EMAIL));
        passwordEditText.setText(getIntent().getStringExtra(Constant.INTENT_PASSWORD));

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailAddressEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String studentNumber = studentNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                LibraryService.register(emailAddress, password, name, studentNumber, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String message) {
                        emailAddressEditText.setError("an error occured");
                    }
                });
            }
        });


    }
}
