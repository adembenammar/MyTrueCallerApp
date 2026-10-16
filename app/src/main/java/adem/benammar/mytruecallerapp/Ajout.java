package adem.benammar.mytruecallerapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ajout extends AppCompatActivity {

    EditText ednom, edpseudo, ednumero;
    Button btnvalider, btnannuler;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ajout);
        Ecran.appliquer(this, findViewById(R.id.main), null, false);

        db = new DBHelper(this);

        ednom = findViewById(R.id.ed_nom_ajout);
        edpseudo = findViewById(R.id.ed_prenom_ajout);
        ednumero = findViewById(R.id.ed_numero_ajout);

        btnannuler = findViewById(R.id.btn_quitter_ajout);
        btnvalider = findViewById(R.id.btn_valider_ajout);

        btnannuler.setOnClickListener(v -> finish());
        findViewById(R.id.btn_retour_ajout).setOnClickListener(v -> finish());

        // l'avatar affiche les initiales au fil de la saisie
        TextView tvavatar = findViewById(R.id.tv_avatar_ajout);
        View ivavatar = findViewById(R.id.iv_avatar_ajout);
        TextWatcher majAvatar = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                String ini = Ecran.initiales(ednom.getText().toString(), edpseudo.getText().toString());
                boolean vide = ini.equals("?");
                tvavatar.setText(ini);
                tvavatar.setVisibility(vide ? View.GONE : View.VISIBLE);
                ivavatar.setVisibility(vide ? View.VISIBLE : View.GONE);
            }
        };
        ednom.addTextChangedListener(majAvatar);
        edpseudo.addTextChangedListener(majAvatar);

        btnvalider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String nom = ednom.getText().toString().trim();
                String pseudo = edpseudo.getText().toString().trim();
                String numero = ednumero.getText().toString().trim();

                // vérification des champs obligatoires
                if (nom.isEmpty()) {
                    ednom.setError("Nom obligatoire");
                    ednom.requestFocus();
                    return;
                }
                if (pseudo.isEmpty()) {
                    edpseudo.setError("Prénom obligatoire");
                    edpseudo.requestFocus();
                    return;
                }
                if (numero.isEmpty()) {
                    ednumero.setError("Numéro obligatoire");
                    ednumero.requestFocus();
                    return;
                }

                db.ajouter(new Contact(nom, pseudo, numero));

                Toast.makeText(Ajout.this, "Contact enregistré", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
