package com.finddoc;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.test.MoreAsserts;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;

import com.finddoc.R;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

public class DisplayDoc extends Activity {
	public final static String KEY = "com.finddoc.key";
	public  static Doctor  itemObject = new Doctor();
	public static String InZipCode = new String();
	List<Doctor> doctorList;
	FindDocApplication FindDocApp;
	ArrayAdapter<Doctor> lvArrayAdapter;
	ActionMode mActionMode = null;
	int listItemPosition;
	public static final String CLASS_NAME="FullscreenActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_doc);
		// Show the Up button in the action bar.
		setupActionBar();
		/* Use application class to maintain global state. */
		FindDocApp = (FindDocApplication) getApplication();
		doctorList = FindDocApp.getItemList();
		
		/* Set up the array adapter for items list view. */
		final ListView itemsLV = (ListView)findViewById(R.id.itemsList);
		lvArrayAdapter = new ArrayAdapter<Doctor>(this, R.layout.list_item_1, doctorList);
		itemsLV.setAdapter(lvArrayAdapter);
		
		/* Refresh the list. */
		listItems(); 	
		 // ListView Doctor Click Listener
        itemsLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long arg3) {

            	// ListView Clicked item value
            	 itemObject =(Doctor) (itemsLV.getItemAtPosition(pos));
            	 String itemvalue = (String)itemObject.getName();
        		 Intent docdetail = new Intent(DisplayDoc.this, DocDetail.class);	
				 docdetail.putExtra(KEY, itemvalue);
        		 startActivity(docdetail);         			   
                   //If you wanna send any data to nextActicity.class you can use
                   // i.putExtra(string key, .get(pos));

                            	 
            }

        });
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);			
		//}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.display_doc, menu);
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

	/**
	 * Refreshes doctorList from data service.
	 * 
	 * An IBMQuery is used to find all the list items.
	 */
	public void listItems() {
		try {

			/*     Get the value.		*/
			if((InZipCode.equals(""))){
			Intent intent1 = getIntent();
			InZipCode = (String) intent1.getStringExtra(FullscreenActivity.ZipCode);
			};
			IBMQuery<Doctor> query = IBMQuery.queryForClass(Doctor.class);
			/* if Zip code is null then Display Allrecords*/
			if(!(InZipCode.equals(""))){
			query.whereKeyEqualsTo( "ZIPCODE", InZipCode); };
			
			// Query all the Doctor objects from the server.
			query.find().continueWith(new Continuation<List<Doctor>, Void>() {

				@Override
				public Void then(Task<List<Doctor>> task) throws Exception {
                    final List<Doctor> objects = task.getResult();
                     // Log if the find was cancelled.
                    if (task.isCancelled()){
                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                    }
					 // Log error message, if the find task fails.
					else if (task.isFaulted()) {
						Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
					}

					
					 // If the result succeeds, load the list.
					else {
                        // Clear local doctorList.
                        // We'll be reordering and repopulating from DataService.
                        doctorList.clear();
                        for(IBMDataObject item:objects) {
                            doctorList.add((Doctor) item);
                        }
                        sortItems(doctorList);
                        lvArrayAdapter.notifyDataSetChanged();
					}
					return null;
				}
			},Task.UI_THREAD_EXECUTOR);
			
		}  catch (IBMDataException error) {
			Log.e(CLASS_NAME, "Exception : " + error.getMessage());
		}
	}
	
	/**
	 * Sort a list of Items.
	 * @param List<Doctor> theList
	 */
	private void sortItems(List<Doctor> theList) {
		// Sort collection by case insensitive alphabetical order.
		Collections.sort(theList, new Comparator<Doctor>() {
			public int compare(Doctor lhs,
					Doctor rhs) {
				String lhsName = lhs.getName();
				String rhsName = rhs.getName();
				return lhsName.compareToIgnoreCase(rhsName);
			}
		});
	}		
	
}
