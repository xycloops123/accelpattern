package com.aig.nyu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.aig.nyu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UnlockActivity extends Activity implements SensorEventListener

{
	// Directory where data file is stored in application directory

	private SensorManager sensorManagerUnlock;
	private ImageView displayArrowUnlock = null;
	private TextView messageUnlock = null;
	private String passwordSaved = null;
	private SharedPreferences settings;
	private static final String RECEIVED_PASSWORD = "unlock";
	private String getBackupPin = null;
	private int numOfTries = 0;
	private int count = 0;
	private long lastUpdate = 0l;
	private long diffTime = 0l;
			
	private String x_direction = "";
	private String y_direction = "";
	
	private float last_x;
	private float last_y;
	private float last_z;
	
	private ImageView displayArrow = null;


	private StringBuilder attemptedPassword = new StringBuilder();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.unlockscreen);

		setTitleColor(getResources().getColor(R.color.black));

		displayArrowUnlock = (ImageView) findViewById(R.id.imageViewUnlock);
		messageUnlock = (TextView) findViewById(R.id.xt_axis);

		
		Intent intent = getIntent();
		passwordSaved = intent.getStringExtra(RECEIVED_PASSWORD);

		// getting shared preferences settings from prefs screen.
		settings = PreferenceManager.getDefaultSharedPreferences(this);

		getBackupPin = settings.getString("passBackupPref", null);
		if (getBackupPin == null)
		{
			Toast.makeText(
					this,
					"A PIN does not exist.\nPlease enter it in the settings menu.",
					Toast.LENGTH_LONG).show();
		}

		sensorManagerUnlock = (SensorManager) getSystemService(SENSOR_SERVICE);

		lastUpdate = System.currentTimeMillis();

	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			try
			{
				getAccelerometer(event);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void getAccelerometer(SensorEvent event) throws Exception
	{

		String direction = null;
		float[] values = event.values;
		// Movement
		float x = values[0];
		float y = values[1];
		float z = values[2];

//		float accelationSquareRoot = (x * x + y * y + z * z)
//				/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

		long actualTime = System.currentTimeMillis();
		//long diffTime = 0l;
		// if(instructionTouched == true)
		// {
		if ((actualTime - lastUpdate) > 100) // From 100
		{
			diffTime = actualTime - lastUpdate;
			lastUpdate = actualTime;
			//System.out.println("here");
		}
		else
		{
			return;
		}
		
		boolean draw = false;
		
        float x_speed = Math.abs(x - last_x) / diffTime * 10000;
        float y_speed = Math.abs(y - last_y) / diffTime * 10000;
        
        if (x - last_x < 0)
        {
        	x_direction = "Right";
        }
        else
        {
        	x_direction = "Left";
        }
        
        if (y - last_y < 0)
        {
        	y_direction = "Forward";
        }
        else
        {
        	y_direction = "Backward";
        }
        
        
                
        if (x_speed > y_speed && x_speed > 200)
        {
        	if (x_direction != "")
        	{
        		System.out.println("shook with direction " + x_direction + " with speed of " + x_speed + " (y_speed = " + y_speed + ")");
        		
        		if (x_direction.equals("Right"))
        		{
        			direction = "Right";
        			displayArrowUnlock.setImageResource(R.drawable.newright);
					Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
					sensorManagerUnlock.unregisterListener(this);
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sensorManagerUnlock.registerListener(this, sensorManagerUnlock
							.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							SensorManager.SENSOR_DELAY_NORMAL);
        		}
        		else
        		{
        			direction = "Left";
        			displayArrowUnlock.setImageResource(R.drawable.newleft);
        			Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
        			sensorManagerUnlock.unregisterListener(this);
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sensorManagerUnlock.registerListener(this, sensorManagerUnlock
							.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							SensorManager.SENSOR_DELAY_NORMAL);
        		}
        	}
        	
        	draw = true;
        	count++;
        }
        else if (y_speed > 200)
        {
        	if (y_direction != "")
        	{
        		System.out.println("shook with direction " + y_direction + " with speed of " + y_speed + " (x_speed = " + x_speed + ")");
        		
        		if (y_direction.equals("Forward"))
        		{
        			direction = "Forward";
        			displayArrowUnlock.setImageResource(R.drawable.newforward);
    				Toast.makeText(this, "Forward", Toast.LENGTH_SHORT).show();
    				sensorManagerUnlock.unregisterListener(this);
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sensorManagerUnlock.registerListener(this, sensorManagerUnlock
							.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							SensorManager.SENSOR_DELAY_NORMAL);
        		}
        		else
        		{
        			direction = "Back";
        			displayArrowUnlock.setImageResource(R.drawable.newback);
    				Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
    				sensorManagerUnlock.unregisterListener(this);
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sensorManagerUnlock.registerListener(this, sensorManagerUnlock
							.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
							SensorManager.SENSOR_DELAY_NORMAL);
        		}
        	}
        	
        	draw = true;
        	count++;
        }
        	
        last_x = x;
        last_y = y;
        last_z = z;
        
		//System.out.println("0000000000000000000000000000000000000000000000");

		//System.out.println(displayArrow.getDrawable())

			/*****************************************************************************/
			// If direction != null, append to the current attemptedPassword try

			// If it is the fourth motion, check it against saved password
			// Check that weird NULL case, automatically fail the validation
			// Otherwise, check against the saved password
			// run the current attemptedPassword through the m5 encoder to get
			// the string, compare against saved password
			// If correct, exit activity

			// Else If it is the Fifth attempt, fail out to PIN entry
			// Unregister the accelerometer listener
			// Pop-up requesting the PIN if available
			// If PIN is incorrect or D.N.E., lock for 5 minutes? Go back to
			// unlock motion controls?
			/*****************************************************************************/

			if (displayArrowUnlock.getDrawable() != null)
			{

				if (numOfTries < 3)
				{
					// If direction != null, append to the current
					// attemptedPassword try
					if (direction != null)
					{
						attemptedPassword.append(direction);
						sensorManagerUnlock
								.registerListener(
										this,
										sensorManagerUnlock
												.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
										SensorManager.SENSOR_DELAY_NORMAL);
					}

					// If it is the fourth motion, check it against saved
					// password
					if (count % 4 == 0)
					{
						sensorManagerUnlock.unregisterListener(this);

						// run the current attemptedPassword through the m5
						// encoder to get the string, compare against saved
						// password
						Toast.makeText(this, attemptedPassword.toString(),
								Toast.LENGTH_SHORT).show();
						String encryptedAttemptedPassword = Util
								.md5(attemptedPassword.toString().trim());

						// Toast.makeText(getApplicationContext(),
						// encryptedAttemptedPassword +"\n" + passwordSaved,
						// Toast.LENGTH_LONG).show();
						Log.d("Encryptedpassrecent", encryptedAttemptedPassword);
						Log.d("savedEncrypted", passwordSaved);
						if (encryptedAttemptedPassword.equals(passwordSaved))
						{
							// Password correct, exit application
							Log.d("DONE", "MATCH");
							setResult(0);
							finish();

						} else
						{
							// TODO: This could be cleaner...if I made the
							// if(commaCount == 3), then could just put those
							// things in the body of the if after the if block

							// Wrong password
							attemptedPassword.setLength(0);
							messageUnlock.setTextColor(Color.RED);
							messageUnlock
									.setText("Incorrect.  Please try again.");
						}

						numOfTries++;
						Toast.makeText(getApplicationContext(),
								"numofTries = " + numOfTries,
								Toast.LENGTH_SHORT).show();
						sensorManagerUnlock
								.registerListener(
										this,
										sensorManagerUnlock
												.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
										SensorManager.SENSOR_DELAY_NORMAL);

					}
				}
				// Else if it is the fifth attempt, fail out to the PIN entry
				else
				{
					// Unregister the accelerometer listener
					sensorManagerUnlock.unregisterListener(this);

					// Pop-up requesting the PIN if available
					if (getBackupPin != null)
					{

						
						final EditText passTextBox = new EditText(this);
						final AlertDialog.Builder pinDialog = new AlertDialog.Builder(this).setView(passTextBox);
						pinDialog.setMessage("Enter your pin number.");
						
						pinDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								
								
							}
						});
						
						pinDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								
								
							}
						});
						
						final AlertDialog dialog = pinDialog.create();
						dialog.show();
						
						//Overriding regular behavior of Android alert dialog.
						//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
				        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
				              {            
				                  @Override
				                  public void onClick(View v)
				                  {
				                      Boolean wantToCloseDialog = false;
				                      
				                      Editable attemptedPin = passTextBox.getEditableText();
				                      
				                      
				                      if(attemptedPin.toString().trim().equals(getBackupPin.toString().trim()))
				                      {
				                    	  //finish the activity. Go to home screen.
				                    	  finish();
				                      }
				                      else
				                      {
				                    	  passTextBox.setError("Incorrect pin! Please try again.");
				                    	  wantToCloseDialog = false;
				                      }
				                      if(wantToCloseDialog)
				                          dialog.dismiss();
				                      
				                  }
				              });

				        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
				          {            
				              @Override
				              public void onClick(View v)
				              {
				                  dialog.dismiss();
				              }
				          });
						
								
				
						// If PIN is incorrect, cancelled or D.N.E., lock for 5
						// minutes? Go back to unlock motion controls? Go back
						// to motion controls.
						numOfTries = 0;
						attemptedPassword.setLength(0);
						sensorManagerUnlock
								.registerListener(
										this,
										sensorManagerUnlock
												.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
										SensorManager.SENSOR_DELAY_NORMAL);
					}

				}
			} else if (draw == true)
			{
				Toast.makeText(this, "Sensor interference. Try again",
						Toast.LENGTH_SHORT).show();

				displayArrowUnlock.setImageResource(0);

				count--;

				attemptedPassword.setLength(0);

				startActivity(getIntent());
				sensorManagerUnlock.registerListener(UnlockActivity.this,
						sensorManagerUnlock
								.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_NORMAL);

			}
		}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		sensorManagerUnlock
				.registerListener(this, sensorManagerUnlock
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause()
	{
		// unregister listener
		super.onPause();
		sensorManagerUnlock.unregisterListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{

		case R.id.settings:
			// go to settings prefs.
			startActivity(new Intent(this, PrefsActivity.class));
			return true;

		case R.id.about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}