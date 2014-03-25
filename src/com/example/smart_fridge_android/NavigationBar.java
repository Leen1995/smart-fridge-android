package com.example.smart_fridge_android;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class NavigationBar {
	
	public NavigationBar() {

	}

	public void onButtonClick(View v, Context c) {
		switch (v.getId()){

		case R.id.addBtn: 
			Intent i = new Intent(c, AddFoodActivity.class);
			c.startActivity(i);
			break;

		case R.id.logoutBtn:
			//Go to the Logout Activity
			break;
		}
	}
}
