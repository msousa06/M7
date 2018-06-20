package com.gp.gpproject.Update;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.gp.gpproject.DBManager;
import com.gp.gpproject.Read.ListarVigilanciasActivity;
import com.gp.gpproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditarVigilanciaActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayTime;
    private Spinner spinnerUC;
    private Spinner spinnerVig;
    private Spinner spinnerPontuacao;
    private EditText salatxt;
    private EditText qtdPretendida;
    private DBManager manager;
    private AlertDialog alertDialog;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_vigilancia);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayTime = (TextView) findViewById(R.id.tvTime);
        spinnerUC = (Spinner) findViewById(R.id.spinnerUc);
        spinnerVig = (Spinner) findViewById(R.id.spinnerVig);
        spinnerPontuacao = (Spinner) findViewById(R.id.spinnerPontuacao);
        salatxt = (EditText) findViewById(R.id.salatxt);
        qtdPretendida = (EditText) findViewById(R.id.qtdPretendida);
        manager = new DBManager(this);
        s = getIntent().getStringExtra("EXTRA_VIGILANCIA_ID");

        addSpinnerPontuacao();
        addSpinnerUC();
        addSpinnerVig();
        setDate();
        setTime();

        ImageView confirmbtn = (ImageView) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager.updateVigilancia(
                            s,
                            salatxt.getText().toString(),
                            mDisplayDate.getText().toString(),
                            mDisplayTime.getText().toString(),
                            spinnerVig.getSelectedItem().toString(),
                            spinnerUC.getSelectedItem().toString(),
                            Integer.parseInt(spinnerPontuacao.getSelectedItem().toString()),
                            Integer.parseInt(qtdPretendida.getText().toString()));

                    startActivity(new Intent(EditarVigilanciaActivity.this, ListarVigilanciasActivity.class));
                    finish();
                } catch (Exception e) {
                    alertDialog = new AlertDialog.Builder(EditarVigilanciaActivity.this).create();
                    alertDialog.setTitle("Erro");
                    alertDialog.setMessage("Vigilância já existente!");
                    alertDialog.show();
                }
            }
        });

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarVigilanciaActivity.this, ListarVigilanciasActivity.class));
                finish();
            }
        });

        populateFields();
    }

    public void addSpinnerUC() {
        spinnerUC.setPrompt("Select an item");

        List<String> disciplinas = manager.getAllDisciplinas();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, disciplinas);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUC.setAdapter(dataAdapter);
    }

    public void addSpinnerVig() {
        spinnerVig.setPrompt("Select an item");

        List<String> vigilantes = manager.getAllDocentes();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vigilantes);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVig.setAdapter(dataAdapter);
    }

    public void addSpinnerPontuacao() {
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            items.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        spinnerPontuacao.setAdapter(adapter);
    }

    public void populateFields() {
        String ucSelected = "" + manager.getNomeDisciplina(manager.getIdDisciplina(s));
        spinnerUC.setSelection(getIndex(spinnerUC, ucSelected));

        String vigSelected = "" + manager.getEmail(manager.getIdRuc(s));
        spinnerVig.setSelection(getIndex(spinnerVig, vigSelected));

        String pontuacaoSelected = "" + manager.getPontuacao(s);
        spinnerPontuacao.setSelection(getIndex(spinnerPontuacao, pontuacaoSelected));

        mDisplayDate.setText(manager.getData(s));
        mDisplayTime.setText(manager.getHora(s));
        salatxt.setText(manager.getSala(s));
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    public void setTime() {
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditarVigilanciaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mDisplayTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void setDate() {
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditarVigilanciaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
    }
}
