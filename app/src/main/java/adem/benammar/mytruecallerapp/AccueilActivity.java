package adem.benammar.mytruecallerapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class AccueilActivity extends AppCompatActivity {

    private static final int NB_RECENTS = 3;

    private TextView tvusername, tvavatar, tvcount;
    private View btnajout, btnaff, btnfermer, btndeconnecter;
    private LinearLayout sectionRecents, llRecents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Ecran.appliquer(this, findViewById(R.id.main), findViewById(R.id.header), true);

        SessionManager session = new SessionManager(this);

        tvusername = findViewById(R.id.tv_user_acc);
        tvavatar = findViewById(R.id.tv_avatar_acc);
        tvcount = findViewById(R.id.tv_count_acc);
        btnajout = findViewById(R.id.btn_ajout_acc);
        btnaff = findViewById(R.id.btn_affiche_acc);
        btnfermer = findViewById(R.id.btn_fermer_acc);
        btndeconnecter = findViewById(R.id.btn_deconnecter_acc);
        sectionRecents = findViewById(R.id.section_recents_acc);
        llRecents = findViewById(R.id.ll_recents_acc);

        Intent x = this.getIntent();
        Bundle b = x.getExtras();

        String u = b != null ? b.getString("USER") : null;
        if (u == null) {
            u = session.getUser();
        }
        if (u == null) {
            u = "";
        }

        tvusername.setText(u);
        tvavatar.setText(u.isEmpty() ? "?" : u.substring(0, 1).toUpperCase());

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

        findViewById(R.id.tv_tout_voir_acc).setOnClickListener(v ->
                startActivity(new Intent(this, Affiche.class)));

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

    @Override
    protected void onResume() {
        super.onResume();
        chargerResume();
    }

    /** Nombre de contacts et derniers contacts ajoutés (id le plus grand). */
    private void chargerResume() {
        DBHelper db = new DBHelper(this);
        ArrayList<Contact> tous = db.tous();
        db.close();

        int n = tous.size();
        tvcount.setText(n + (n > 1 ? " contacts enregistrés" : " contact enregistré"));

        Collections.sort(tous, (a, b) -> Integer.compare(b.id, a.id));
        llRecents.removeAllViews();
        LayoutInflater inf = LayoutInflater.from(this);

        for (int k = 0; k < Math.min(NB_RECENTS, n); k++) {
            Contact c = tous.get(k);
            View ligne = inf.inflate(R.layout.item_recent, llRecents, false);

            TextView ini = ligne.findViewById(R.id.tv_initiale_recent);
            int[] couleurs = Ecran.couleursAvatar(this, c.id);
            ini.setText(Ecran.initiales(c.nom, c.pseudo));
            ini.setBackgroundTintList(ColorStateList.valueOf(couleurs[0]));
            ini.setTextColor(couleurs[1]);

            ((TextView) ligne.findViewById(R.id.tv_nom_recent)).setText(c.nom + " " + c.pseudo);
            ((TextView) ligne.findViewById(R.id.tv_num_recent)).setText(c.numero);

            ligne.setOnClickListener(v -> startActivity(new Intent(this, Affiche.class)));
            // ouvre le composeur (pas besoin de la permission d'appel ici)
            ligne.findViewById(R.id.btn_appel_recent).setOnClickListener(v ->
                    startActivity(new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + Uri.encode(c.numero)))));

            llRecents.addView(ligne);
        }

        sectionRecents.setVisibility(n > 0 ? View.VISIBLE : View.GONE);
    }
}
