# MyTrueCaller

Application Android de gestion de contacts : connexion, ajout, recherche, appel, modification et suppression de contacts, stockés localement dans une base SQLite.

## Fonctionnalités

- **Écran de démarrage** avec redirection automatique : vers l'accueil si une session est ouverte, sinon vers la connexion.
- **Connexion** par nom d'utilisateur et mot de passe, avec session mémorisée entre deux lancements.
- **Accueil** : salutation, nombre de contacts, accès rapide à « Ajouter » et « Mes contacts », liste des 3 derniers contacts ajoutés.
- **Mes contacts** : liste triée par nom, recherche instantanée (nom, prénom ou numéro), avatars colorés avec initiales, message quand la liste est vide.
- **Appel direct** depuis la liste (la permission `CALL_PHONE` est demandée au premier appel).
- **Feuille de contact** (en bas de l'écran) pour appeler, modifier ou supprimer un contact, avec confirmation avant suppression.
- **Ajout de contact** avec champs obligatoires et avatar qui affiche les initiales pendant la saisie.
- **Thème toujours sombre**, quel que soit le réglage du téléphone.

## Design

Thème Material 3 **sombre premium** : fond graphite (`#0B0D10`), surfaces `#15181D`, accent vert émeraude (`#10B981`) et touche dorée (`#E8C468`) :

- en-tête graphite arrondi avec halo émeraude sur Splash, Connexion et Accueil ;
- boutons principaux, tuile « Ajouter » et bouton flottant en émeraude, texte foncé ;
- cartes sans ombre bordées d'un trait fin, avatars aux teintes sobres, icônes vectorielles au trait ;
- affichage bord à bord (le contenu passe sous les barres système).

Les couleurs sont dans `res/values/colors.xml`, les styles des composants dans `res/values/styles.xml`.

## Prérequis

- Android Studio (version récente, compatible AGP 9.4)
- JDK 11 ou plus
- Appareil ou émulateur sous **Android 7.0 (API 24)** minimum ; SDK cible : API 37

## Installation et lancement

```bash
git clone https://github.com/adembenammar/MyTrueCallerApp.git
```

Ouvrez le dossier dans Android Studio, attendez la synchronisation Gradle, puis lancez la configuration **app** sur un émulateur ou un téléphone.

En ligne de commande :

```bash
./gradlew assembleDebug
```

L'APK est généré dans `app/build/outputs/apk/debug/app-debug.apk`.

## Identifiants de test

| Nom d'utilisateur | Mot de passe |
|---|---|
| `azer` | `111` |

Ces identifiants sont codés en dur dans `MainActivity` : ils servent uniquement à la démonstration.

## Structure du projet

```
app/src/main/
├── java/adem/benammar/mytruecallerapp/
│   ├── SplashActivity.java            Écran de démarrage
│   ├── MainActivity.java              Connexion
│   ├── AccueilActivity.java           Accueil (tuiles, compteur, récents)
│   ├── Affiche.java                   Liste et recherche des contacts, appels
│   ├── Ajout.java                     Ajout d'un contact
│   ├── MyRecyclerContactAdapter.java  Lignes de la liste + feuille de contact
│   ├── MyContactAdapter.java          Ancien adaptateur (non utilisé)
│   ├── Contact.java                   Modèle (id, nom, prénom, numéro)
│   ├── DBHelper.java                  Base SQLite (contacts.db, table contact)
│   ├── SessionManager.java            Session de l'utilisateur connecté
│   └── Ecran.java                     Bord à bord, initiales, couleurs d'avatar
└── res/
    ├── layout/     Écrans, ligne de contact, feuille de contact
    ├── drawable/   Fonds, formes et icônes vectorielles
    ├── color/      Sélecteur de contour des champs
    └── values/     Couleurs, thème, styles
```

## Données

Les contacts sont enregistrés dans la base SQLite locale `contacts.db`, table `contact` (`id`, `nom`, `pseudo` pour le prénom, `numero`). Rien n'est envoyé sur Internet.

## Auteur

Adem Ben Ammar
