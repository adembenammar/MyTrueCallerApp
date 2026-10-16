package adem.benammar.mytruecallerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerContactAdapter extends RecyclerView.Adapter<MyRecyclerContactAdapter.MyViewHolder> {
    Context con;
    ArrayList<Contact> data;

    public MyRecyclerContactAdapter(Context con, ArrayList<Contact> data) {
        this.con = con;
        this.data = data;
    }

    /** Remplace la liste affichée (résultat du filtre de recherche). */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Contact> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // convertir le fichier xml d'une ligne en vue java
        View v = LayoutInflater.from(con).inflate(R.layout.view_contact, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //affectation
        Contact c = data.get(position);
        holder.tvinitiale.setText(c.nom.isEmpty() ? "?" : c.nom.substring(0, 1).toUpperCase());
        holder.tvnom.setText(c.nom);
        holder.tvpseudo.setText(c.pseudo);
        holder.tvnumero.setText(c.numero);

        //Evenement
        holder.imgCall.setOnClickListener(view -> {
            // appel direct : la demande de permission est gérée par l'activité
            if (con instanceof Affiche) {
                ((Affiche) con).appeler(c.numero);
            }
        });

        holder.imgEdit.setOnClickListener(view -> afficherDialogModification(c));

        holder.imgDelete.setOnClickListener(view -> {
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
        });
    }

    /** Boîte de dialogue de modification */
    private void afficherDialogModification(final Contact c) {
        View vue = LayoutInflater.from(con).inflate(R.layout.view_dialog, null);

        final EditText ednom = vue.findViewById(R.id.ed_nom_dialog);
        final EditText edprenom = vue.findViewById(R.id.ed_prenom_dialog);
        final EditText ednumero = vue.findViewById(R.id.ed_numero_dialog);

        ednom.setText(c.nom);
        edprenom.setText(c.pseudo);
        ednumero.setText(c.numero);

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("Modification");
        builder.setView(vue);

        builder.setPositiveButton("Valider", (dialog, which) -> {
            c.nom = ednom.getText().toString();
            c.pseudo = edprenom.getText().toString();
            c.numero = ednumero.getText().toString();

            DBHelper db = new DBHelper(con);
            db.modifier(c);
            db.close();

            if (con instanceof Affiche) {
                ((Affiche) con).charger();
            }
        });

        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvinitiale, tvnom, tvpseudo, tvnumero;
        ImageView imgCall, imgEdit, imgDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvinitiale = itemView.findViewById(R.id.tv_initiale_contact);
            tvnom = itemView.findViewById(R.id.tv_nom_contact);
            tvpseudo = itemView.findViewById(R.id.tv_prenom_contact);
            tvnumero = itemView.findViewById(R.id.tv_num_contact);
            imgCall = itemView.findViewById(R.id.imageViewCall_Contact);
            imgEdit = itemView.findViewById(R.id.imageViewEdit_Contact);
            imgDelete = itemView.findViewById(R.id.imageViewDelete_Contact);
        }
    }
}
