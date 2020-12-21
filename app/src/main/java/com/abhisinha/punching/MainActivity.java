package com.abhisinha.punching;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    public EditText emp_id,name;
    Button punch,viewbutton;
    //Define fields for Google API Client
    private FusedLocationProviderClient mFusedLocationClient;
    private Location lastLocation;
    private LocationRequest locationRequest;

    private LocationCallback mLocationCallback;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 14;
    DBHelper dbHelper;
    private int mYear, mMonth, mDay, mHour, mMinute;
  public   List<Datum> data;
    public String address;
   public String strAdd;
    RecyclerView recyclerView;
    myAdapter adapter;


    String date1,time1,name1,id1,address1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new DBHelper(this);

        emp_id=findViewById(R.id.editTextuniqueId);
        name=findViewById(R.id.editTextnameId);
        punch=findViewById(R.id.buttonId);
        viewbutton=findViewById(R.id.view_all_id);
        recyclerView=findViewById(R.id.recyclerView_id);
        data=new ArrayList<>();

        // location
        try
        {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
//                                 txtAddress.setText(mResultReceiver.getAddress());
                            }
                        }
                    });
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


            mLocationCallback = new LocationCallback()
            {
                @Override
                public void onLocationResult(LocationResult locationResult)
                {
                    for (Location location : locationResult.getLocations()) {
                        // Update UI with location data

                        double lat = location.getLatitude();
                        double lan = location.getLatitude();

                        Log.e("TAG", "onLocationResult: " + lat);


                     /*   // lan lon decoder
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(lat, lan, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); //

                            Log.e("TAG", "onLocationResult: " + city);

                        } catch (IOException e) {
                            Log.e("TAG", "onLocationResult: error" + e.getMessage());
                            e.printStackTrace();
                        }*/


                        //getCompleteAddressString(lat,lan);

                    }
                }
            };
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return view;




        punch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String n = name.getText().toString();
                String i = emp_id.getText().toString();

                if(n.equals("")){
                    name.setError("Please Enter Your Name.");

                }else if(i.equals("")){
                    emp_id.setError("Please Enter Your ID");
                }
                else {


                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    String date = mDay + "-" + mMonth + "-" + mYear;


                    // Get Current Time
                    final Calendar t = Calendar.getInstance();
                    mHour = t.get(Calendar.HOUR_OF_DAY);
                    mMinute = t.get(Calendar.MINUTE);

                    String time = mHour + ":" + mMinute;


                    //SQLiteDatabase sqLiteDatabase= dbHelper.getWritableDatabase();
                    long count = dbHelper.empInsert(i, n, date, time, strAdd);

                    if (count == -1) {
                        Toast.makeText(getApplicationContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getApplicationContext(), "Sucessuflly punch !", Toast.LENGTH_LONG).show();
                        emp_id.setText("");
                        name.setText("");
                        punch.setEnabled(false);
                        punch.setAlpha(0.5f);

                    }


                }


            }
        });



                viewbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cursor = dbHelper.displayAllData();

                        if (cursor.getCount() == 0) {

                            showData("Error !", "No Data Found");


                            return;
                        }
                        StringBuffer stringBuffer = new StringBuffer();
                        while (cursor.moveToNext()) {
                            stringBuffer.append("Date :" + cursor.getString(3) + "\n\n");
                            stringBuffer.append("Time :" + cursor.getString(4) + "\n\n");
                            stringBuffer.append("Employee Id :" + cursor.getString(2) + "\n\n");
                            stringBuffer.append("Name :" + cursor.getString(1) + "\n\n");
                            stringBuffer.append("Address :" + cursor.getString(5) + "\n\n\n\n");

                            Datum datum = new Datum();
                            datum.setDate("Date :    " + cursor.getString(3));
                            datum.setTime("Time :   " + cursor.getString(4));
                            datum.setId("Employee Id :  " + cursor.getString(2) + "\n\n");
                            datum.setName("Name :   " + cursor.getString(1));
                            datum.setAddress("Address : " + cursor.getString(5));


                            data.add(datum);


                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                        adapter = new myAdapter(getApplicationContext(), data);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);

                        viewbutton.setEnabled(false);
                        viewbutton.setAlpha(0.5f);

                     
                    }


                });

            }




    private void showData(String title, String data) {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(data);
        builder.setCancelable(true);
        builder.show();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            startLocationUpdates();
            requestPermissions();
        } else {
            getLastLocation();
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("TAG", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("TAG", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {

            }
        }
    }



    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(MainActivity.this, android. Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("TAG", "Displaying permission rationale to provide additional context.");

            startLocationPermissionRequest();


        } else {
            Log.i("TAG", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */




    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            lastLocation = task.getResult();

                            double lat=lastLocation.getLatitude();
                            double lon=lastLocation.getLongitude();

                            Log.e("TAG", "onComplete: "+lat+"-----"+lon );
                            getCompleteAddressString(lat,lon);

                        } else {
                            Log.w("TAG", "getLastLocation:exception", task.getException());
                            // showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,  android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }


    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
         strAdd = "";

        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.e("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current loction address", "Canont get Address!"+e.getMessage());
        }
        return strAdd;






    }
}
