package com.gp.gpproject.Read;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gp.gpproject.Create.AdicionarDocenteActivity;
import com.gp.gpproject.DBManager;
import com.gp.gpproject.Delete.EliminarDocenteActivity;
import com.gp.gpproject.R;
import com.gp.gpproject.Update.EditarDocenteActivity;

public class SearchDocenteResultActivity extends AppCompatActivity {
    private String nome, departamento, categoria, modificador, pontos;
    private AlertDialog alertDialog;
    private DBManager manager;
    private TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_docente_result);

        textViewResult = (TextView) findViewById(R.id.searchResultTextInput);


        manager = new DBManager(this);
        Bundle extras = getIntent().getExtras();
        this.nome = extras.getString("nome");
        this.pontos = extras.getString("pontos");
        this.departamento = extras.getString("departamento");
        this.categoria = extras.getString("categoria");
        this.modificador = extras.getString("modificador");

        this.departamento = (this.departamento.contains("Select an item"))? "" : this.departamento;
        this.categoria = (this.categoria.contains("Select an item"))? "" : this.categoria;

        manager.list_search_docentes(textViewResult, nome, departamento, categoria, pontos, modificador);



        ImageButton backbtn = (ImageButton) findViewById(R.id.backBtnSearchResult);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ImageButton newBtn = (ImageButton) findViewById(R.id.newBtnSearchResult);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchDocenteResultActivity.this, AdicionarDocenteActivity.class));
                finish();
            }
        });

        ImageButton deleteBtn = (ImageButton) findViewById(R.id.deleteBtnSearchResult);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchDocenteResultActivity.this, EliminarDocenteActivity.class));
                finish();
            }
        });


        ImageButton editBtn = (ImageButton) findViewById(R.id.editBtnSearchResult);

        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(SearchDocenteResultActivity.this).create();
                alertDialog.setTitle("Editar Docente");
                alertDialog.setMessage("ID do Docente a Editar: ");

                final EditText input = new EditText(SearchDocenteResultActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().equalsIgnoreCase("") && Integer.parseInt(input.getText().toString()) >= 0) {
                                    if (manager.idExists("docentes", input.getText().toString())) {
                                        dialog.cancel();
                                        Intent intent = new Intent(SearchDocenteResultActivity.this, EditarDocenteActivity.class);
                                        intent.putExtra("EXTRA_DOCENTE_ID", input.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        dialog.cancel();
                                        alertDialog = new AlertDialog.Builder(SearchDocenteResultActivity.this).create();
                                        alertDialog.setTitle("Erro");
                                        alertDialog.setMessage("Docente não existente!");
                                        alertDialog.show();
                                    }
                                } else {
                                    dialog.cancel();
                                    alertDialog = new AlertDialog.Builder(SearchDocenteResultActivity.this).create();
                                    alertDialog.setTitle("Erro");
                                    alertDialog.setMessage("Insira um valor válido!");
                                    alertDialog.show();
                                }
                            }
                        });

                alertDialog.show();
            }

        });
    }
}
