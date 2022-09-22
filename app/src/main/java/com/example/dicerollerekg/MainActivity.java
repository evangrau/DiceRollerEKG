package com.example.dicerollerekg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView imageViewDice;
    private Random rng = new Random();

    // Device shake stuff
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable, isNotFirstRun = false;
    private float lastX;
    private float lastY;
    private float lastZ;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
        }
        else {
            isAccelerometerSensorAvailable = false;
        }

        imageViewDice = findViewById(R.id.image_view_dice);
        imageViewDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
            }
        });
    }

    private void rollDice() {
        int randomNumber = rng.nextInt(6) + 1;

        switch (randomNumber) {
            case 1:
                imageViewDice.setImageResource(R.drawable.dice1);
                break;
            case 2:
                imageViewDice.setImageResource(R.drawable.dice2);
                break;
            case 3:
                imageViewDice.setImageResource(R.drawable.dice3);
                break;
            case 4:
                imageViewDice.setImageResource(R.drawable.dice4);
                break;
            case 5:
                imageViewDice.setImageResource(R.drawable.dice5);
                break;
            case 6:
                imageViewDice.setImageResource(R.drawable.dice6);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float currentX = sensorEvent.values[0];
        float currentY = sensorEvent.values[1];
        float currentZ = sensorEvent.values[2];

        if (isNotFirstRun) {
            float xDifference = Math.abs(lastX - currentX);
            float yDifference = Math.abs(lastY - currentY);
            float zDifference = Math.abs(lastZ - currentZ);

            float shakeThreshold = 5f;
            if ((xDifference > shakeThreshold && yDifference > shakeThreshold) || (xDifference > shakeThreshold && zDifference > shakeThreshold) || (yDifference > shakeThreshold && zDifference > shakeThreshold)) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        isNotFirstRun = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAccelerometerSensorAvailable) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAccelerometerSensorAvailable) {
            sensorManager.unregisterListener(this);
        }
    }
}