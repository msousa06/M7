package com.gp.gpproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.firebase.storage.FirebaseStorage;
import java.util.UUID;

public class DBManager extends SQLiteOpenHelper {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "m7database.db";
    private static final String DATABASE_PATH = "database/";



    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS departamentos (id TEXT PRIMARY KEY, " +
                " nome TEXT NOT NULL UNIQUE, sigla TEXT NOT NULL UNIQUE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS categorias (id TEXT PRIMARY KEY , nome " +
                " TEXT UNIQUE NOT NULL);");


        db.execSQL("CREATE TABLE IF NOT EXISTS funcionarios (id TEXT PRIMARY KEY , nome  TEXT NOT NULL, " +
                " apelido TEXT NOT NULL, telefone TEXT, email TEXT NOT NULL UNIQUE, id_categoria TEXT, FOREIGN KEY " +
                " (id_categoria) REFERENCES categorias (id) ON DELETE NO ACTION ON UPDATE CASCADE);");

        
        db.execSQL("CREATE TABLE IF NOT EXISTS docentes (id TEXT PRIMARY KEY UNIQUE, pontos INTEGER DEFAULT 0, " +
                " id_departamento TEXT NOT NULL, FOREIGN KEY (id) REFERENCES funcionarios (id) ON DELETE NO ACTION " +
                " ON UPDATE CASCADE, FOREIGN KEY (id_departamento) REFERENCES departamentos (id) ON DELETE NO ACTION ON UPDATE CASCADE);");
        
        
        db.execSQL("CREATE TABLE IF NOT EXISTS disciplinas (id TEXT PRIMARY KEY , nome TEXT NOT NULL UNIQUE, " +
                " sigla TEXT NOT NULL UNIQUE, id_departamento TEXT NOT NULL , id_ruc TEXT UNIQUE, FOREIGN KEY " +
                " (id_departamento) REFERENCES departamentos (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
                " FOREIGN KEY (id_ruc) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_disciplina (id TEXT PRIMARY KEY  , id_disciplina " +
                " TEXT NOT NULL, id_docente TEXT NOT NULL, CONSTRAINT unique_pair UNIQUE (id_disciplina, id_docente), " +
                " FOREIGN KEY (id_docente) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
                " FOREIGN KEY (id_disciplina) REFERENCES disciplinas (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS vigilancias (id TEXT NOT NULL PRIMARY KEY  , sala TEXT NOT NULL, " +
                " hora TEXT NOT NULL,id_ruc TEXT NOT NULL,pontuacao_vigilancia INTEGER NOT NULL DEFAULT 1,FOREIGN KEY (id_ruc) " +
                " REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_vigilancia (id TEXT NOT NULL PRIMARY KEY  ,id_vigilancia " +
                " TEXT NOT NULL,id_docente TEXT NOT NULL,  esteve_presente TINYINT DEFAULT 0,justificacao TEXT, FOREIGN KEY " +
                " (id_docente) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE, FOREIGN KEY (id_vigilancia) " +
                " REFERENCES vigilancias (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS vigilancias_history (id TEXT NOT NULL PRIMARY KEY  , sala TEXT NOT NULL, " +
                " hora TEXT NOT NULL,id_ruc TEXT NOT NULL,pontuacao_vigilancia INTEGER NOT NULL DEFAULT 1,FOREIGN KEY (id_ruc) " +
                " REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_vigilancia_history (id TEXT NOT NULL PRIMARY KEY  ,id_vigilancia " +
                " TEXT NOT NULL,id_docente TEXT NOT NULL, esteve_presente TINYINT DEFAULT 0,justificacao TEXT, FOREIGN KEY " +
                " (id_docente) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE, FOREIGN KEY (id_vigilancia) " +
                " REFERENCES vigilancias (id) ON DELETE NO ACTION ON UPDATE CASCADE);");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS departamentos");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL("DROP TABLE IF EXISTS funcionarios");
        db.execSQL("DROP TABLE IF EXISTS docentes");
        db.execSQL("DROP TABLE IF EXISTS disciplinas");
        db.execSQL("DROP TABLE IF EXISTS docente_disciplina");
        db.execSQL("DROP TABLE IF EXISTS vigilancias");
        db.execSQL("DROP TABLE IF EXISTS docente_vigilancia");
        db.execSQL("DROP TABLE IF EXISTS vigilancias_history");
        db.execSQL("DROP TABLE IF EXISTS docente_vigilancia_history");
        onCreate(db);
    }


    public long createData(DBManager dbManager, String nome) {
        ContentValues values = new ContentValues();
        String uniqueID = UUID.randomUUID().toString(); //creating unique index
        values.put("id", uniqueID);
        values.put("nome", nome);
        SQLiteDatabase db = dbManager.getWritableDatabase();
        long result = db.insert("categorias", null, values);
        db.close();
        return result;
    }


    /*
        db.execSQL("CREATE TABLE parent (parentID INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT, email TEXT);");
    *
    *  Cada operação do (C)RUD var ter a seguinte ideologia:
    *
    public long addParent(DBManager dbManager, String name, String email) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        SQLiteDatabase db = dbManager.getWritableDatabase();
        long result = db.insert("parent", null, values);
        db.close();
        return result;
    }


    Cada operação do C(R)UD var ter a seguinte ideologia:

    public Cursor getParent(DBManager dbManager, String email) {

        SQLiteDatabase db = dbManager.getReadableDatabase();
        String query = "SELECT parentID FROM parent WHERE email = '" + email + "';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            return null;
        } else {
            return cursor;
        }
    }

    *
    * */
}
