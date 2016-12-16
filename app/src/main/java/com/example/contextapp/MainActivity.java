package com.example.contextapp;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity  {
	GoogleMap map;
	
	LocationManager locationmanager ;
	TextView place,speed,temperature;
	Sensor temperaturesensor;
	SensorManager DeviceSensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		place = (TextView) findViewById(R.id.place);
		speed = (TextView) findViewById(R.id.Speed);
		temperature = (TextView) findViewById(R.id.temperature);
		

		  map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
	        .getMap();

	//Κλήση μεθόδου για την προβολή της θέσης του χρήστη
		  
		DeviceSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		temperaturesensor = (Sensor) DeviceSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

		
		if (temperaturesensor!=null){
			DeviceSensorManager.registerListener(new SensorEventListener() {
				
				@Override
				public void onSensorChanged(SensorEvent event) {
					// TODO Auto-generated method stub
					if (event.sensor==temperaturesensor){
					temperature.setText(Float.toString(Math.round(event.values[0])));
					}
				}
				
				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
					// TODO Auto-generated method stub
					
				}
			}, temperaturesensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		else {
			temperature.setText("Temperature N/A");
		}
		
	


locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
if (LocationManager.GPS_PROVIDER!=null){
	if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)==true){
		locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20, new LocationListener() {			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
			if(location.getAccuracy()<20){
				Calendar moment = Calendar.getInstance();
				moment.setTimeInMillis(location.getTime());
				String date =""+ moment.get(Calendar.DAY_OF_MONTH)+"/"+Integer.toString(moment.get(Calendar.MONTH)+1)+"/"+moment.get(Calendar.YEAR);
				String time=""+ moment.get(Calendar.HOUR_OF_DAY)+ ":"+moment.get(Calendar.MINUTE)+":" + moment.get(Calendar.SECOND);
				
				
				map.addMarker(new MarkerOptions()
				.position(new LatLng(location.getLatitude(), location.getLongitude()) )
				.title(date)
				.snippet(time)
				);
				
				Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
				
				try {
					List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
					if(listAddresses!=null){
						if(listAddresses.size()>0){
							String locationinfo = listAddresses.get(0).getAddressLine(1);
							place.setText(locationinfo);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
		});
		locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
			if(location.getAccuracy()<20){
				speed.setText(Float.toString(Math.round(location.getSpeed())));
			}
		}
		});
		

		
	}
	
}
else {
	Toast.makeText(this, "GPS N/A", Toast.LENGTH_LONG).show();
}	 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
