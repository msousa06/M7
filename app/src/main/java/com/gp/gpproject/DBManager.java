package com.gp.gpproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import com.google.firebase.storage.FirebaseStorage;


public class DBManager extends SQLiteOpenHelper {

    //private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "m7database.db";
    private static final String DATABASE_PATH = "database/";



    public DBManager(Context context, String s, Object o, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBManager(Context context,String nome){
        super(context, nome, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS departamentos (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT NOT NULL UNIQUE, sigla TEXT NOT NULL UNIQUE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS categorias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome " +
                " TEXT UNIQUE NOT NULL);");


        db.execSQL("CREATE TABLE IF NOT EXISTS funcionarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nome  TEXT NOT NULL, " +
                " apelido TEXT NOT NULL, telefone TEXT, email TEXT NOT NULL UNIQUE, id_categoria INTEGER, FOREIGN KEY " +
                " (id_categoria) REFERENCES categorias (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docentes (id INTEGER PRIMARY KEY UNIQUE, pontos INTEGER DEFAULT 0, " +
                " id_departamento INTEGER NOT NULL, FOREIGN KEY (id) REFERENCES funcionarios (id) ON DELETE NO ACTION " +
                " ON UPDATE CASCADE, FOREIGN KEY (id_departamento) REFERENCES departamentos (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS disciplinas (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL UNIQUE, " +
                " sigla TEXT NOT NULL UNIQUE, id_departamento INTEGER NOT NULL , id_ruc INTEGER UNIQUE, FOREIGN KEY " +
                " (id_departamento) REFERENCES departamentos (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
                " FOREIGN KEY (id_ruc) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_disciplina (id INTEGER PRIMARY KEY AUTOINCREMENT, id_disciplina " +
                " INTEGER NOT NULL, id_docente INTEGER NOT NULL, CONSTRAINT unique_pair UNIQUE (id_disciplina, id_docente), " +
                " FOREIGN KEY (id_docente) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
                " FOREIGN KEY (id_disciplina) REFERENCES disciplinas (id) ON DELETE NO ACTION ON UPDATE CASCADE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS vigilancias (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, sala TEXT NOT NULL, " +
                " data TEXT NOT NULL, hora TEXT NOT NULL, id_vigilante INTEGER NOT NULL, id_disciplina INTEGER NOT NULL, pontuacao_vigilancia INTEGER NOT NULL DEFAULT 1,FOREIGN KEY (id_vigilante) " +
                " REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_vigilancia (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,id_vigilancia " +
                " INTEGER NOT NULL,id_docente INTEGER NOT NULL, esteve_presente TINYINT DEFAULT 0,justificacao TEXT, FOREIGN KEY " +
                " (id_docente) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE, FOREIGN KEY (id_vigilancia) " +
                " REFERENCES vigilancias (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS vigilancias_history (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, sala TEXT NOT NULL, " +
                " data INTEGER NOT NULL, id_ruc INTEGER NOT NULL,pontuacao_vigilancia INTEGER NOT NULL DEFAULT 1,FOREIGN KEY (id_ruc) " +
                " REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_vigilancia_history (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,id_vigilancia " +
                " INTEGER NOT NULL, id_docente INTEGER NOT NULL, esteve_presente TINYINT DEFAULT 0,justificacao TEXT, FOREIGN KEY " +
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

    public boolean insert_funcionario(String nome, String apelido, String telefone, String email, int categoria){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome",nome);
        contentValues.put("apelido", apelido);
        contentValues.put("telefone", telefone);
        contentValues.put("email", email);
        contentValues.put("id_categoria", categoria);
        if(this.getWritableDatabase().insertOrThrow("funcionarios","",contentValues) != -1){
            return true;
        } else {
            return false;
        }
    }

    public boolean insert_docente(String departamento, String nome, String apelido, String telefone, String email, String categoria){
        ContentValues contentValues = new ContentValues();

        insert_funcionario(nome, apelido, telefone, email, getIdCategoria(categoria));
        contentValues.put("id", getIdFuncionario(email));

        contentValues.put("pontos", 0);
        contentValues.put("id_departamento", getIdFromName("departamentos",departamento));
        if(this.getWritableDatabase().insertOrThrow("docentes","",contentValues) != -1){
            return true;
        } else {
            return false;
        }
    }

    public void list_all_docentes(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM docentes",null);
        textView.setText("");
        textView.append(Html.fromHtml("<b>" + "ID: \t\tNome: \t\tPontos:"));
        textView.append(Html.fromHtml("<b><br/>" + "Categoria: \t\tDepartamento: "));
        while (cursor.moveToNext()){
            textView.append("\n\n" + cursor.getString(0) + ",\t\t" + getNomeFuncionario(cursor.getString(0)) + " " + getApelidoFuncionario(cursor.getString(0)) + ",\t\t"+ cursor.getString(1) + ",\n" + getNomeCategoria(getCatFunc(cursor.getString(0))) + ",\t\t" + getNomeDepartamento(cursor.getString(2)));
        }
    }

    public void list_all_Vigilancias(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM vigilancias",null);
        textView.setText("");
        textView.append(Html.fromHtml("<b>" + "ID: \t\tSala: \t\tData:"));
        textView.append(Html.fromHtml("<b><br/>" + "Hora: \t\tDisciplina: \t\tVigilante: "));
        while (cursor.moveToNext()){
            textView.append("\n\n" + cursor.getString(0) + ",\t\t" + cursor.getString(1) + ",\t\t" + cursor.getString(2) + ",\n" + cursor.getString(3) + ",\t\t" + getNomeDisciplina(cursor.getString(5)) + ",\t\t" + getNomeFuncionario(cursor.getString(4)) + " " + getApelidoFuncionario(cursor.getString(4)));
        }
    }

    public int getIdFuncionario(String email){
        int id = -1;
        String query = "SELECT id FROM funcionarios WHERE email = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {email});
        if(c.moveToFirst()){
            id = c.getInt(c.getColumnIndex("id"));
        }
        return id;
    }


    public String getNomeFuncionario(String id){
        String nome = "";
        String query = "SELECT nome FROM funcionarios WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            nome = c.getString(c.getColumnIndex("nome"));
        }
        return nome;
    }

    public String getApelidoFuncionario(String id){
        String nome = "";
        String query = "SELECT apelido FROM funcionarios WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            nome = c.getString(c.getColumnIndex("apelido"));
        }
        return nome;
    }

    public List<String> getAllCategorias(){
        List<String> categorias = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM categorias";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                categorias.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categorias;
    }

    public boolean insert_categoria(String nome){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);

        if(this.getWritableDatabase().insertOrThrow("categorias","",contentValues) != -1){
            return true;
        } else {
            return false;
        }
    }

    public int getIdCategoria(String nome){
        int id = -1;
        String query = "SELECT id FROM categorias WHERE nome = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {nome});
        if(c.moveToFirst()){
            id = c.getInt(c.getColumnIndex("id"));
        }
        return id;
    }

    public String getNomeCategoria(String id){
        String nome = "";
        String query = "SELECT nome FROM categorias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            nome = c.getString(c.getColumnIndex("nome"));
        }
        return nome;
    }

    public String getCatFunc(String id){
        String cat = "";
        String query = "SELECT id_categoria FROM funcionarios WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            cat = c.getString(c.getColumnIndex("id_categoria"));
        }
        return cat;
    }

    public boolean insert_departamento(String nome,String sigla){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("sigla", sigla);

        if(this.getWritableDatabase().insertOrThrow("departamentos","",contentValues) != -1){
            return true;
        } else {
            return false;
        }
    }

    public String getIdFromName(String tablename, String name){
        String id = "";
        String query = "SELECT id FROM " + tablename + " WHERE nome = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {name});
        if(c.moveToFirst()){
            id = "" + c.getInt(c.getColumnIndex("id"));
        }
        return id;
    }

    public String getIdDepartamento(String id){
        String idaux = "";
        String query = "SELECT id_departamento FROM docentes WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getInt(c.getColumnIndex("id_departamento"));
        }
        return idaux;
    }

    public String getIdDisciplina(String id){
        String idaux = "";
        String query = "SELECT id_disciplina FROM vigilancias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getInt(c.getColumnIndex("id_disciplina"));
        }
        return idaux;
    }

    public String getNomeDepartamento(String id){
        String nome = "";
        String query = "SELECT nome FROM departamentos WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            nome = c.getString(c.getColumnIndex("nome"));
        }
        return nome;
    }

    public String getNomeDisciplina(String id){
        String nome = "";
        String query = "SELECT nome FROM disciplinas WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            nome = c.getString(c.getColumnIndex("nome"));
        }
        return nome;
    }

    public List<String> getAllDepartamentos(){
        List<String> departamentos = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM departamentos";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                departamentos.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return departamentos;
    }

    public void deleteAll(String table){
        this.getWritableDatabase().execSQL("delete from " + table);
        this.getWritableDatabase().execSQL("delete from  SQLITE_SEQUENCE  where name = ' " + table + "'");
    }

    public void deleteAllDB(){
        deleteAll("departamentos");
        deleteAll("categorias");
        deleteAll("funcionarios");
        deleteAll("docentes");
        deleteAll("disciplinas");
        deleteAll("docente_disciplina");
        deleteAll("vigilancias");
        deleteAll("docente_vigilancia");
        deleteAll("vigilancias_history");
        deleteAll("docente_vigilancia_history");
    }

    public boolean idExists(String table, String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + table + " where id =?";
        Cursor cursor = db.rawQuery(Query, new String[] {id});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public String getEmail(String id){
        String nome = "";
        String query = "SELECT email FROM funcionarios WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            nome = "" + c.getString(c.getColumnIndex("email"));
        }
        return nome;
    }

    public String getIdVigilante(String id){
        String idaux = "";
        String query = "SELECT id_vigilante FROM vigilancias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getInt(c.getColumnIndex("id_vigilante"));
        }
        return idaux;
    }

    public String getPontuacao(String id){
        String idaux = "";
        String query = "SELECT pontuacao_vigilancia FROM vigilancias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getInt(c.getColumnIndex("pontuacao_vigilancia"));
        }
        return idaux;
    }

    public String getData(String id){
        String idaux = "";
        String query = "SELECT data FROM vigilancias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getString(c.getColumnIndex("data"));
        }
        return idaux;
    }

    public String getHora(String id){
        String idaux = "";
        String query = "SELECT hora FROM vigilancias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getString(c.getColumnIndex("hora"));
        }
        return idaux;
    }

    public String getSala(String id){
        String idaux = "";
        String query = "SELECT sala FROM vigilancias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getString(c.getColumnIndex("sala"));
        }
        return idaux;
    }

    public String getTlm(String id){
        String nome = "";
        String query = "SELECT telefone FROM funcionarios WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            nome = "" + c.getInt(c.getColumnIndex("telefone"));
        }
        return nome;
    }

    public boolean updateDocente(String id, String departamento, String nome, String apelido, String telefone, String email, String categoria){
        updateFuncionario(id, nome, apelido, telefone, email, getIdCategoria(categoria));
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_departamento", getIdFromName("departamentos",departamento));

        if(this.getWritableDatabase().updateWithOnConflict("docentes", contentValues, "id = " + id,null,SQLiteDatabase.CONFLICT_ROLLBACK) != -1){
            return true;
        } else {
            return false;
        }
    }

    public boolean updateFuncionario(String id, String nome, String apelido, String telefone, String email, int categoria){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome",nome);
        contentValues.put("apelido", apelido);
        contentValues.put("telefone", telefone);
        contentValues.put("email", email);
        contentValues.put("id_categoria", categoria);

        if(this.getWritableDatabase().updateWithOnConflict("funcionarios", contentValues, "id = " + id,null,SQLiteDatabase.CONFLICT_ROLLBACK) != -1){
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(String tableName, String id){
        String sql = "SELECT id FROM " + tableName +" WHERE id=" + id;
        Cursor c = this.getWritableDatabase().rawQuery(sql,null);

        if(c.getCount()>0) {
            this.getWritableDatabase().execSQL("delete from " + tableName + " where id = ?", new String[] {id});
            return true;
        } else {
            return false;
        }
    }

    public List<String> getAllDisciplinas(){
        List<String> disciplinas = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM disciplinas";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                disciplinas.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return disciplinas;
    }

    public List<String> getAllDocentes(){
        List<String> docentes = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM funcionarios";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                docentes.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return docentes;
    }

    public boolean insert_disciplina(String nome, String sigla, String departamento, String emailRuc){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("sigla", sigla);
        contentValues.put("id_departamento", getIdFromName("departamentos", departamento));
        contentValues.put("id_ruc", getIdFuncionario(emailRuc));

        if(this.getWritableDatabase().insertOrThrow("disciplinas","",contentValues) != -1){
            return true;
        } else {
            return false;
        }
    }

    public boolean insert_vigilancia(String sala, String data, String hora, String emailVig, String disciplina, String pontuacao){
        ContentValues contentValues = new ContentValues();
        contentValues.put("sala", sala);
        contentValues.put("data", data);
        contentValues.put("hora", hora);
        contentValues.put("id_vigilante", getIdFuncionario(emailVig));
        contentValues.put("id_disciplina", getIdFromName("disciplinas", disciplina));
        contentValues.put("pontuacao_vigilancia", Integer.parseInt(pontuacao));

        if(this.getWritableDatabase().insertOrThrow("vigilancias","",contentValues) != -1){
            return true;
        } else {
            return false;
        }
    }

    public boolean updateVigilancia(String id, String sala, String data, String hora, String emailVig, String disciplina, int pontuacao){
        ContentValues contentValues = new ContentValues();
        contentValues.put("sala", sala);
        contentValues.put("data", data);
        contentValues.put("hora", hora);
        contentValues.put("id_vigilante", getIdFuncionario(emailVig));
        contentValues.put("id_disciplina", getIdFromName("disciplinas",disciplina));
        contentValues.put("pontuacao_vigilancia", pontuacao);

        if(this.getWritableDatabase().updateWithOnConflict("vigilancias", contentValues, "id = " + id,null,SQLiteDatabase.CONFLICT_ROLLBACK) != -1){
            return true;
        } else {
            return false;
        }
    }
}
