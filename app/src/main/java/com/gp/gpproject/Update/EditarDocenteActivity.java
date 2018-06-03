package com.gp.gpproject.Update;

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

public class EditarDocenteActivity extends AppCompatActivity {

    private EditText fieldnome;
    private EditText fieldapelido;
    private EditText fieldtlm;
    private EditText fieldemail;
    private Spinner spinnerCat;
    private Spinner spinnerDep;
    private DBManager bd = new DBManager(this);
    private String s;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_docente);

        fieldnome = (EditText) findViewById(R.id.fieldnome);
        fieldapelido = (EditText) findViewById(R.id.fieldapelido);
        fieldtlm = (EditText) findViewById(R.id.fieldtlm);
        fieldemail = (EditText) findViewById(R.id.fieldemail);
        spinnerCat = (Spinner) findViewById(R.id.spinnerCategoria);
        spinnerDep = (Spinner) findViewById(R.id.spinnerDepartamento);

        addSpinnerCat();
        addSpinnerDep();

        s = getIntent().getStringExtra("EXTRA_DOCENTE_ID");

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(fieldemail.getText().toString())) {
                    if (fieldtlm.length() == 9) {
                        try {
                            bd.updateDocente(s, spinnerDep.getSelectedItem().toString(), fieldnome.getText().toString(), fieldapelido.getText().toString(), fieldtlm.getText().toString(), fieldemail.getText().toString(), spinnerCat.getSelectedItem().toString());
                            startActivity(new Intent(EditarDocenteActivity.this, ListarDocentesActivity.class));
                            finish();
                        } catch (Exception e) {
                            alertDialog = new AlertDialog.Builder(EditarDocenteActivity.this).create();
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("Docente já Existente!");
                            alertDialog.show();
                        }
                    } else {
                        alertDialog = new AlertDialog.Builder(EditarDocenteActivity.this).create();
                        alertDialog.setTitle("Erro");
                        alertDialog.setMessage("Insira um numero de telefone válido!");
                        alertDialog.show();
                    }
                } else {
                    alertDialog = new AlertDialog.Builder(EditarDocenteActivity.this).create();
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
                startActivity(new Intent(EditarDocenteActivity.this, ListarDocentesActivity.class));
                finish();
            }
        });

        populateFields();
    }

    public void addSpinnerCat() {
        spinnerCat.setPrompt("Select an item");

        List<String> categorias = bd.getAllCategorias();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCat.setAdapter(dataAdapter);
    }

    public void addSpinnerDep() {
        spinnerDep.setPrompt("Select an item");

        List<String> departamentos = bd.getAllDepartamentos();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departamentos);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDep.setAdapter(dataAdapter);
    }

    public void populateFields() {
        String depSelected = "" + bd.getNomeDepartamento(bd.getIdDepartamento(s));
        spinnerDep.setSelection(getIndex(spinnerDep, depSelected));

        String catSelected = "" + bd.getNomeCategoria(bd.getCatFunc(s));
        spinnerCat.setSelection(getIndex(spinnerCat, catSelected));

        fieldnome.setText(bd.getNomeFuncionario(s));
        fieldapelido.setText(bd.getApelidoFuncionario(s));
        fieldemail.setText(bd.getEmail(s));
        fieldtlm.setText(bd.getTlm(s));
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
