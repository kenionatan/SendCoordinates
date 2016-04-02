package com.example.administrador.app3;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {


    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    //private PendingIntent pendint;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);


                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                //pendint = new PendingIntent(){

                    @Override
                    public void onLocationChanged(Location location) {

                        double latit = location.getLatitude();
                        double longit = location.getLongitude();

                        final String lat = String.valueOf(latit);
                        final String lng = String.valueOf(longit);

                        class AddCoord extends AsyncTask<Void,Void,String>{
                            ProgressDialog loading;

                            @Override
                            protected void onPreExecute(){
                                super.onPreExecute();
                                loading = ProgressDialog.show(MainActivity.this,"Adding...","Wait...",false,false);
                            }
                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                loading.dismiss();
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                            }
                            @Override
                            protected String doInBackground(Void... v) {
                                HashMap<String,String> params = new HashMap<>();
                                params.put(Config.KEY_EMP_LAT,lat);
                                params.put(Config.KEY_EMP_LNG,lng);
                                //params.put(Config.KEY_EMP_SAL, sal);

                                RequestHandler rh = new RequestHandler();
                                String res = rh.sendPostRequest(Config.URL_ADD, params);
                                return res;
                            }
                        }

                        AddCoord ac = new AddCoord();
                        ac.execute();


                    }




                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {



                    }

                    @Override
                    public void onProviderDisabled(String provider) {


                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10 );
                        return;
                    }
                }else{

                    locationManager.requestLocationUpdates("gps", 300000, 0, locationListener);
                    //locationManager.requestLocationUpdates("gps", 300000, 0, pendint);

                }



            }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates("gps", 300000, 0, locationListener);
                    //locationManager.requestLocationUpdates("gps", 300000, 0, pendint);

        }
    }



}