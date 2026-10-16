package adem.benammar.mytruecallerapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/** Petits utilitaires d'affichage partagés par les écrans. */
public final class Ecran {

    private static final int[][] AVATARS = {
            {R.color.avatar_blue_bg, R.color.avatar_blue_fg},
            {R.color.avatar_amber_bg, R.color.avatar_amber_fg},
            {R.color.avatar_violet_bg, R.color.avatar_violet_fg},
            {R.color.avatar_green_bg, R.color.avatar_green_fg},
    };

    private Ecran() {
    }

    /**
     * Affichage bord à bord : le contenu passe sous les barres système.
     * L'en-tête (s'il existe) s'étend sous la barre d'état, sinon c'est la racine qui se décale.
     * fondSombre : true si le haut de l'écran est bleu nuit (icônes de barre d'état claires).
     */
    public static void appliquer(Activity a, View racine, View entete, boolean fondSombre) {
        WindowCompat.setDecorFitsSystemWindows(a.getWindow(), false);

        boolean nuit = (a.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        WindowInsetsControllerCompat ctrl =
                WindowCompat.getInsetsController(a.getWindow(), a.getWindow().getDecorView());
        ctrl.setAppearanceLightStatusBars(!fondSombre && !nuit);
        ctrl.setAppearanceLightNavigationBars(!nuit);

        final int haut = racine.getPaddingTop();
        final int bas = racine.getPaddingBottom();
        final int enteteHaut = entete != null ? entete.getPaddingTop() : 0;

        ViewCompat.setOnApplyWindowInsetsListener(racine, (v, insets) -> {
            Insets barres = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets clavier = insets.getInsets(WindowInsetsCompat.Type.ime());

            if (entete != null) {
                entete.setPadding(entete.getPaddingLeft(), enteteHaut + barres.top,
                        entete.getPaddingRight(), entete.getPaddingBottom());
                v.setPadding(barres.left, haut, barres.right, bas + Math.max(barres.bottom, clavier.bottom));
            } else {
                v.setPadding(barres.left, haut + barres.top, barres.right,
                        bas + Math.max(barres.bottom, clavier.bottom));
            }
            return insets;
        });
    }

    /** Initiales "NP" à partir du nom et du prénom. */
    public static String initiales(String nom, String prenom) {
        StringBuilder s = new StringBuilder();
        if (nom != null && !nom.trim().isEmpty()) s.append(nom.trim().charAt(0));
        if (prenom != null && !prenom.trim().isEmpty()) s.append(prenom.trim().charAt(0));
        return s.length() == 0 ? "?" : s.toString().toUpperCase();
    }

    /** Couleurs {fond, texte} d'un avatar, stables pour un même contact. */
    public static int[] couleursAvatar(Context con, int cle) {
        int[] paire = AVATARS[Math.abs(cle) % AVATARS.length];
        return new int[]{
                ContextCompat.getColor(con, paire[0]),
                ContextCompat.getColor(con, paire[1])
        };
    }
}
