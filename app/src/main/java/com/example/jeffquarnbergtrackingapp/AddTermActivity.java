package com.example.jeffquarnbergtrackingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.jeffquarnbergtrackingapp.Entities.Terms;
import com.example.jeffquarnbergtrackingapp.ViewModel.TermsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTermActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mTitle;
    private EditText mStartDate;
    private Date termStartDate;
    private EditText mEndDate;
    private Date termEndDate;
    private DatePickerDialog termStartDialog;
    private DatePickerDialog termEndDialog;
    private SimpleDateFormat dateFormat;
    private Button saveButton;

    private Terms mAddTerm;
    private TermsViewModel mTermsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        mTitle = findViewById(R.id.add_term_title);
        mStartDate = findViewById(R.id.add_term_start_date);
        mStartDate.setInputType(InputType.TYPE_NULL);
        mEndDate = findViewById(R.id.add_term_end_date);
        mEndDate.setInputType(InputType.TYPE_NULL);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.US);
        mTermsViewModel = ViewModelProviders.of(this).get(TermsViewModel.class);

        saveButton = findViewById(R.id.add_term_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        termDatePickers();

    }

    private void saveChanges() {
        try {
            termStartDate = dateFormat.parse(mStartDate.getText().toString());
            termEndDate = dateFormat.parse(mEndDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mAddTerm = new Terms(mTitle.getText().toString(),
                termStartDate,
                termEndDate);

        mTermsViewModel.insert(mAddTerm);

        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    private void termDatePickers() {
        mStartDate.setOnClickListener(this);
        mEndDate.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();

        termStartDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                mStartDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        termEndDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                mEndDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    termStartDialog.show();
                } else {
                    termEndDialog.show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
