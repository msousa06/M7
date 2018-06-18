package com.gp.gpproject;

import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class DBManagerTest {

    private DBManager dbManager;

    @Before
    public void setUp() throws Exception {
        //super.setUp();

        final String prefixContext = "test_";
        RenamingDelegatingContext mockContext = new RenamingDelegatingContext(getInstrumentation().getTargetContext(),prefixContext);

        dbManager = new DBManager(mockContext, "mock.db");

    }

    @After
    public void tearDown() throws Exception {
        dbManager.deleteAllDB();
        //super.tearDown();
    }

    @Test
    public void insert_departamento() {
        String nome = "Matematica";
        String sigla = "Mat";

        boolean resultado = dbManager.insert_departamento(nome,sigla);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void insert_categoria() {
        String categoria = "Prof. Adjunto";

        boolean resultado = dbManager.insert_categoria(categoria);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void insert_funcionario() {
        String nome = "João";
        String apelido = "Garcia";
        String telefone = "923333444";
        String email = "150221030@estudantes.ips.pt";
        int categoria = 1;

        boolean resultado = dbManager.insert_funcionario(nome,apelido,telefone,email,categoria);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void insert_docente() {
        String nome = "João";
        String apelido = "Garcia";
        String telefone = "923333444";
        String email = "150221030@estudantes.ips.pt";
        String categoria = "Prof. Adjunto";
        String departamento = "Matematica";

        boolean resultado = dbManager.insert_docente(departamento,nome,apelido,telefone,email,categoria);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void insert_disciplina() {
        String nome = "Probabilidade e Estatistica";
        String sigla = "PE";
        String departamento = "Matematica";
        String emailRuc = "150221030@estudantes.ips.pt";

        boolean resultado = dbManager.insert_disciplina(nome,sigla,departamento,emailRuc);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void insert_vigilancia() {
        String sala = "e255";
        String data = "6/18/2018";
        String hora = "17:20";
        String emailVig = "150221030@estudantes.ips.pt";
        String disciplina = "Matematica";
        String pontuacao = "5";

        boolean resultado = dbManager.insert_vigilancia(sala,data,hora,emailVig,disciplina,pontuacao);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void updateDocente() {
        String id = "1";
        String nome = "João1";
        String apelido = "Garcia1";
        String telefone = "923333555";
        String email = "999999999@estudantes.ips.pt";
        String categoria = "Prof. Adjunto";
        String departamento = "Matematica";

        boolean resultado = dbManager.updateDocente(id,departamento,nome,apelido,telefone,email,categoria);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void updateFuncionario() {
        String id = "1";
        String nome = "João1";
        String apelido = "Garcia1";
        String telefone = "923333555";
        String email = "999999999@estudantes.ips.pt";
        int categoria = 1;

        boolean resultado = dbManager.updateFuncionario(id,nome,apelido,telefone,email,categoria);
        Assert.assertEquals(true, resultado);
    }

    @Test
    public void updateVigilancia() {
        String id = "1";
        String sala = "e277";
        String data = "9/18/2018";
        String hora = "10:20";
        String emailVig = "999999999@estudantes.ips.pt";
        String disciplina = "Matematica";
        int pontuacao = 7;

        boolean resultado = dbManager.updateVigilancia(id,sala,data,hora,emailVig,disciplina,pontuacao);
        Assert.assertEquals(true, resultado);
    }

    /*@Test
    public void delete() {
        String tableName = "funcionarios";
        String id = "1";

        boolean resultado = dbManager.delete(tableName,id);

        Assert.assertEquals(true, resultado);
    }
    */
}