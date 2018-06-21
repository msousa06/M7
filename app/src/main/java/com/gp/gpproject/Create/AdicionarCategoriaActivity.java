package com.gp.gpproject.Create;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gp.gpproject.DBManager;
import com.gp.gpproject.R;


public class AdicionarCategoriaActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_categoria);

        final EditText fieldnome = (EditText) findViewById(R.id.fieldnome);

        final DBManager manager = new DBManager(this);

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.insert_categoria(fieldnome.getText().toString());
                    finish();
                }
                catch(Exception e){
                    alertDialog = new AlertDialog.Builder(AdicionarCategoriaActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Categoria já Existente!");
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
