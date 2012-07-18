package com.ajaykumar.orientation.example;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

// Mostly from https://groups.google.com/forum/?fromgroups#!topic/android-beginners/V4pOfLn8klQ

public class MainActivity extends Activity implements SensorEventListener {
	TextView data0, data1, data2;
	private SensorManager mSensMan;
	private float[] mGravs = new float[3];
	private float[] mGeoMags = new float[3];
	private float[] mOrientation = new float[3];
	private float[] mRotationM = new float[9];
	private boolean mFailed;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		data0 = (TextView) findViewById(R.id.data0);
		data1 = (TextView) findViewById(R.id.data1);
		data2 = (TextView) findViewById(R.id.data2);

		// Initiate the Sensor Manager and register this as Listener for the
		// required sensor types:
		// TODO: Find how to get a SensorManager outside an Activity, to
		// implement as a utility class.
		mSensMan = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensMan.registerListener(this,
				mSensMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);
		// Anonymous Sensors-no further use for them.
		mSensMan.registerListener(this,
				mSensMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			System.arraycopy(event.values, 0, mGravs, 0, 3);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for (int i = 0; i < 3; i++)
				mGeoMags[i] = event.values[i];
			break;
		default:
			return;
		}

		if (SensorManager.getRotationMatrix(mRotationM, null, mGravs, mGeoMags)) {
			SensorManager.getOrientation(mRotationM, mOrientation);
			onSuccess();
		} else
			onFailure();
	}

	void onSuccess() {
		if (mFailed)
			mFailed = false;

		data0.setText("1: " + Math.round(Math.toDegrees(mOrientation[0])));
		data1.setText("2: " + Math.round(Math.toDegrees(mOrientation[1])));
		data2.setText("3: " + Math.round(Math.toDegrees(mOrientation[2])));
	}

	void onFailure() {
		if (!mFailed) {
			mFailed = true;
		}
	}
}