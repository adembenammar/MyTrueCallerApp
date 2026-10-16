
package adem.benammar.mytruecallerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class Affiche extends AppCompatActivity {

    private static final int REQ_CALL = 100;

    RecyclerView rvaffiche;
    EditText edrecherche;
    TextView tvvide;
    MyRecyclerContactAdapter ad;
    DBHelper db;
    String numeroEnAttente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche);

        db = new DBHelper(this);

        rvaffiche = findViewById(R.id.rv_affiche);
        edrecherche = findViewById(R.id.ed_recherche);
        tvvide = findViewById(R.id.tv_vide);

        findViewById(R.id.fab_ajout).setOnClickListener(v ->
                startActivity(new Intent(this, Ajout.class)));

        ad = new MyRecyclerContactAdapter(this, new ArrayList<Contact>());
        rvaffiche.setLayoutManager(new GridLayoutManager(this, 2));
        rvaffiche.setAdapter(ad);

        edrecherche.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charger();
            }

            public void afterTextChanged(android.text.Editable s) {}
        });

        charger();
    }

    void charger() {
        String q = edrecherche.getText().toString().toLowerCase(Locale.getDefault()).trim();
        ArrayList<Contact> resultat = new ArrayList<>();

        for (Contact c : db.tous()) {
            if (c.nom.toLowerCase(Locale.getDefault()).contains(q)
                    || c.pseudo.toLowerCase(Locale.getDefault()).contains(q)
                    || c.numero.contains(q)) {
                resultat.add(c);
            }
        }

        ad.setData(resultat);
        tvvide.setVisibility(resultat.isEmpty() ? View.VISIBLE : View.GONE);
        tvvide.setText(q.isEmpty()
                ? "Aucun contact\nAppuyez sur + pour en ajouter"
                : "Aucun résultat pour cette recherche");
    }

    void appeler(String numero) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            lancerAppel(numero);
        } else {
            numeroEnAttente = numero;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQ_CALL);
        }
    }

    private void lancerAppel(String numero) {
        try {
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:" + Uri.encode(numero)));
            startActivity(i);
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CALL) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && numeroEnAttente != null) {
                lancerAppel(numeroEnAttente);
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
            }

            numeroEnAttente = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ad != null) {
            charger();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
