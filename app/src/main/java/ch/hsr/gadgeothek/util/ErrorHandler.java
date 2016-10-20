package ch.hsr.gadgeothek.util;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;

public final class ErrorHandler {

    public static void showOverallErrorMsg(Activity activity, int viewId, String errorMsg) {
        Snackbar.make(activity.findViewById(viewId), errorMsg, Snackbar.LENGTH_SHORT).show();
        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
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

}