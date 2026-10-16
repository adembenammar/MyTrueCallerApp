package adem.benammar.mytruecallerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccueilActivity extends AppCompatActivity {

    private TextView tvusername;
    private Button btnajout, btnaff, btnfermer, btndeconnecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        SessionManager session = new SessionManager(this);

        tvusername = findViewById(R.id.tv_user_acc);
        btnajout = findViewById(R.id.btn_ajout_acc);
        btnaff = findViewById(R.id.btn_affiche_acc);
        btnfermer = findViewById(R.id.btn_fermer_acc);
        btndeconnecter = findViewById(R.id.btn_deconnecter_acc);

        Intent x = this.getIntent();
        Bundle b = x.getExtras();

        String u = b != null ? b.getString("USER") : null;
        if (u == null) {
            u = session.getUser();
        }

        tvusername.setText("Accueil de M. " + u);

        btnajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccueilActivity.this, Ajout.class));
            }
        });

        btnaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccueilActivity.this, Affiche.class));
            }
        });

        // fermer l'application en restant connecté
        btnfermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        // oublier la session et revenir à l'authentification
        btndeconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.deconnecter();
                Intent i = new Intent(AccueilActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }
}
