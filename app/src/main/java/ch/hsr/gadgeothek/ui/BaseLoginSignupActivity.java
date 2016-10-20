package ch.hsr.gadgeothek.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Switch;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.service.SimpleLibraryServiceCallback;
import ch.hsr.gadgeothek.ui.fragment.ServerAddressDialogFragment;
import ch.hsr.gadgeothek.util.InputValidator;

public abstract class BaseLoginSignupActivity extends AppCompatActivity {

    protected void doLogin(final EditText emailAddressEditText,
                           final EditText passwordEditText,
                           final Switch keepMeLoggedInSwitch,
                           final Context context,
                           final SimpleLibraryServiceCallback loginCallback) {

        boolean hasInvalidFields = InputValidator.checkForEmptyFieldsAndSetErrorMsgs(
                context, passwordEditText);
        hasInvalidFields = InputValidator.validateEmailField(context, emailAddressEditText) || hasInvalidFields;

        if (hasInvalidFields) {
            return;
        }

        final String emailAddress = emailAddressEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final boolean keepMeLoggedIn = keepMeLoggedInSwitch.isChecked();

        LibraryService.login(emailAddress, password, keepMeLoggedIn, context, loginCallback);
    }

    protected void doSignUp(final EditText emailAddressEditText,
                            final EditText nameEditText,
                            final EditText studentNumberEditText,
                            final EditText passwordEditText,
                            final Switch keepMeLoggedInSwitch,
                            final Context context,
                            final SimpleLibraryServiceCallback loginCallback) {

        boolean hasInvalidFields = InputValidator.checkForEmptyFieldsAndSetErrorMsgs(
                context,
                nameEditText,
                studentNumberEditText,
                passwordEditText);
        hasInvalidFields = InputValidator.validateEmailField(context, emailAddressEditText) || hasInvalidFields;
        hasInvalidFields = InputValidator.validateNumberFields(context, studentNumberEditText);

        if (hasInvalidFields) {
            return;
        }

        final String emailAddress = emailAddressEditText.getText().toString();
        final String name = nameEditText.getText().toString();
        final String studentNumber = studentNumberEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final boolean keepMeLoggedIn = keepMeLoggedInSwitch.isChecked();

        LibraryService.register(emailAddress, password, name, studentNumber, new SimpleLibraryServiceCallback<Boolean>() {

            @Override
            public void onCompletion(Boolean input) {
                doLogin(emailAddressEditText, passwordEditText, keepMeLoggedInSwitch, context, loginCallback);
            }

            @Override
            public void onError(String message) {
                showOverallErrorMsg(R.id.activity_signup, getString(R.string.error_signup));
            }
        });

    }

    protected void showOverallErrorMsg(int viewId, String errorMsg) {
        Snackbar.make(findViewById(viewId), errorMsg, Snackbar.LENGTH_SHORT).show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int dot = 200;          // Length of a Morse Code "dot" in milliseconds
        int dash = 500;         // Length of a Morse Code "dash" in milliseconds
        int shortGap = 200;     // Length of Gap Between dots/dashes
        int mediumGap = 500;    // Length of Gap Between Letters
        int longGap = 1000;     // Length of Gap Between Words
        long[] pattern = {
                0, 
                dot, shortGap, dot, shortGap, dot,    // s
                mediumGap,
                dash, shortGap, dash, shortGap, dash, // o
                mediumGap,
                dot, shortGap, dot, shortGap, dot,    // s
                longGap
        };
        vibrator.vibrate(pattern, -1);
    }

    protected void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    protected void showServerAddressDialog() {
        DialogFragment serverAddressDialog = new ServerAddressDialogFragment();
        serverAddressDialog.show(getFragmentManager(), "serverAddress");
    }

}
