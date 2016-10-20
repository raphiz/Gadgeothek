package ch.hsr.gadgeothek.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Switch;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.service.SimpleLibraryServiceCallback;
import ch.hsr.gadgeothek.ui.fragment.ServerAddressDialogFragment;
import ch.hsr.gadgeothek.util.ErrorHandler;
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
                ErrorHandler.showOverallErrorMsg(BaseLoginSignupActivity.this, R.id.activity_signup, getString(R.string.error_signup));
            }
        });

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
