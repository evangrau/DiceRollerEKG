package com.example.dicerollerekg;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView imageViewDice;
    private Random rng = new Random();

    // Device shake stuff
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable, isNotFirstRun = false;
    private float lastX, lastY, lastZ;
    private Vibrator vibrator;
    private MediaPlayer tap_roll, shake_roll, crit_sound, miss_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tap_roll = MediaPlayer.create(this, R.raw.tap_roll);
        shake_roll = MediaPlayer.create(this, R.raw.shake_roll);
        crit_sound = MediaPlayer.create(this, R.raw.hit);
        miss_sound = MediaPlayer.create(this, R.raw.miss);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
        }
        else {
            isAccelerometerSensorAvailable = false;
        }

//        tap_roll.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                tap_roll.reset();
//                tap_roll.release();
//            }
//        });
//        shake_roll.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                shake_roll.reset();
//                shake_roll.release();
//            }
//        });
//        crit_sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                crit_sound.reset();
//                crit_sound.release();
//            }
//        });
//        miss_sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                miss_sound.reset();
//                miss_sound.release();
//            }
//        });

        imageViewDice = findViewById(R.id.image_view_dice);
        imageViewDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
                tap_roll.start();
            }
        });
    }

    private void rollDice() {
        int randomNumber = rng.nextInt(20) + 1;
        TextView crit_hit = findViewById(R.id.criticalHit);
        TextView crit_miss = findViewById(R.id.criticalMiss);
        crit_hit.setVisibility(View.INVISIBLE);
        crit_miss.setVisibility(View.INVISIBLE);

        switch (randomNumber) {
            case 1:
                imageViewDice.setImageResource(R.drawable.d20_1);
                crit_miss.setVisibility(View.VISIBLE);
                miss_sound.start();
                break;
            case 2:
                imageViewDice.setImageResource(R.drawable.d20_2);
                break;
            case 3:
                imageViewDice.setImageResource(R.drawable.d20_3);
                break;
            case 4:
                imageViewDice.setImageResource(R.drawable.d20_4);
                break;
            case 5:
                imageViewDice.setImageResource(R.drawable.d20_5);
                break;
            case 6:
                imageViewDice.setImageResource(R.drawable.d20_6);
                break;
            case 7:
                imageViewDice.setImageResource(R.drawable.d20_7);
                break;
            case 8:
                imageViewDice.setImageResource(R.drawable.d20_8);
                break;
            case 9:
                imageViewDice.setImageResource(R.drawable.d20_9);
                break;
            case 10:
                imageViewDice.setImageResource(R.drawable.d20_10);
                break;
            case 11:
                imageViewDice.setImageResource(R.drawable.d20_11);
                break;
            case 12:
                imageViewDice.setImageResource(R.drawable.d20_12);
                break;
            case 13:
                imageViewDice.setImageResource(R.drawable.d20_13);
                break;
            case 14:
                imageViewDice.setImageResource(R.drawable.d20_14);
                break;
            case 15:
                imageViewDice.setImageResource(R.drawable.d20_15);
                break;
            case 16:
                imageViewDice.setImageResource(R.drawable.d20_16);
                break;
            case 17:
                imageViewDice.setImageResource(R.drawable.d20_17);
                break;
            case 18:
                imageViewDice.setImageResource(R.drawable.d20_18);
                break;
            case 19:
                imageViewDice.setImageResource(R.drawable.d20_19);
                break;
            case 20:
                imageViewDice.setImageResource(R.drawable.d20_20);
                crit_hit.setVisibility(View.VISIBLE);
                crit_sound.start();
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
                rollDice();
                shake_roll.start();
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