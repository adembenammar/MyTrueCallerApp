package adem.benammar.mytruecallerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int DELAI_MS = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Ecran.appliquer(this, findViewById(R.id.main), null, true);

        // attendre 2,5 s puis passer à l'authentification
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionManager session = new SessionManager(SplashActivity.this);
                Intent i;
                if (session.estConnecte()) {
                    // déjà connecté : direct à l'accueil
                    i = new Intent(SplashActivity.this, AccueilActivity.class);
                    i.putExtra("USER", session.getUser());
                } else {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(i);
                finish(); // empêche de revenir au splash avec le bouton retour
            }
        }, DELAI_MS);
    }
}
