package com.codepath.simpleapp;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codepath.simpleapp.R.id.editTask;


public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

    }

    public void datePicker(View view){

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"date");
    }

    private void setDate(final Calendar calendar){

        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar cal = new GregorianCalendar(year,month,dayOfMonth);
        setDate(cal);

    }


    public static class DatePickerFragment extends DialogFragment{


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(),year,month,day);

        }
    }

    public void onDoneBtn(View v){

        EditText editText = (EditText)findViewById(editTask);
        EditText editDate = (EditText)findViewById(R.id.showDate);
        if (!editText.getText().toString().equals("")){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("Task",editText.getText().toString());
            bundle.putString("DueDate",editDate.getText().toString());
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            finish();

        }
    }
}
