package com.gp.gpproject.Create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gp.gpproject.DBManager;
import com.gp.gpproject.MainActivity;
import com.gp.gpproject.R;


public class AdicionarDepartamentoActivity extends AppCompatActivity {

    private EditText fieldnome;
    private EditText fieldsigla;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_departamento);

        fieldnome = (EditText) findViewById(R.id.fieldnome);
        fieldsigla = (EditText) findViewById(R.id.fieldsigla);

        final DBManager manager = new DBManager(this);

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.insert_departamento(fieldnome.getText().toString(), fieldsigla.getText().toString());
                    startActivity(new Intent(AdicionarDepartamentoActivity.this, MainActivity.class));
                    finish();
                }
                catch(Exception e){
                    alertDialog = new AlertDialog.Builder(AdicionarDepartamentoActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Departamento já Existente!");
                    alertDialog.show();
                }
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(AdicionarDepartamentoActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
