package com.example.computer.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class detail_Rcy extends AppCompatActivity implements SensorEventListener {
    TextView name, price;
    ImageView img_sp;
    Button btnHome;
    private SensorManager sensorManager;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__rcy);

        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        img_sp = (ImageView) findViewById(R.id.img_sp);
        btnHome = (Button) findViewById(R.id.btnHome);
        Intent intent = getIntent();
        String name_sp = intent.getStringExtra("name_detail");
        String price_detail = intent.getStringExtra("price_detail");
//        Toast.makeText(this, intent.getStringExtra("img_detail"), Toast.LENGTH_SHORT).show();
//        if(getIntent().hasExtra("img_detail")) {
//            Bitmap bmp = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("img_detail"), 0, getIntent().getByteArrayExtra("img_detail").length);
//            img_sp.setImageBitmap(bmp);
//        }
        // int image = MyData.drawableArray[MyData.selected_id];
        name.setText(name_sp);
        price.setText(price_detail);
//        img_sp.setImageResource(image);
        //sensorManager = (SensorManager) getSystemService(SEARCH_SERVICE);

//        img_sp.setImageResource(img_detail);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            getAcce
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
