package adem.benammar.mytruecallerapp;

import android.content.Context;
import android.content.SharedPreferences;

/** Mémorise l'utilisateur connecté (SharedPreferences) pour rester connecté. */
public class SessionManager {

    private static final String PREFS = "session";
    private static final String KEY_USER = "user";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void connecter(String user) {
        prefs.edit().putString(KEY_USER, user).apply();
    }

    public void deconnecter() {
        prefs.edit().remove(KEY_USER).apply();
    }

    public boolean estConnecte() {
        return prefs.getString(KEY_USER, null) != null;
    }

    public String getUser() {
        return prefs.getString(KEY_USER, "");
    }
}
