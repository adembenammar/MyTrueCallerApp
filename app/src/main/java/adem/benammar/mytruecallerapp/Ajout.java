package adem.benammar.mytruecallerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        db = new DBHelper(this);

        ednom = findViewById(R.id.ed_nom_ajout);
        edpseudo = findViewById(R.id.ed_prenom_ajout);
        ednumero = findViewById(R.id.ed_numero_ajout);

        btnannuler = findViewById(R.id.btn_quitter_ajout);
        btnvalider = findViewById(R.id.btn_valider_ajout);

        btnannuler.setOnClickListener(v -> finish());

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

                Toast.makeText(Ajout.this, "done", Toast.LENGTH_SHORT).show();
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
