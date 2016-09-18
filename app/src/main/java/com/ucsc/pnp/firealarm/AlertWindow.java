package com.ucsc.pnp.firealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

public class AlertWindow extends AppCompatActivity {
ImageView imageView;
    GetAlert getAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView=(ImageView)findViewById(R.id.imageView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        startService(new Intent(this, NetService.class));

        IntentFilter filter = new IntentFilter("com.ucsc.pnp.fire.CUSTOM");

        getAlert = new GetAlert();
        registerReceiver(getAlert, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(getAlert);
    }

    class GetAlert extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonmsg=intent.getStringExtra("data");
            Toast.makeText(getApplicationContext(),"Alert in",Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject=new JSONObject(jsonmsg);
                String immg=jsonObject.getString("img");
                byte[] decodedString = Base64.decode(immg, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }

}
