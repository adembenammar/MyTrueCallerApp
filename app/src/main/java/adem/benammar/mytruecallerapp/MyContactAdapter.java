package adem.benammar.mytruecallerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.ContactHolder> {

    Context con;
    ArrayList<Contact> data;

    public MyContactAdapter(Context con, ArrayList<Contact> data) {
        this.con = con;
        this.data = data;
    }

    /** Remplace la liste affichée (résultat du filtre de recherche). */
    public void setData(ArrayList<Contact> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /** Conserve les références des vues d'une ligne (évite les findViewById répétés). */
    public static class ContactHolder extends RecyclerView.ViewHolder {
        TextView tvnom, tvpseudo, tvnumero;
        ImageView imgCall, imgEdit, imgDelete;

        public ContactHolder(@NonNull View v) {
            super(v);
            tvnom = v.findViewById(R.id.tv_nom_contact);
            tvpseudo = v.findViewById(R.id.tv_prenom_contact);
            tvnumero = v.findViewById(R.id.tv_num_contact);
            imgCall = v.findViewById(R.id.imageViewCall_Contact);
            imgEdit = v.findViewById(R.id.imageViewEdit_Contact);
            imgDelete = v.findViewById(R.id.imageViewDelete_Contact);
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // parsing xml code to java : convertir
        View v = LayoutInflater.from(con).inflate(R.layout.view_contact, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        //affectation
        Contact c = data.get(position);
        holder.tvnom.setText(c.nom);
        holder.tvpseudo.setText(c.pseudo);
        holder.tvnumero.setText(c.numero);

        //Evenement
        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // appel direct : la demande de permission est gérée par l'activité
                if (con instanceof Affiche) {
                    ((Affiche) con).appeler(c.numero);
                }
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, Ajout.class);
                i.putExtra("ID", c.id);
                con.startActivity(i);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(con);
                alert.setTitle("suppression");
                alert.setMessage("Etes vous sur de vouloir supprimer le contact ");
                alert.setPositiveButton("confirmer", (dialog, which) -> {
                    DBHelper db = new DBHelper(con);
                    db.supprimer(c.id);
                    db.close();
                    if (con instanceof Affiche) {
                        ((Affiche) con).charger();
                    }
                });
                alert.setNegativeButton("non", null);
                alert.setNeutralButton("annuler", null);
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // nombre de vues à créer
        return data.size();
    }
}
