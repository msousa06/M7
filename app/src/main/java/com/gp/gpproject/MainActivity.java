package com.gp.gpproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    DBManager db = new DBManager(this,"",null,2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnListaDocentes = (Button) findViewById(R.id.btnListaDocentes);
        btnListaDocentes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,ListarDocentesActivity.class));
            }
           @Override
           public void onClick(View v){
               startActivity(new Intent(MainActivity.this,ListarDocentesActivity.class));
           }
        });

        Button btnAddCategoria = (Button) findViewById(R.id.btnAddCategoria);
        btnAddCategoria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,AdicionarCategoriaActivity.class));
            }
        });

        Button btnAddDepartamento = (Button) findViewById(R.id.btnAddDepartamento);
        btnAddDepartamento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,AdicionarDepartamentoActivity.class));
            }
        });

        Button btnListaVig = (Button) findViewById(R.id.btnListaVig);
        btnListaVig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,ListarVigilanciasActivity.class));
            }
        });

        Button btnAddDisciplina = (Button) findViewById(R.id.btnAddDisciplina);
        btnAddDisciplina.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,AdicionarDisciplinaActivity.class));
            }
        });
    }
}
