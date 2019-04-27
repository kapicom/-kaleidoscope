package com.example.kaleidoscope;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {
    private VideoView mVideoView;
    //センサーマネージャー
    private SensorManager sensorManager;
    //加速度センサーにリスナーとして自身を登録済みか否か
    private boolean registerAccelerometer;
    //磁気センサーにリスナーとして...
    private boolean registerMagneticFieldSensor;
    //加速度センサーが返す値
    private float[] accels = new float[3];
    //磁気センサーが返す値
    private float[] magneticFields = new float[3];
    //傾きのラジアン値
    private float[] orientations = new float[3];
    Uri[] uri=new Uri[12];
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uri[0] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video1);
        uri[1] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video2);
        uri[2] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video3);
        uri[3] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video4);
        uri[4] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video5);
        uri[5] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video6);
        uri[6] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video7);
        uri[7] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video8);
        uri[8] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video9);
        uri[9] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video10);
        uri[10] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video11);
        uri[11] = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video12);


        //センサーマネージャーを取得
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVideoView = (VideoView) findViewById(R.id.VideoView00);
        mVideoView.setVideoURI(uri[1]);
        mVideoView.start();
        registerAccelerometer = false;
        registerMagneticFieldSensor = false;
        for (int i = 0; i < 3; i++) {
            accels[i] = 0.0f;
            magneticFields[i] = 0.0f;
            orientations[i] = 0.0f;
        }
        mVideoView.setVideoURI(uri[0]);
        mVideoView.start();
        count=1;
    }

    @Override
    protected void onPause() {
        //アプリを落としたとき（一位停止)
        if (registerAccelerometer || registerMagneticFieldSensor) {
            sensorManager.unregisterListener((SensorEventListener) this); //センサーを外す

            registerAccelerometer = false;
            registerMagneticFieldSensor = false;
        }
        super.onPause();
    }

    @Override
    protected void onResume() { //一時停止から戻ってきたとき加速度センサーを復活させる
        if (!registerAccelerometer) {
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0) {
                registerAccelerometer = sensorManager.registerListener(
                        (SensorEventListener) this, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        if (!registerMagneticFieldSensor) {
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);

            if (sensors.size() > 0) {
                registerMagneticFieldSensor = sensorManager.registerListener((SensorEventListener) this, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        super.onResume();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //加速度センサ処理
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //磁力センサ処理
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            for (int i = 0; i < 3; i++)
                accels[i] = event.values[i];
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            for (int i = 0; i < 3; i++) {
                magneticFields[i] = event.values[i];
            }
            float[] R = new float[9];
            float[] I = new float[9];

            SensorManager.getRotationMatrix(R, I, accels, magneticFields);
            SensorManager.getOrientation(R, orientations);
            int Azimuth = (int)Math.toDegrees(orientations[0]);
            int Pitch = (int)Math.toDegrees(orientations[1]);
            int Roll = (int)Math.toDegrees(orientations[2]);

            if(Roll>40){
                if(count==12){
                    count=0;
                }
                mVideoView.setVideoURI(uri[count]);
                mVideoView.start();
                count++;
            }
        }
    }
}
