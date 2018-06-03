package com.gp.gpproject.Read;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gp.gpproject.DBManager;
import com.gp.gpproject.R;

import java.util.Calendar;
import java.util.List;

public class SearchVigilanciaActivity extends AppCompatActivity {
    private TextView textViewHora, textViewData;
    private EditText editTextSala;
    private Spinner spinnerRUCs, spinnerDiscipplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vigilancia);

        textViewHora = (TextView) findViewById(R.id.textViewSetHora);
        textViewData = (TextView) findViewById(R.id.textViewSetData);
        editTextSala = (EditText) findViewById(R.id.searchSala);
        spinnerRUCs = (Spinner) findViewById(R.id.spinnerRuc);
        spinnerDiscipplina = (Spinner) findViewById(R.id.spinnerDisciplina);

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton confirmSearch = (ImageButton) findViewById(R.id.confirmSearch);
        confirmSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });


        setHourEvent();
        setDateEvent();
        setRUCSpinner();
        setDisciplinaSpinner();
    }

    private void doSearch() {
        Intent goToResultPage = new Intent(this, SearchVigilanciaResultActivity.class);
        goToResultPage.putExtra("textViewHora", textViewHora.getText().toString());
        goToResultPage.putExtra("textViewData", textViewData.getText().toString());
        goToResultPage.putExtra("spinnerDiscipplina", spinnerDiscipplina.getSelectedItem().toString());
        goToResultPage.putExtra("ruc", spinnerRUCs.getSelectedItem().toString());
        goToResultPage.putExtra("sala", editTextSala.getText().toString());
        startActivity(goToResultPage);
    }

    private void setRUCSpinner() {
        DBManager bd = new DBManager(this);
        List<String> rucs = bd.getAllRucs();
        rucs.add(0, "Select an item");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, rucs);
        spinnerRUCs.setAdapter(dataAdapter);
    }

    private void setDisciplinaSpinner() {
        DBManager bd = new DBManager(this);
        List<String> disciplinas = bd.getAllDisciplinas();
        disciplinas.add(0, "Select an item");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, disciplinas);
        spinnerDiscipplina.setAdapter(dataAdapter);
    }

    private void setDateEvent() {
        textViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(SearchVigilanciaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth++;

                        String text = "";
                        text += (selectedday < 10)? "0" + selectedday : selectedday ;
                        text += "/";
                        text += (selectedmonth < 10)? "0" + selectedmonth : selectedmonth;
                        text +=  "/" + selectedyear;

                        textViewData.setText(text);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
    }

    private void setHourEvent() {
        textViewHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SearchVigilanciaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String text = "";
                        text += (selectedHour < 10)? "0" + selectedHour : selectedHour ;
                        text += ":";
                        text += (selectedMinute < 10)? "0" + selectedMinute : selectedMinute;

                        textViewHora.setText(text);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }
}
