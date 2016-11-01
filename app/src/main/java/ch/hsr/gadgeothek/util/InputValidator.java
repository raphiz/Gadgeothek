package ch.hsr.gadgeothek.util;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.hsr.gadgeothek.R;

public final class InputValidator {

    final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    final static String NUMBER_PATTERN = "-?\\d+(\\.\\d+)?";

    public static List<ValidationWrapper> checkForEmptyFields(EditText... inputFields) {
        List<ValidationWrapper> invalidFields = new ArrayList<>();
        for (EditText inputField : inputFields) {
            if (isFieldEmpty(inputField.getText().toString())) {
                invalidFields.add(new ValidationWrapper(inputField, R.string.error_empty_input));
            }
        }
        return invalidFields;
    }

    public static List<ValidationWrapper> validateNumberFields(EditText... numberFields) {
        List<ValidationWrapper> invalidFields = new ArrayList<>();
        for (EditText numberField : numberFields) {
            if (isValidNumber(numberField.getText().toString())) {
                invalidFields.add(new ValidationWrapper(numberField, R.string.error_invalid_input));
            }
        }
        return invalidFields;
    }

    public static List<ValidationWrapper> validateEmailField(EditText emailAddress) {
        List<ValidationWrapper> invalidFields = new ArrayList<>();
        if (hasInvalidEmail(emailAddress.getText().toString())) {
            invalidFields.add(new ValidationWrapper(emailAddress, R.string.error_invalid_input));
        }
        return invalidFields;
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

}
