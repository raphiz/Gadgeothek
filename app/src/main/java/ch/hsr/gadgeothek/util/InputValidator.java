package ch.hsr.gadgeothek.util;

import android.content.Context;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.hsr.gadgeothek.R;

public final class InputValidator {

    final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    final static String NUMBER_PATTERN = "-?\\d+(\\.\\d+)?";

    public static boolean checkForEmptyFieldsAndSetErrorMsgs(Context context, EditText... inputFields) {
        boolean hasEmptyFields = false;
        for (EditText inputField : inputFields) {
            if (isFieldEmpty(inputField.getText().toString())) {
                setErrorMsg(inputField, context, R.string.error_empty_input);
                hasEmptyFields = true;
            }
        }
        return hasEmptyFields;
    }

    public static boolean validateNumberFields(Context context, EditText... numberFields) {
        boolean hasInvalidFields = false;
        for (EditText numberField : numberFields) {
            if (isValidNumber(numberField.getText().toString())) {
                setErrorMsg(numberField, context, R.string.error_invalid_input);
                hasInvalidFields = true;
            }
        }
        return hasInvalidFields;
    }

    public static boolean validateEmailField(Context context, EditText emailAddress) {
        boolean hasInvalidField = hasInvalidEmail(emailAddress.getText().toString());
        if (hasInvalidField) {
            setErrorMsg(emailAddress, context, R.string.error_invalid_input);
        }
        return hasInvalidField;
    }

    private static boolean isFieldEmpty(String inputField) {
        return inputField.isEmpty();
    }

    private static boolean isValidNumber(String number) {
        return !number.matches(NUMBER_PATTERN);
    }

    private static boolean hasInvalidEmail(String emailAddress) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailAddress);
        return !matcher.matches();
    }

    private static void setErrorMsg(EditText inputField, Context context, int errorMsgID) {
        inputField.setError(context.getString(errorMsgID));
    }

}
