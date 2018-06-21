package com.gp.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import com.gp.gpproject.Create.AdicionarCategoriaActivity;
import com.gp.gpproject.Create.AdicionarDepartamentoActivity;
import com.gp.gpproject.Create.AdicionarDisciplinaActivity;
import com.gp.gpproject.Read.ListarDocentesActivity;
import com.gp.gpproject.Read.ListarVigilanciasActivity;


public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    DBManager db = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ImageButton logOFFbtn = (ImageButton) findViewById(R.id.logOFFbtn);
        logOFFbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        Button btnListaDocentes = (Button) findViewById(R.id.btnListaDocentes);
        btnListaDocentes.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               String idListaDocentes = "";
               String nameListaDocentes = "";
               Bundle bundleListaDocentes = new Bundle();
               bundleListaDocentes.putString(FirebaseAnalytics.Param.ITEM_ID, idListaDocentes);
               bundleListaDocentes.putString(FirebaseAnalytics.Param.ITEM_NAME, nameListaDocentes);
               bundleListaDocentes.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
               mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleListaDocentes);
               startActivity(new Intent(MainActivity.this,ListarDocentesActivity.class));
           }
        });

        Button btnAddCategoria = (Button) findViewById(R.id.btnAddCategoria);
        btnAddCategoria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String idAddCategoria = "";
                String nameAddCategoria = "";
                Bundle bundleAddCategoria = new Bundle();
                bundleAddCategoria.putString(FirebaseAnalytics.Param.ITEM_ID, idAddCategoria);
                bundleAddCategoria.putString(FirebaseAnalytics.Param.ITEM_NAME, nameAddCategoria);
                bundleAddCategoria.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAddCategoria);
                startActivity(new Intent(MainActivity.this,AdicionarCategoriaActivity.class));
            }
        });

        Button btnAddDepartamento = (Button) findViewById(R.id.btnAddDepartamento);
        btnAddDepartamento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String idAddDepartamento = "";
                String nameAddDepartamento = "";
                Bundle bundleAddDepartamento = new Bundle();
                bundleAddDepartamento.putString(FirebaseAnalytics.Param.ITEM_ID, idAddDepartamento);
                bundleAddDepartamento.putString(FirebaseAnalytics.Param.ITEM_NAME, nameAddDepartamento);
                bundleAddDepartamento.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAddDepartamento);
                startActivity(new Intent(MainActivity.this,AdicionarDepartamentoActivity.class));
            }
        });

        Button btnListaVig = (Button) findViewById(R.id.btnListaVig);
        btnListaVig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String idListaVig = "";
                String nameListaVig = "";
                Bundle bundleListaVig = new Bundle();
                bundleListaVig.putString(FirebaseAnalytics.Param.ITEM_ID, idListaVig);
                bundleListaVig.putString(FirebaseAnalytics.Param.ITEM_NAME, nameListaVig);
                bundleListaVig.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleListaVig);
                startActivity(new Intent(MainActivity.this,ListarVigilanciasActivity.class));
            }
        });

        Button btnAddDisciplina = (Button) findViewById(R.id.btnAddDisciplina);
        btnAddDisciplina.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String idAddDisciplina = "";
                String nameAddDisciplina = "";
                Bundle bundleAddDisciplina = new Bundle();
                bundleAddDisciplina.putString(FirebaseAnalytics.Param.ITEM_ID, idAddDisciplina);
                bundleAddDisciplina.putString(FirebaseAnalytics.Param.ITEM_NAME, nameAddDisciplina);
                bundleAddDisciplina.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundleAddDisciplina);
                startActivity(new Intent(MainActivity.this,AdicionarDisciplinaActivity.class));
            }
        });

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,SearchMenuActivity.class));
            }
        });
    }
}
