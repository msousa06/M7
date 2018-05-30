package com.gp.gpproject;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class EliminarDocenteActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_docente);

        final EditText fieldID = (EditText) findViewById(R.id.fieldID);

        final DBManager manager = new DBManager(this,"",null,2);

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manager.delete("docentes",fieldID.getText().toString())){
                    finish();
                } else {
                    alertDialog = new AlertDialog.Builder(EliminarDocenteActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Docente não existe!");
                    alertDialog.show();
                }
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}
