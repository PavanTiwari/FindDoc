package com.finddoc;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.finddoc.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	public final static String ZipCode = "com.finddoc.zipcode";
	public  static String callType;
	private double LAT,LON;
	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.doc_button).setOnTouchListener(
				mDelayHideTouchListener);
		findViewById(R.id.hos_button).setOnTouchListener(
				mDelayHideTouchListener);
		findViewById(R.id.spec_button).setOnTouchListener(
				mDelayHideTouchListener);
		/*Set the Zip code of location*/
		/*     Get the value of Zip code entered by user */
		getLocation();
		
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		// lat,lng, your current location
		Address adrs = new Address(Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(LAT,LON, 1);
			 if (addresses.size() > 0) {
				 adrs = addresses.get(0);
		        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		EditText Zipcode = (EditText) findViewById(R.id.Zipcode);
		String InZipCode = adrs.getPostalCode()	;
		Zipcode.setText(InZipCode);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	
	/** Called when the user clicks the Send button */
	public void displayDoc(View view) {
//	    Mark that call is for Doctor
			callType = "D";		
/*     Get the value of Zip code entered by user */
		EditText Zipcode = (EditText) findViewById(R.id.Zipcode);
		String InZipCode = Zipcode.getText().toString();
	    // Do something in response to button
		 Intent intent = new Intent(this, DisplayDoc.class);
		 intent.putExtra(ZipCode, InZipCode);
		 startActivity(intent);
	}
		
	/** Called when the user clicks the Send button */
	public void displayHospital(View view) {
//    Mar that call is for Hospital
		callType = "H";
/*     Get the value of Zip code entered by user */
		EditText Zipcode = (EditText) findViewById(R.id.Zipcode);
		String InZipCode = Zipcode.getText().toString();
	    // Do something in response to button
		 Intent intent = new Intent(this, DisplayDoc.class);
		 intent.putExtra(ZipCode, InZipCode);
		 startActivity(intent);
	}	
	/** Called when the user clicks the Send button */
	public void displaySpec(View view) {
//    Mar that call is for Hospital
		callType = "S";
/*     Get the value of Zip code entered by user */
		EditText Zipcode = (EditText) findViewById(R.id.Zipcode);
		String InZipCode = Zipcode.getText().toString();
	    // Do something in response to button
		 Intent intent = new Intent(this, DisplayDoc.class);
		 intent.putExtra(ZipCode, InZipCode);
		 startActivity(intent);
	}		
	public void getLocation() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		try {
		String bestProvider = locationManager.getBestProvider(criteria, false);
		Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
		LAT = currentLocation.getLatitude() ;
	    LON = currentLocation.getLongitude();
		}
		catch (NullPointerException e)
        {
            System.out.print("NullPointerException caught");
        }
	}
	
}
