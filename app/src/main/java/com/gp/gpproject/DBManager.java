package com.gp.gpproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import com.google.firebase.storage.FirebaseStorage;


public class DBManager extends SQLiteOpenHelper {

    //private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "m7database.db";



    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS role (id INTEGER PRIMARY " +
                "KEY AUTOINCREMENT, nome TEXT NOT NULL UNIQUE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS departamentos (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT NOT NULL UNIQUE, sigla TEXT NOT NULL UNIQUE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS categorias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome " +
                " TEXT UNIQUE NOT NULL);");





        db.execSQL("CREATE TABLE IF NOT EXISTS funcionarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nome  TEXT NOT NULL, " +
                " apelido TEXT NOT NULL, telefone TEXT, email TEXT NOT NULL UNIQUE, id_categoria INTEGER, id_role INTEGER," +
                " FOREIGN KEY (id_categoria) REFERENCES categorias (id) ON DELETE NO ACTION ON UPDATE CASCADE, " +
                " FOREIGN KEY (id_role) REFERENCES role (id) ON DELETE NO ACTION ON UPDATE CASCADE); ");










        db.execSQL("CREATE TABLE IF NOT EXISTS docentes (id INTEGER PRIMARY KEY UNIQUE, pontos INTEGER DEFAULT 0, " +
                " id_departamento INTEGER NOT NULL, tem_cargo_gestao TINYINT DEFAULT 0, FOREIGN KEY (id) REFERENCES funcionarios (id) ON DELETE NO ACTION " +
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
                " data TEXT NOT NULL, hora TEXT NOT NULL, id_ruc INTEGER NOT NULL, id_disciplina INTEGER NOT NULL," +
                " pontuacao_vigilancia INTEGER NOT NULL DEFAULT 1,FOREIGN KEY (id_ruc) " +
                " REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_vigilancia  (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_vigilancia " +
                "  INTEGER NOT NULL,id_docente INTEGER NOT NULL, estado TEXT CHECK (estado IN ('Pendente', 'Aceite', 'Recusado'))" +
                " DEFAULT 'Pendente' NOT NULL, justificacao TEXT, FOREIGN KEY (id_docente) REFERENCES docentes (id) ON DELETE NO " +
                " ACTION ON UPDATE CASCADE, FOREIGN KEY (id_vigilancia) REFERENCES vigilancias (id) ON DELETE NO ACTION ON UPDATE" +
                " CASCADE);");






        db.execSQL("CREATE TABLE IF NOT EXISTS vigilancias_history (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, sala TEXT NOT NULL, " +
                " data TEXT NOT NULL, hora TEXT NOT NULL, id_ruc INTEGER NOT NULL, id_disciplina INTEGER NOT NULL," +
                "pontuacao_vigilancia INTEGER NOT NULL DEFAULT 1,FOREIGN KEY (id_ruc) " +
                " REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE);");


        db.execSQL("CREATE TABLE IF NOT EXISTS docente_vigilancia_history (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,id_vigilancia " +
                " INTEGER NOT NULL, id_docente INTEGER NOT NULL, esteve_presente TINYINT DEFAULT 0,justificacao TEXT, FOREIGN KEY " +
                " (id_docente) REFERENCES docentes (id) ON DELETE NO ACTION ON UPDATE CASCADE, FOREIGN KEY (id_vigilancia) " +
                " REFERENCES vigilancias (id) ON DELETE NO ACTION ON UPDATE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS role");
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

    public void insert_funcionario(String nome, String apelido, String telefone, String email, int categoria){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome",nome);
        contentValues.put("apelido", apelido);
        contentValues.put("telefone", telefone);
        contentValues.put("email", email);
        contentValues.put("id_categoria", categoria);
        this.getWritableDatabase().insertOrThrow("funcionarios","",contentValues);
    }

    public void insert_docente(String departamento, String nome, String apelido, String telefone, String email, String categoria){
        ContentValues contentValues = new ContentValues();

        insert_funcionario(nome, apelido, telefone, email, getIdCategoria(categoria));
        contentValues.put("id", getIdFuncionario(email));

        contentValues.put("pontos", 0);
        contentValues.put("id_departamento", getIdFromName("departamentos",departamento));
        this.getWritableDatabase().insertOrThrow("docentes","",contentValues);
    }

    public void list_all_docentes(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM docentes",null);
        insert_docentes_result_in_TextView(textView, cursor);
    }

    public void list_all_Vigilancias(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM vigilancias",null);
        insert_vigilancias_result_in_TextView(textView, cursor);
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

    public void insert_categoria(String nome){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        this.getWritableDatabase().insertOrThrow("categorias","",contentValues);
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

    public void insert_departamento(String nome,String sigla){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("sigla", sigla);
        this.getWritableDatabase().insertOrThrow("departamentos","",contentValues);
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

    public String getIdRuc(String id){
        String idaux = "";
        String query = "SELECT id_ruc FROM vigilancias WHERE id = ?";
        Cursor c = this.getWritableDatabase().rawQuery(query, new String[] {id});
        if(c.moveToFirst()){
            idaux = "" + c.getInt(c.getColumnIndex("id_ruc"));
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

    public void updateDocente(String id, String departamento, String nome, String apelido, String telefone, String email, String categoria){
        updateFuncionario(id, nome, apelido, telefone, email, getIdCategoria(categoria));
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_departamento", getIdFromName("departamentos",departamento));
        this.getWritableDatabase().updateWithOnConflict("docentes", contentValues, "id = " + id,null,SQLiteDatabase.CONFLICT_ROLLBACK);
    }

    public void updateFuncionario(String id, String nome, String apelido, String telefone, String email, int categoria){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome",nome);
        contentValues.put("apelido", apelido);
        contentValues.put("telefone", telefone);
        contentValues.put("email", email);
        contentValues.put("id_categoria", categoria);
        this.getWritableDatabase().updateWithOnConflict("funcionarios", contentValues, "id = " + id,null,SQLiteDatabase.CONFLICT_ROLLBACK);
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

    public void insert_disciplina(String nome, String sigla, String departamento, String emailRuc){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("sigla", sigla);
        contentValues.put("id_departamento", getIdFromName("departamentos", departamento));
        contentValues.put("id_ruc", getIdFuncionario(emailRuc));
        this.getWritableDatabase().insertOrThrow("disciplinas","",contentValues);
    }

    public void insert_vigilancia(String sala, String data, String hora, String emailVig, String disciplina, String pontuacao, int qtdNecessaria) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sala", sala);
        contentValues.put("data", data);
        contentValues.put("hora", hora);
        contentValues.put("id_ruc", getIdFuncionario(emailVig));
        contentValues.put("id_disciplina", getIdFromName("disciplinas", disciplina));
        contentValues.put("pontuacao_vigilancia", Integer.parseInt(pontuacao));
        this.getWritableDatabase().insertOrThrow("vigilancias", "", contentValues);

        String sql = "SELECT id FROM vigilancias WHERE sala = ? AND data = ? AND hora = ? " +
                "AND id_ruc = ? AND id_disciplina = ?  AND pontuacao_vigilancia = ?";

        int idVigilancia= -1;
        Cursor cursor = this.getWritableDatabase().rawQuery(sql, new String[]{
                sala,
                data,
                hora,
                String.valueOf(getIdFuncionario(emailVig)),
                getIdFromName("disciplinas", disciplina),
                pontuacao
        });
        if (cursor.moveToFirst()) {
            idVigilancia = cursor.getInt(0);
        }

        if(qtdNecessaria > 0)
            assignDocenteToVigilancia(qtdNecessaria, getIdFuncionario(emailVig), idVigilancia);
    }


    private void assignDocenteToVigilancia(int qtdNecessaria, int idFuncionario, int idVigilancia) {
        qtdNecessaria += 2;
        String qtd = String.valueOf(qtdNecessaria);
        String id = getIdDepartamento(String.valueOf(idFuncionario));
        List<Integer> docentes = new ArrayList<>(qtdNecessaria);

        String sqlSearch = "SELECT DISTINCT id, pontos FROM docentes WHERE id_departamento = ? ORDER BY pontos LIMIT ?";

        Cursor c = this.getWritableDatabase().rawQuery(sqlSearch, new String[]{id, qtd});
        if (c.moveToFirst()) {
            do {
                docentes.add(c.getInt(0));
            } while (c.moveToNext());
            if (docentes.size() < qtdNecessaria) {
                String query = "SELECT DISTINCT id, pontos FROM docentes ORDER BY pontos DESC LIMIT ?";
                qtdNecessaria -= docentes.size();
                qtd = String.valueOf(qtdNecessaria);
                Cursor cc = this.getWritableDatabase().rawQuery(query, new String[]{qtd});
                if (cc.moveToFirst()) {
                    do {
                        docentes.add(cc.getInt(0));
                    } while (c.moveToNext());
                }
            }
        }

        for(int id_docente : docentes) {
            ContentValues values = new ContentValues();
            values.put("id_vigilancia", idVigilancia);
            values.put("id_docente", id_docente);
            values.put("esteve_presente", 0);
            values.put("justificacao", "");
            this.getWritableDatabase().insertOrThrow("docente_vigilancia", "", values);
        }

        //TODO Falta a notificação
    }

    private void editAssignedDotcenteToVigilancia(int qtdNecessaria, int idFuncionario, int idVigilancia) {
        String sql = "SELECT id FROM docente_vigilancia WHERE id_vigilancia = " + idVigilancia;
        Cursor c = this.getWritableDatabase().rawQuery(sql,null);

        if(c.moveToFirst()) {
            do {
                this.getWritableDatabase().execSQL("delete from docente_vigilancia where id = ?", new String[] {String.valueOf(idVigilancia)});
            } while (c.moveToNext());
        }

        if(qtdNecessaria > 0)
            assignDocenteToVigilancia(qtdNecessaria, idFuncionario, idVigilancia);
    }

    public void updateVigilancia(String id, String sala, String data, String hora, String emailVig, String disciplina, int pontuacao, int qtdPretendida){
        ContentValues contentValues = new ContentValues();
        contentValues.put("sala", sala);
        contentValues.put("data", data);
        contentValues.put("hora", hora);
        contentValues.put("id_ruc", getIdFuncionario(emailVig));
        contentValues.put("id_disciplina", getIdFromName("disciplinas",disciplina));
        contentValues.put("pontuacao_vigilancia", pontuacao);
        this.getWritableDatabase().updateWithOnConflict("vigilancias", contentValues, "id = " + id,null,SQLiteDatabase.CONFLICT_ROLLBACK);

        editAssignedDotcenteToVigilancia(qtdPretendida, getIdFuncionario(emailVig), Integer.parseInt(id));
    }

    public void list_search_docentes(TextView textView, String nome, String departamento, String categoria, String pontos, String modificador){
        String whereClause = "";

        String whereName =(!nome.isEmpty())? " nome like '%" + nome + "%' OR apelido like '%" + nome + "%'" : "";
        String whereDep =(!departamento.isEmpty())? " id_departamento = " + getIdDepartamento(departamento) : "";
        String whereCat =(!categoria.isEmpty())? " id_categoria = " + getIdCategoria(categoria) : "";
        String wherePts = (!pontos.isEmpty() && !modificador.isEmpty()) ? " pontos " + modificador + " " + pontos : "";

        whereClause += whereName;
        whereClause += (whereClause.isEmpty())? whereDep : (whereDep.isEmpty())? "" : " AND " + whereDep;
        whereClause += (whereClause.isEmpty())? whereCat : (whereCat.isEmpty())? "" : " AND " + whereCat;
        whereClause += (whereClause.isEmpty())? wherePts : (wherePts.isEmpty())? "" : " AND " + wherePts;

        String sqlQuery = "SELECT d.id, d.pontos, d.id_departamento FROM  docentes d, funcionarios ";
        sqlQuery += (!whereClause.isEmpty())? " WHERE " + whereClause : "";

        Cursor cursor = this.getReadableDatabase().rawQuery(sqlQuery,null);
        insert_docentes_result_in_TextView(textView, cursor);
    }

    public void list_search_vigilancias(TextView textView, String sala, String data, String hora, String ruc, String disciplina){
        String whereClause = "";
        int idruc =  getIdFuncionario(ruc);
        int iddis =  getIdDisciplinaFromNome(disciplina);

        String whereSala =(!sala.isEmpty())? " sala like '%" + sala + "%'" : "";
        String whereData =(!data.isEmpty())? " data like '" + data + "'" : "";
        String whereHora =(!hora.isEmpty())? " hora like '" + hora + "'": "";
        String whereRuc = (!ruc.isEmpty()) ? ((idruc == -1)? " id_ruc = " + idruc:"") : "";
        String whereDisc = (!disciplina.isEmpty()) ? ((iddis == -1)? " id_disciplina = " + iddis : "") : "";

        whereClause += whereSala;
        whereClause += (whereClause.isEmpty())? whereData : (whereData.isEmpty())? "" : " AND " + whereData;
        whereClause += (whereClause.isEmpty())? whereHora : (whereHora.isEmpty())? "" : " AND " + whereHora;
        whereClause += (whereClause.isEmpty())? whereRuc : (whereRuc.isEmpty())? "" : " AND " + whereRuc;
        whereClause += (whereClause.isEmpty())? whereDisc : (whereDisc.isEmpty())? "" : " AND " + whereDisc;

        String sqlQuery = "SELECT * FROM vigilancias";
        sqlQuery += (!whereClause.isEmpty())? " WHERE " + whereClause : "";

        Cursor cursor = this.getReadableDatabase().rawQuery(sqlQuery,null);
        insert_vigilancias_result_in_TextView(textView, cursor);
    }

    public List<String> getAllRucs(){
        List<Integer> rucs = new ArrayList<>();
        List<String> mails = new ArrayList<>();

        String selectQuery = "SELECT id_ruc FROM disciplinas";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                rucs.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        String selectRUCSQuery = "SELECT email from funcionarios WHERE id = ";
        for (int ruc : rucs) {
            Cursor cursor2 = db.rawQuery(selectRUCSQuery + ruc, null);
            if (cursor2.moveToFirst()) {
                mails.add(cursor2.getString(0));
            }
            cursor2.close();
        }
        db.close();

        return mails;
    }

    private void pesquisa_vigilancia_helper(TextView textView, String docenteID, boolean isHistorico) {
        String historico = (isHistorico)? "_history" : "";
        List<Integer> ids = getAllVigilanciasFromDocente(Integer.parseInt(docenteID), historico);
        List<Integer> vigilancias = new ArrayList<>();
        List<String> salas = new ArrayList<>();
        List<String> datas = new ArrayList<>();
        List<String> horas = new ArrayList<>();
        List<Integer> disciplinas = new ArrayList<>();
        for(int id_vigilancia : ids) {
            Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM vigilancias" + historico + " WHERE id = " + id_vigilancia,null);
            if(cursor.moveToFirst()) {
                do {
                    vigilancias.add(cursor.getInt(0));
                    salas.add(cursor.getString(1));
                    datas.add(cursor.getString(2));
                    horas.add(cursor.getString(3));
                    disciplinas.add(cursor.getInt(5));
                } while (cursor.moveToNext());
            }
        }


        textView.setText("");
        textView.append(Html.fromHtml("<b>" + "ID: \t\tSala: \t\tData:"));
        textView.append(Html.fromHtml("<b><br/>" + "Hora: \t\tDisciplina: "));
        for(int i = 0; i < vigilancias.size(); i++) {
            textView.append("\n\n" + vigilancias.get(i) + ",\t\t" +
                    salas.get(i) + ",\t\t" +
                    datas.get(i) + ",\n" +
                    horas.get(i) + ",\t\t" +
                    getNomeDisciplina("" + disciplinas.get(i)));
        }
    }

    public void list_historico_vigilancias_um_docente(TextView textView, String docenteID) {
        pesquisa_vigilancia_helper(textView, docenteID, true);
    }

    public void list_vigilancias_um_docente(TextView textView, String docenteID){
        pesquisa_vigilancia_helper(textView, docenteID, false);
    }

    private List<Integer>  getAllVigilanciasFromDocente(int id, String historico) {
        List<Integer> ids = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT DISTINCT id_vigilancia FROM docente_vigilancia" + historico + " WHERE id_docente = " + id,null);
        if(cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return ids;
    }

    private int getIdDisciplinaFromNome(String nome) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT id FROM disciplinas WHERE nome like '" + nome + "'",null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    private void insert_docentes_result_in_TextView(TextView textView, Cursor cursor) {
        textView.setText("");
        textView.append(Html.fromHtml("<b>" + "ID: \t\tNome: \t\tPontos:"));
        textView.append(Html.fromHtml("<b><br/>" + "Categoria: \t\tDepartamento: "));
        while (cursor.moveToNext()){
            textView.append("\n\n" + cursor.getString(0) + ",\t\t" +
                    getNomeFuncionario(cursor.getString(0)) + " " +
                    getApelidoFuncionario(cursor.getString(0)) + ",\t\t"+
                    cursor.getString(1) + ",\n" +
                    getNomeCategoria(getCatFunc(cursor.getString(0))) +
                    ",\t\t" + getNomeDepartamento(cursor.getString(2)));
        }
    }

    private void insert_vigilancias_result_in_TextView(TextView textView, Cursor cursor) {
        textView.setText("");
        textView.append(Html.fromHtml("<b>" + "ID: \t\tSala: \t\tData:"));
        textView.append(Html.fromHtml("<b><br/>" + "Hora: \t\tDisciplina: \t\tRUC: "));
        while (cursor.moveToNext()){
            textView.append("\n\n" + cursor.getString(0) + ",\t\t" +
                    cursor.getString(1) + ",\t\t" +
                    cursor.getString(2) + ",\n" +
                    cursor.getString(3) + ",\t\t" +
                    getNomeDisciplina(cursor.getString(5)) + ",\t\t" +
                    getNomeFuncionario(cursor.getString(4)) + " " +
                    getApelidoFuncionario(cursor.getString(4)));
        }
    }

    public void list_historico_vigilancias(TextView textView) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM vigilancias_history",null);

        insert_vigilancias_result_in_TextView(textView, cursor);
    }
}
