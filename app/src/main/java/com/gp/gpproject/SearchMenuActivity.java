package com.gp.gpproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gp.gpproject.Read.SearchDocenteActivity;
import com.gp.gpproject.Read.SearchVigilanciaActivity;
import com.gp.gpproject.Read.SearchVigilanciaResultActivity;

public class SearchMenuActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    private DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);
        manager = new DBManager(this);

        Button btnSearchDocente = (Button) findViewById(R.id.btnSearchDocente);
        btnSearchDocente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchDocente();
            }
        });

        Button btnSearchVigilancias = (Button) findViewById(R.id.btnSearchVigilancias);
        btnSearchVigilancias.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchVigilancias();
            }
        });

        Button btnSearchOwnVigilancias = (Button) findViewById(R.id.btnOwnVigilancias);
        btnSearchOwnVigilancias.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ searchOwnVigilancias();
            }
        });

        Button btnViewVigilanciasFromDocente = (Button) findViewById(R.id.btnSearchVigilanciasFromDocente);
        btnViewVigilanciasFromDocente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ vigilanciasFromDocente();
            }
        });

        Button btnHistoricoDoDocente = (Button) findViewById(R.id.btnHistoricoDoDocente);
        btnHistoricoDoDocente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                historicoDoDocente();
            }
        });

        Button btnVerHistorico = (Button) findViewById(R.id.btnHistoricoVigilancias);
        btnVerHistorico.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                verHistorico();
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

    public void searchDocente() {
        startActivity(new Intent(SearchMenuActivity.this, SearchDocenteActivity.class));
    }

    public void searchVigilancias() {
        startActivity(new Intent(SearchMenuActivity.this, SearchVigilanciaActivity.class));
    }

    public void searchOwnVigilancias() {
        Intent goSearchOwnVigilancias = new Intent(SearchMenuActivity.this, SearchVigilanciaResultActivity.class);
        goSearchOwnVigilancias.putExtra("SearchID", "1");
        //TODO rever o modo que o id do docente logado "viaja" dentro da aplicação
        startActivity(goSearchOwnVigilancias);
    }

    public void vigilanciasFromDocente() {
        alertDialog = new AlertDialog.Builder(SearchMenuActivity.this).create();
        alertDialog.setTitle("Pesquisar Vigilâncias do Docente");
        alertDialog.setMessage("ID do docente a Pesquisar: ");

        final EditText input = new EditText(SearchMenuActivity.this);
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equalsIgnoreCase("") && Integer.parseInt(input.getText().toString()) >= 0) {
                            if (manager.idExists("docentes", input.getText().toString())) {
                                dialog.cancel();
                                Intent intent = new Intent(SearchMenuActivity.this, SearchVigilanciaResultActivity.class);
                                intent.putExtra("SearchID", input.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.cancel();
                                alertDialog = new AlertDialog.Builder(SearchMenuActivity.this).create();
                                alertDialog.setTitle("Erro");
                                alertDialog.setMessage("Docente não existente!");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                alertDialog.show();
                            }
                        } else {
                            dialog.cancel();
                            alertDialog = new AlertDialog.Builder(SearchMenuActivity.this).create();
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("Insira um valor válido!");

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
        alertDialog.show();
    }

    private void historicoDoDocente() {
        alertDialog = new AlertDialog.Builder(SearchMenuActivity.this).create();
        alertDialog.setTitle("Pesquisar Histórico de Vigilâncias do Docente");
        alertDialog.setMessage("ID do docente a Pesquisar: ");

        final EditText input = new EditText(SearchMenuActivity.this);
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equalsIgnoreCase("") && Integer.parseInt(input.getText().toString()) >= 0) {
                            if (manager.idExists("docentes", input.getText().toString())) {
                                dialog.cancel();
                                Intent intent = new Intent(SearchMenuActivity.this, SearchVigilanciaResultActivity.class);
                                intent.putExtra("HistorySearchID", input.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.cancel();
                                alertDialog = new AlertDialog.Builder(SearchMenuActivity.this).create();
                                alertDialog.setTitle("Erro");
                                alertDialog.setMessage("Docente não existente!");

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                alertDialog.show();
                            }
                        } else {
                            dialog.cancel();
                            alertDialog = new AlertDialog.Builder(SearchMenuActivity.this).create();
                            alertDialog.setTitle("Erro");
                            alertDialog.setMessage("Insira um valor válido!");

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
        alertDialog.show();
    }

    private void verHistorico() {
        Intent intent = new Intent(SearchMenuActivity.this, SearchVigilanciaResultActivity.class);
        intent.putExtra("AllHistory", true);
        //TODO rever o modo que o id do docente logado "viaja" dentro da aplicação
        startActivity(intent);

        Toast.makeText(SearchMenuActivity.this, "Botão não implementado", Toast.LENGTH_LONG).show();
    }
}
