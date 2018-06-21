package com.gp.gpproject.Read;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.gp.gpproject.Create.AdicionarDocenteActivity;
import com.gp.gpproject.DBManager;
import com.gp.gpproject.R;
import com.gp.gpproject.Read.ListarDocentesActivity;
import com.gp.gpproject.Read.SearchDocenteResultActivity;

import java.util.List;

public class SearchDocenteActivity extends AppCompatActivity {
    private Spinner spinnerDep, spinnerCat, spinnerPts;
    private EditText editTextNome, editTextPontos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_docente);
        spinnerDep = findViewById(R.id.spinnerSearchDepartments);
        spinnerCat = findViewById(R.id.spinnerSearchCategory);
        spinnerPts = findViewById(R.id.spinnerSearchPoints);
        editTextNome = (EditText) findViewById(R.id.searchDocenteNome);
        editTextPontos = (EditText) findViewById(R.id.searchDocentePontos);


        addSpinnerDep();
        addSpinnerCat();
        addSpinnerPts();

        ImageButton confirmSearchBtn = (ImageButton) findViewById(R.id.confirmSearch);
        confirmSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void doSearch(){
        Intent goToResultPage = new Intent(this, SearchDocenteResultActivity.class);
        goToResultPage.putExtra("nome", editTextNome.getText().toString());
        goToResultPage.putExtra("pontos", editTextPontos.getText().toString());
        goToResultPage.putExtra("departamento", spinnerDep.getSelectedItem().toString());
        goToResultPage.putExtra("categoria", spinnerCat.getSelectedItem().toString());
        goToResultPage.putExtra("modificador", spinnerPts.getSelectedItem().toString());
        startActivity(goToResultPage);
    }

    private void addSpinnerDep() {
        DBManager bd = new DBManager(this);
        List<String> departamentos = bd.getAllDepartamentos();
        departamentos.add(0, "Select an item");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, departamentos);
        spinnerDep.setAdapter(dataAdapter);
    }

    private void addSpinnerPts() {
        ArrayAdapter<String> adapterPoints = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, new String[] {"<","<=","=",">=", ">"});
        spinnerPts.setAdapter(adapterPoints);
    }

    private void addSpinnerCat() {
        DBManager bd = new DBManager(this);
        List<String> categorias = bd.getAllCategorias();
        categorias.add(0, "Select an item");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categorias);
        spinnerCat.setAdapter(dataAdapter);
    }
}
