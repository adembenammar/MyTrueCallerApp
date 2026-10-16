package adem.benammar.mytruecallerapp;

public class Contact {
    int id; // clé primaire SQLite (-1 tant que le contact n'est pas enregistré)
    String nom,pseudo,numero;

    public Contact(int id, String nom, String pseudo, String numero) {
        this.id = id;
        this.nom = nom;
        this.pseudo = pseudo;
        this.numero = numero;
    }

    public Contact(String nom, String pseudo, String numero) {
        this(-1, nom, pseudo, numero);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", numero='" + numero + '\'' +
                '}';
    }
}
