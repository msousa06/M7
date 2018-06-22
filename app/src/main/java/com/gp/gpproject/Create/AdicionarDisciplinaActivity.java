package com.gp.gpproject.Create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;


import com.gp.gpproject.DBManager;
import com.gp.gpproject.MainActivity;
import com.gp.gpproject.R;

import java.util.List;

public class AdicionarDisciplinaActivity extends AppCompatActivity {

    private EditText fieldnome;
    private EditText fieldsigla;
    private Spinner spinnerDep;
    private Spinner spinnerRuc;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_disciplina);

        fieldnome = (EditText) findViewById(R.id.fieldnome);
        fieldsigla = (EditText) findViewById(R.id.fieldsigla);
        spinnerRuc = (Spinner) findViewById(R.id.spinnerRuc);
        spinnerDep = (Spinner) findViewById(R.id.spinnerDep);

        addSpinnerDep();
        addSpinnerRuc();

        final DBManager manager = new DBManager(this);

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.insert_disciplina(fieldnome.getText().toString(), fieldsigla.getText().toString(), spinnerDep.getSelectedItem().toString(), spinnerRuc.getSelectedItem().toString());
                    startActivity(new Intent(AdicionarDisciplinaActivity.this, MainActivity.class));
                    finish();
                }
                catch(Exception e){
                    alertDialog = new AlertDialog.Builder(AdicionarDisciplinaActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Disciplina já Existente!");
                    alertDialog.show();
                }
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(AdicionarDisciplinaActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void addSpinnerRuc() {
        spinnerRuc.setPrompt("Select an item");
        DBManager bd = new DBManager(this);

        List<String> docentes = bd.getAllDocentes();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, docentes);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRuc.setAdapter(dataAdapter);
    }

    public void addSpinnerDep() {
        spinnerDep.setPrompt("Select an item");
        DBManager bd = new DBManager(this);

        List<String> departamentos = bd.getAllDepartamentos();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departamentos);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDep.setAdapter(dataAdapter);
    }
}
