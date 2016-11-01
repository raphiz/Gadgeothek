package ch.hsr.gadgeothek.util;

import android.widget.EditText;

/**
 * Created by jmat on 01.11.16.
 */
public class ValidationWrapper {
    EditText editText;
    int errorMsgID;

    public ValidationWrapper(EditText editText, int errorMsgID) {
        this.editText = editText;
        this.errorMsgID = errorMsgID;
    }

    public EditText getEditText() {
        return editText;
    }

    public int getErrorMsgID() {
        return errorMsgID;
    }

}
