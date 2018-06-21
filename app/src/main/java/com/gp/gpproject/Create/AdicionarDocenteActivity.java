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
import com.gp.gpproject.Read.ListarDocentesActivity;
import com.gp.gpproject.R;

import java.util.List;

public class AdicionarDocenteActivity extends AppCompatActivity {

    private EditText fieldnome;
    private EditText fieldapelido;
    private EditText fieldtlm;
    private EditText fieldemail;
    private Spinner spinnerCat;
    private Spinner spinnerDep;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_docente);

        fieldnome = (EditText) findViewById(R.id.fieldnome);
        fieldapelido = (EditText) findViewById(R.id.fieldapelido);
        fieldtlm = (EditText) findViewById(R.id.fieldtlm);
        fieldemail = (EditText) findViewById(R.id.fieldemail);
        spinnerCat = (Spinner) findViewById(R.id.spinnerCategoria);
        spinnerDep = (Spinner) findViewById(R.id.spinnerDepartamento);

        final DBManager manager = new DBManager(this);

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(fieldemail.getText().toString())) {
                    if (fieldtlm.length() == 9) {
                        try {
                            manager.insert_docente(spinnerDep.getSelectedItem().toString(), fieldnome.getText().toString(), fieldapelido.getText().toString(), fieldtlm.getText().toString(), fieldemail.getText().toString(), spinnerCat.getSelectedItem().toString());
                            startActivity(new Intent(AdicionarDocenteActivity.this, ListarDocentesActivity.class));
                            finish();
                        } catch (Exception e) {
                            alertDialog = new AlertDialog.Builder(AdicionarDocenteActivity.this).create();
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("Docente já Existente!");
                            alertDialog.show();
                        }

                    } else {
                        alertDialog = new AlertDialog.Builder(AdicionarDocenteActivity.this).create();
                        alertDialog.setTitle("Erro");
                        alertDialog.setMessage("Insira um numero de telefone válido!");
                        alertDialog.show();
                    }
                } else {
                    alertDialog = new AlertDialog.Builder(AdicionarDocenteActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Insira um email válido!");
                    alertDialog.show();
                }
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdicionarDocenteActivity.this, ListarDocentesActivity.class));
                finish();
            }
        });

        addSpinnerCat();
        addSpinnerDep();
    }

    public void addSpinnerCat() {
        spinnerCat.setPrompt("Select an item");
        DBManager bd = new DBManager(this);

        List<String> categorias = bd.getAllCategorias();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCat.setAdapter(dataAdapter);
    }

    public void addSpinnerDep() {
        spinnerDep.setPrompt("Select an item");
        DBManager bd = new DBManager(this);

        List<String> departamentos = bd.getAllDepartamentos();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departamentos);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDep.setAdapter(dataAdapter);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
