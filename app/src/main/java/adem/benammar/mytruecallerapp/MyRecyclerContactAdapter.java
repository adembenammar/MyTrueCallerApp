package adem.benammar.mytruecallerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
        int[] couleurs = Ecran.couleursAvatar(con, c.id);
        holder.tvinitiale.setText(Ecran.initiales(c.nom, c.pseudo));
        holder.tvinitiale.setBackgroundTintList(ColorStateList.valueOf(couleurs[0]));
        holder.tvinitiale.setTextColor(couleurs[1]);
        holder.tvnom.setText(c.nom);
        holder.tvpseudo.setText(c.pseudo);
        holder.tvnumero.setText(c.numero);

        //Evenement
        holder.imgCall.setOnClickListener(view -> appeler(c));
        holder.itemView.findViewById(R.id.card_contact).setOnClickListener(view -> afficherFeuille(c));
        holder.imgEdit.setOnClickListener(view -> afficherFeuille(c));
        holder.imgDelete.setOnClickListener(view -> confirmerSuppression(c, null));
    }

    private void appeler(Contact c) {
        // appel direct : la demande de permission est gérée par l'activité
        if (con instanceof Affiche) {
            ((Affiche) con).appeler(c.numero);
        }
    }

    private void rafraichir() {
        if (con instanceof Affiche) {
            ((Affiche) con).charger();
        }
    }

    private void confirmerSuppression(final Contact c, final BottomSheetDialog feuille) {
        new MaterialAlertDialogBuilder(con)
                .setTitle("Supprimer ce contact ?")
                .setMessage(c.nom + " " + c.pseudo + " sera définitivement supprimé.")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    DBHelper db = new DBHelper(con);
                    db.supprimer(c.id);
                    db.close();
                    if (feuille != null) {
                        feuille.dismiss();
                    }
                    rafraichir();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    /** Feuille du bas : détail du contact, appel, suppression et modification */
    private void afficherFeuille(final Contact c) {
        View vue = LayoutInflater.from(con).inflate(R.layout.view_dialog, null);
        final BottomSheetDialog feuille = new BottomSheetDialog(con);
        feuille.setContentView(vue);
        feuille.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        feuille.getBehavior().setSkipCollapsed(true);

        TextView ini = vue.findViewById(R.id.tv_initiale_dialog);
        int[] couleurs = Ecran.couleursAvatar(con, c.id);
        ini.setText(Ecran.initiales(c.nom, c.pseudo));
        ini.setBackgroundTintList(ColorStateList.valueOf(couleurs[0]));
        ini.setTextColor(couleurs[1]);
        ((TextView) vue.findViewById(R.id.tv_titre_dialog)).setText(c.nom + " " + c.pseudo);
        ((TextView) vue.findViewById(R.id.tv_sous_titre_dialog)).setText(c.numero);

        final EditText ednom = vue.findViewById(R.id.ed_nom_dialog);
        final EditText edprenom = vue.findViewById(R.id.ed_prenom_dialog);
        final EditText ednumero = vue.findViewById(R.id.ed_numero_dialog);

        ednom.setText(c.nom);
        edprenom.setText(c.pseudo);
        ednumero.setText(c.numero);

        vue.findViewById(R.id.btn_fermer_dialog).setOnClickListener(v -> feuille.dismiss());
        vue.findViewById(R.id.btn_appeler_dialog).setOnClickListener(v -> appeler(c));
        vue.findViewById(R.id.btn_supprimer_dialog).setOnClickListener(v -> confirmerSuppression(c, feuille));

        vue.findViewById(R.id.btn_enregistrer_dialog).setOnClickListener(v -> {
            c.nom = ednom.getText().toString().trim();
            c.pseudo = edprenom.getText().toString().trim();
            c.numero = ednumero.getText().toString().trim();

            DBHelper db = new DBHelper(con);
            db.modifier(c);
            db.close();

            feuille.dismiss();
            rafraichir();
        });

        feuille.show();
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
