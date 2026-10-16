package adem.benammar.mytruecallerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btnval,btnquitter;
    EditText edmail,edpws;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // mettre un fichier xml sur lecran
        setContentView(R.layout.activity_main);

        // récupération des composants
        edmail=findViewById(R.id.ed_user_auth);
        edpws=findViewById(R.id.ed_pwd_auth);
        btnval=findViewById(R.id.btn_valider_auth);
        btnquitter=findViewById(R.id.btn_quitter_auth);

        //evenement
        btnquitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //fermer activiter
            }
        });

        btnval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edmail.getText().toString();
                String pws=edpws.getText().toString();
                if(email.equals("azer") && pws.equals("111")){
                    // mémoriser la connexion pour les prochains lancements
                    new SessionManager(MainActivity.this).connecter(email);

                    //passage vers activiter accuiel
                    Intent i = new Intent( MainActivity.this,AccueilActivity.class);
                    i.putExtra("USER",email);
                    startActivity(i);
                    finish();


                }
                else {
                    // message erreur
                    Toast.makeText(MainActivity.this,
                            "VERIFIER LES INFOS ",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}