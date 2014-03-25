package com.example.smart_fridge_android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
<<<<<<< HEAD
import android.widget.DatePicker;
import android.widget.EditText;

public class AddFoodActivity extends Activity implements OnDateSetListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_food);
		EditText mDateTextView = (EditText)findViewById(R.id.dateSelector);
		mDateTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				DialogFragment newFragment = new DatePickerDialogFragment(AddFoodActivity.this);
				newFragment.show(ft, "date_dialog");
			}
		});
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		EditText mDateTextView = (EditText)findViewById(R.id.dateSelector);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		mDateTextView.setText(dateFormat.format(cal.getTime()));
	}

	public void onButtonClick(View v){

		switch (v.getId()){

		case R.id.addBtn: //Put this code in a modular location
			Intent i = new Intent(getApplicationContext(), AddFoodActivity.class);
			startActivity(i);
			break;
		case R.id.manualBtn:
			setContentView(R.layout.add_food_manual);
			break;
=======
import android.widget.Button;

public class AddFoodActivity extends Activity {
	
	private Button add;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food);
        add = (Button) findViewById(R.id.addBtn);
        if (add != null)
        	add.setVisibility(View.INVISIBLE);
    }
	
	protected void onPause() {
		super.onPause();
		add.setVisibility(View.VISIBLE); //Make button visible again when you leave this view.
	}
	
	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
    	
		switch (v.getId()){
		
		case R.id.addBtn:
		case R.id.logoutBtn:
    		navBar.onButtonClick(v, this);
    		break;
>>>>>>> 017a213d718ddfff3ae127a9b3bf59d2508f4654
		}
	}
}
