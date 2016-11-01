package ch.hsr.gadgeothek.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.util.SharedPreferencesHandler;

public class LibraryService {
    
    private static final String TAG = LibraryService.class.getSimpleName();
    private static LoginToken token;
    private static String serverUrl;

    public static void setServerAddress(String address) {
        Log.d(TAG, "Setting server to " + address);
        serverUrl = address;
    }

    public static boolean isLoggedIn() {
        return token != null;
    }
    public static boolean keepMeLoggedIn() {return token != null && token.getKeepMeLoggedIn(); }

    public static boolean handleAutomaticLogin(final Context context) {
        loadServerUrl(context);
        LoginToken tempLoginToken = SharedPreferencesHandler.getLoginToken(context);
        if (tempLoginToken != null) {
            token = tempLoginToken;
            return true;
        }
        return false;
    }

    private static void loadServerUrl(final Context context) {
        setServerAddress(SharedPreferencesHandler.getServerAddress(context));
    }

    public static void login(String mail, String password, final boolean keepMeLoggedIn, final Context context, final SimpleLibraryServiceCallback<Boolean> callback) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("email", mail);
        parameter.put("password", password);

        loadServerUrl(context);

        Request<LoginToken> request = new Request<>(HttpVerb.POST, serverUrl + "/login", LoginToken.class, parameter, new SimpleLibraryServiceCallback<LoginToken>() {
            @Override
            public void onCompletion(LoginToken input) {
                token = input;
                token.setKeepMeLoggedIn(keepMeLoggedIn);
                if (keepMeLoggedIn) {
                    SharedPreferencesHandler.addLoginToken(context, token);
                }
                callback.onCompletion(input != null && !input.getSecurityToken().isEmpty());
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        request.execute();
    }

    public static void logout(final Context context, final SimpleLibraryServiceCallback<Boolean> callback) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("token", getTokenAsString());

        SharedPreferencesHandler.removeLoginToken(context);

        Request<Boolean> request = new Request<>(HttpVerb.POST, serverUrl + "/logout", Boolean.class, parameter, new SimpleLibraryServiceCallback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                if (input) {
                    token = null;
                    //TODO remove token from shared preferences
                }
                callback.onCompletion(input);
            }

            @Override
            public void onError(String message) {
                token = null;
                callback.onError(message);
            }
        });
        request.execute();
    }

    public static void register(String mail, String password, String name, String studentenNumber, final Context context, final SimpleLibraryServiceCallback<Boolean> callback) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("email", mail);
        parameter.put("password", password);
        parameter.put("name", name);
        parameter.put("studentnumber", studentenNumber);

        loadServerUrl(context);

        Request<Boolean> request = new Request<>(HttpVerb.POST, serverUrl + "/register", Boolean.class, parameter, new SimpleLibraryServiceCallback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                callback.onCompletion(input);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        request.execute();
    }


    public static void getLoansForCustomer(final SimpleLibraryServiceCallback<List<Loan>> callback) {
        if (token == null) {
            throw new IllegalStateException("Not logged in");
        }
        HashMap<String, String> parameter = new HashMap<>();

        parameter.put("token", getTokenAsString());
        Request<List<Loan>> request = new Request<>(HttpVerb.GET, serverUrl + "/loans", new TypeToken<List<Loan>>() {
        }.getType(), parameter, new SimpleLibraryServiceCallback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                callback.onCompletion(input == null ? new ArrayList<Loan>() : input);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        request.execute();
    }

    public static void getReservationsForCustomer(final SimpleLibraryServiceCallback<List<Reservation>> callback) {
        if (token == null) {
            throw new IllegalStateException("Not logged in");
        }
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("token", getTokenAsString());

        Request<List<Reservation>> request = new Request<>(HttpVerb.GET, serverUrl + "/reservations", new TypeToken<List<Reservation>>() {
        }.getType(), parameter, new SimpleLibraryServiceCallback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                callback.onCompletion(input == null ? new ArrayList<Reservation>() : input);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        request.execute();
    }


    public static void reserveGadget(Gadget toReserve, final SimpleLibraryServiceCallback<Boolean> callback) {
        if (token == null) {
            throw new IllegalStateException("Not logged in");
        }
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("token", getTokenAsString());
        parameter.put("gadgetId", toReserve.getInventoryNumber());

        Request<Boolean> request = new Request<>(HttpVerb.POST, serverUrl + "/reservations", new TypeToken<Boolean>() {
        }.getType(), parameter, new SimpleLibraryServiceCallback<Boolean>() {
            @Override
            public void onCompletion(Boolean success) {
                callback.onCompletion(success);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        request.execute();
    }


    public static void deleteReservation(Reservation toDelete, final SimpleLibraryServiceCallback<Boolean> callback) {
        if (token == null) {
            throw new IllegalStateException("Not logged in");
        }
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("token", getTokenAsString());
        parameter.put("id", toDelete.getReservationId());
        Request<Boolean> request = new Request<>(HttpVerb.DELETE, serverUrl + "/reservations", Boolean.class, parameter, new SimpleLibraryServiceCallback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                callback.onCompletion(input);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        request.execute();
    }

    public static void getGadgets(final SimpleLibraryServiceCallback<List<Gadget>> callback) {
        if (token == null) {
            throw new IllegalStateException("Not logged in");
        }
        HashMap<String, String> parameter = new HashMap<>();

        parameter.put("token", getTokenAsString());
        Request<List<Gadget>> request = new Request<>(HttpVerb.GET, serverUrl + "/gadgets", new TypeToken<List<Gadget>>() {
        }.getType(), parameter, new SimpleLibraryServiceCallback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                callback.onCompletion(input);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        request.execute();
    }

    private static String getTokenAsString() {
        Gson gson = createGsonObject();
        return gson.toJson(token);
    }

    static Gson createGsonObject() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }

    private static class State {

        Boolean hasFailed = false;
        LibraryServiceCallback callback;
        List<Gadget> gadgets = null;
        List<Loan> loans = null;
        List<Reservation> reservations = null;
        State(LibraryServiceCallback callback){
            this.callback = callback;
        }
        void finish(){
            if(!hasFailed && loans != null && gadgets != null & reservations != null){
                callback.onCompletion(gadgets, loans, reservations);
            }
        }

        void fail(String message){
            if(!hasFailed){
                hasFailed = true;
                callback.onError(message);
            }
        }
    }

    public static void load(LibraryServiceCallback callback) {

        final State state = new State(callback);
        getGadgets(new SimpleLibraryServiceCallback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                state.gadgets = input;
                state.finish();
            }

            @Override
            public void onError(String message) {
                state.fail(message);
            }
        });
        getLoansForCustomer(new SimpleLibraryServiceCallback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                state.loans = input;
                state.finish();
            }

            @Override
            public void onError(String message) {
                state.fail(message);
            }
        });
        getReservationsForCustomer(new SimpleLibraryServiceCallback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                state.reservations = input;
                state.finish();
            }

            @Override
            public void onError(String message) {
                state.fail(message);
            }
        });
    }
}


