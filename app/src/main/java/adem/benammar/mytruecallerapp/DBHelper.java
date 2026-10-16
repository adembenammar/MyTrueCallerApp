package adem.benammar.mytruecallerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contacts.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE = "contact";
    private static final String COL_ID = "id";
    private static final String COL_NOM = "nom";
    private static final String COL_PSEUDO = "pseudo";
    private static final String COL_NUMERO = "numero";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOM + " TEXT, " +
                COL_PSEUDO + " TEXT, " +
                COL_NUMERO + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues valeurs(Contact c) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NOM, c.nom);
        cv.put(COL_PSEUDO, c.pseudo);
        cv.put(COL_NUMERO, c.numero);
        return cv;
    }

    public long ajouter(Contact c) {
        return getWritableDatabase().insert(TABLE, null, valeurs(c));
    }

    public int modifier(Contact c) {
        return getWritableDatabase().update(TABLE, valeurs(c),
                COL_ID + "=?", new String[]{String.valueOf(c.id)});
    }

    public int supprimer(int id) {
        return getWritableDatabase().delete(TABLE,
                COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Contact getParId(int id) {
        Contact c = null;
        Cursor cur = getReadableDatabase().query(TABLE, null,
                COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cur.moveToFirst()) {
            c = lire(cur);
        }
        cur.close();
        return c;
    }

    public ArrayList<Contact> tous() {
        ArrayList<Contact> liste = new ArrayList<>();
        Cursor cur = getReadableDatabase().query(TABLE, null, null, null,
                null, null, COL_NOM + " COLLATE NOCASE ASC");
        while (cur.moveToNext()) {
            liste.add(lire(cur));
        }
        cur.close();
        return liste;
    }

    private Contact lire(Cursor cur) {
        return new Contact(
                cur.getInt(cur.getColumnIndexOrThrow(COL_ID)),
                cur.getString(cur.getColumnIndexOrThrow(COL_NOM)),
                cur.getString(cur.getColumnIndexOrThrow(COL_PSEUDO)),
                cur.getString(cur.getColumnIndexOrThrow(COL_NUMERO)));
    }
}
