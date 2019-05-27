package com.example.a18frior_app_project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.jar.Attributes;

import okhttp3.internal.Util;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();
    //Lagrar alla destinations från databasen
    ArrayList<Destinations> DestinationsArray = new ArrayList();
    //Lagrar alla berg för listview
    ArrayList<HashMap<String, String>> DestinationsListView = new ArrayList<HashMap<String, String>>();
    //Egen adapter
    private SimpleAdapter adapter2;
    // Ny Databashanterare
    DestinationsReaderDbHelper db = new DestinationsReaderDbHelper(this);


    private class FetchData extends AsyncTask<Void, Bitmap, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=a18frior");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }

        }


        protected void onPostExecute(String o) {

            super.onPostExecute(o);
            String s1 = o;




            try {


                JSONArray destinations = new JSONArray(s1);

                //Får ut alla Destinations variabler från JSON-objektet
                for (int i = 0; i < destinations.length(); i++) {
                    JSONObject json1 = destinations.getJSONObject(i);

                    String Location = json1.getString("location");

                    String Name = json1.getString("name");

                    String Citizen = json1.getString("size");

                    String Category = json1.getString("category");

                    String Image = json1.getString("auxdata");

                    String Info = json1.getString("company");



                    Destinations d = new Destinations(Name, Location, Citizen, Image, Category, Info);


                    if (db.getDestinations(Name) == null) {
                        db.addDestination(d);
                    }
                }

            } catch (Exception e) {
                Log.d("jennas log", "E:" + e.getMessage());
            }


            //Fyller Arraylist DestinationsArray med alla destinationer
            DestinationsArray = db.getAllDestinations();


            HashMap<String, String> item;

            //Fyller ArrayList DestinationsListView med Name,Location och Category för varje destination.
            for (int i = 0; i < DestinationsArray.size(); i++) {

                item = new HashMap<>();
                item.put("line1", DestinationsArray.get(i).getName());
                item.put("line2", DestinationsArray.get(i).getLocation());
                item.put("line3", DestinationsArray.get(i).getCategory());

                DestinationsListView.add(item);

            }

            //adapter2 visar line1,line2 och line3 i listview
            adapter2 = new SimpleAdapter(MainActivity.this, DestinationsListView,
                    R.layout.destinations_item,
                    new String[]{"line1", "line2", "line3"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c}


            );

            ListView listView = findViewById(R.id.DestinationsListView);
            listView.setAdapter(adapter2);

            //Skickar info,url för bild och extra information och startar second activity onClick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent appInfo = new Intent(MainActivity.this, DestinationsDetailsActivity.class);

                    appInfo.putExtra("Destinationsinfo", DestinationsArray.get(position).info());
                    appInfo.putExtra("Destinationsinfo2", DestinationsArray.get(position).getInfo());
                    appInfo.putExtra("DestinationsImage", DestinationsArray.get(position).getImage());


                    startActivity(appInfo);
                }
            });


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setIcon(getResources().getDrawable(R.mipmap.ic_icon2));


        new FetchData().execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //Hämtar text från about.html och presenterar denna i en toast
        if (id == R.id.action_About) {
            try {
                InputStream is = getAssets().open("about.html");
                int size = is.available();

                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                String aboutHtml = new String(buffer);
                aboutHtml = aboutHtml.replace("old string", "new string");
                Spanned about = Html.fromHtml(aboutHtml);
                for (int i = 0; i < 2; i++) {

                    Toast.makeText(getApplicationContext(), about, Toast.LENGTH_LONG).show();


                }


            } catch (Exception e) {

                e.printStackTrace();
            }


            return true;
        }
        //Uppdaterar listView och visar enbart destinationer i Europa
        if (id == R.id.action_Destinations) {

            DestinationsListView.clear();
            HashMap<String, String> item2;

            for (int i = 0; i < DestinationsArray.size(); i++) {
                String Cate;
                Cate = DestinationsArray.get(i).getCategory();
                if (Cate.equals("Europe")) {
                    item2 = new HashMap<>();
                    item2.put("line1", DestinationsArray.get(i).getName());
                    item2.put("line2", DestinationsArray.get(i).getLocation());
                    item2.put("line3", DestinationsArray.get(i).getCategory());

                    Log.d("jennas log", "" + DestinationsArray.get(i).getCategory());


                    DestinationsListView.add(item2);

                }
            }


            adapter2 = new SimpleAdapter(MainActivity.this, DestinationsListView,
                    R.layout.destinations_item,
                    new String[]{"line1", "line2", "line3"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c}
            );

            ListView lista = findViewById(R.id.DestinationsListView);
            lista.setAdapter(adapter2);

            return true;
        }
        //Uppdaterar listView och visar enbart destinationer i Asien
        if (id == R.id.action_Destinations2) {

            DestinationsListView.clear();
            HashMap<String, String> item2;

            for (int i = 0; i < DestinationsArray.size(); i++) {
                String Cate;
                Cate = DestinationsArray.get(i).getCategory();
                if (Cate.equals("Asia")) {
                    item2 = new HashMap<>();
                    item2.put("line1", DestinationsArray.get(i).getName());
                    item2.put("line2", DestinationsArray.get(i).getLocation());
                    item2.put("line3", DestinationsArray.get(i).getCategory());

                    Log.d("jennas log", "" + DestinationsArray.get(i).getCategory());


                    DestinationsListView.add(item2);

                }
            }


            adapter2 = new SimpleAdapter(MainActivity.this, DestinationsListView,
                    R.layout.destinations_item,
                    new String[]{"line1", "line2", "line3"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c}
            );

            ListView lista = findViewById(R.id.DestinationsListView);
            lista.setAdapter(adapter2);

            return true;
        }
        //Uppdaterar listView och visar alla destinationer
        if (id == R.id.action_refresh) {

            DestinationsListView.clear();
            DestinationsArray.clear();
            new FetchData().execute();

            return true;
        }
        //Uppdaterar listView och visar alla destinationer i bokstavsordning
        if (id == R.id.action_Sort) {

            DestinationsListView.clear();
            DestinationsArray.clear();
            DestinationsArray = db.getAllDestinations();
            Collections.sort(DestinationsArray, new DestinationsNameComparator());
            HashMap<String, String> item2;
            for (int i = 0; i < DestinationsArray.size(); i++) {
                item2 = new HashMap<>();
                item2.put("line1", DestinationsArray.get(i).getName());
                item2.put("line2", DestinationsArray.get(i).getLocation());
                item2.put("line3", DestinationsArray.get(i).getCategory());
                DestinationsListView.add(item2);

            }
            adapter2 = new SimpleAdapter(MainActivity.this, DestinationsListView,
                    R.layout.destinations_item,
                    new String[]{"line1", "line2", "line3"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c}
            );

            ListView lista = findViewById(R.id.DestinationsListView);
            lista.setAdapter(adapter2);


            return true;
        }

        return super.onOptionsItemSelected(item);

    }


}
