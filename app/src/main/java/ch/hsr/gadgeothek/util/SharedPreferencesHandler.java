package ch.hsr.gadgeothek.util;

import android.content.Context;
import android.content.SharedPreferences;

import ch.hsr.gadgeothek.service.LoginToken;

public final class SharedPreferencesHandler {

    private static final String SHARED_PREFS_FILE = "gadeothek_prefs";
    private static final String CUSTOMER_ID = "customerId";
    private static final String SECURITY_TOKEN = "securityToken";
    private static final String SERVER_ADDRESS ="serverAddress";

    private static final String DEFAULT_SERVER_ADDRESS = "http://mge1.dev.ifs.hsr.ch/public";

    public static void addLoginToken(Context context, LoginToken loginToken) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(CUSTOMER_ID, loginToken.getCustomerId());
        editor.putString(SECURITY_TOKEN, loginToken.getSecurityToken());
        editor.commit();
    }

    public static LoginToken getLoginToken(Context context) {
        LoginToken loginToken = new LoginToken();
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS_FILE, context.MODE_PRIVATE);
        String customerId = sharedPref.getString(CUSTOMER_ID, null);
        String securityToken = sharedPref.getString(SECURITY_TOKEN, null);
        if (customerId != null && securityToken != null) {
            loginToken.setCustomerId(customerId);
            loginToken.setSecurityToken(securityToken);
            loginToken.setKeepMeLoggedIn(true);
            return loginToken;
        }
        return null;
    }

    public static void removeLoginToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(CUSTOMER_ID);
        editor.remove(SECURITY_TOKEN);
        editor.commit();
    }

    public static void setServerAddress(Context context, String address) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SERVER_ADDRESS, address);
        editor.commit();
    }

    public static String getServerAddress(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(SERVER_ADDRESS, DEFAULT_SERVER_ADDRESS);
    }

}
