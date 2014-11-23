package com.finddoc;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class DocDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_doc_detail);
		// Show the Up button in the action bar.
		setupActionBar();
 /*     Get the value.		*/
		//Intent intent1 = getIntent();
		//String key = (String) intent1.getStringExtra(DisplayDoc.KEY);		
		 // Create the text view
	    //TextView textView = new TextView(this);
		String FNAME = (String) DisplayDoc.itemObject.getObject("FNAME"); 
        TextView textView = (TextView) findViewById(R.id.FnameValue);
        textView.setText(FNAME);
	    //textView.setTextSize(40);
	    //textView.setText(key);
/*    Get the Last Name */      
         String LNAME = (String) DisplayDoc.itemObject.getObject("LNAME"); 
         TextView textView2 = (TextView) findViewById(R.id.LnameValue);
         textView2.setText(LNAME);
         /*    Get the Street */      
         String street = (String) DisplayDoc.itemObject.getObject("STREET"); 
         TextView textView3 = (TextView) findViewById(R.id.txtStreetVal);
         textView3.setText(street);         
         /*    Get the City */      
         String city = (String) DisplayDoc.itemObject.getObject("CITY"); 
         TextView textView4 = (TextView) findViewById(R.id.txtCityVal);
         textView4.setText(city);            
         /*    Get the Speciality */      
         String spl = (String) DisplayDoc.itemObject.getObject("SPECIALITY"); 
         TextView textView5 = (TextView) findViewById(R.id.txtSplVal);
         textView5.setText(spl);  
         /*    Get the hospital name */      
         String hplname = (String) DisplayDoc.itemObject.getObject("HOSPITAL"); 
         TextView textView6 = (TextView) findViewById(R.id.txtHplVal);
         textView6.setText(hplname);  
         /*    Get the Timing */      
         String timing = (String) DisplayDoc.itemObject.getObject("TIME"); 
         TextView textView7 = (TextView) findViewById(R.id.txtTimVal);
         textView7.setText(timing);        
         /*    Get the Contact */      
         String contact = (String) DisplayDoc.itemObject.getObject("CONTACT"); 
         TextView textView8 = (TextView) findViewById(R.id.txtContactVal);
         textView8.setText(contact);                 
	    // Set the text view as the activity layout
	  //  setContentView(textView);		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doc_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
