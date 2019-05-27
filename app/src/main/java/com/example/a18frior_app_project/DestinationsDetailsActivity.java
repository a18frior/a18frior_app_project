package com.example.a18frior_app_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class DestinationsDetailsActivity extends AppCompatActivity {
    //Hämtar bild från URL
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations_details);

        //Hämtar data skickad från MainActivity
        Intent i = getIntent();
        String Info = i.getStringExtra("Destinationsinfo");
        String Info2 = "<b>Things to do:</b> " + i.getStringExtra("Destinationsinfo2");
        String Image = i.getStringExtra("DestinationsImage");
        //Visar bild i ImageView
        new DownloadImageTask((ImageView) findViewById(R.id.image_view))
                .execute(Image);
        //Visar text i textView
        TextView DestinationsInfo = findViewById(R.id.text_1);
        TextView DestinationsInfo2 = findViewById(R.id.text_2);
        DestinationsInfo.setText(Info);
        DestinationsInfo2.setText(Html.fromHtml(Info2));
        Button button = (Button) findViewById(R.id.button);

        //Skickar användaren till Skyscanner.se Onclick
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebView webView = (WebView) findViewById(R.id.WebView_Dash);
                WebSettings webSettings = webView.getSettings();
                ((WebSettings) webSettings).setJavaScriptEnabled(true);
                webView.loadUrl("https://www.skyscanner.se/?ksh_id=_k_Cj0KCQjwz6PnBRCPARIsANOtCw0pKPFUQdjDG7yuy5mKckANAHI-rKMROWNspC8zqOIlWAu9D0BDdNYaAiGCEALw_wcB_k_&associateID=SEM_GGT_00065_00028&utm_source=google&utm_medium=cpc&utm_campaign=SE-Travel-Search-Brand-Skyscanner%20Only-Broad&utm_term=%2Bskyscanner&kpid=google_357837394_22396872634_176565598303_aud-326758276458:kwd-22351616371_c_&gclid=Cj0KCQjwz6PnBRCPARIsANOtCw0pKPFUQdjDG7yuy5mKckANAHI-rKMROWNspC8zqOIlWAu9D0BDdNYaAiGCEALw_wcB");
            }
        });


    }


}

