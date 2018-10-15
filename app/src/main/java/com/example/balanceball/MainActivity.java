package com.example.balanceball;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;
    private Sensor accelerometer;
    private MyBallView ball = null;
    private Handler redrawHandler = new Handler();
    private Timer moveTimer = null;
    private TimerTask moveTask = null;
    private int sWidth, sHeight;
    private PointF ballPos, ballSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FrameLayout board = (FrameLayout) findViewById(R.id.gameboard);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        sHeight = metrics.heightPixels;
        sWidth  = metrics.widthPixels;

//        Display display = getWindowManager().getDefaultDisplay();

        /* */
//        sWidth = display.getWidth();
//        sHeight = display.getWidth();

        /* */
//        Point size = new Point();
//        display.getSize(size);
//        sWidth = size.x;
//        sHeight = size.y;


        ballPos = new PointF();
        ballSpeed = new PointF();
        ballPos.x = sWidth/2;
        ballPos.y = sHeight/2;
        ballSpeed.x = 0;
        ballSpeed.y = 0;
        ball = new MyBallView(this,ballPos.x,ballPos.y,100);
        board.addView(ball);
        ball.invalidate();
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        board.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v,android.view.MotionEvent e){
                ballPos.x = e.getX();
                ballPos.y = e.getY();
                return true;
            }
        });
    }

    public void onResume() {
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        moveTimer = new Timer();
        moveTask = new TimerTask() {
            public void run() {
                Log.d("Balanceball", "更新时间 - " + ballPos.x + ":" + ballPos.y);
                ballPos.x += ballSpeed.x;
                ballPos.y += ballSpeed.y;
                float oX = 10 * Math.abs(ballSpeed.x);
                float oY = 10 * Math.abs(ballSpeed.y);
                if (ballPos.x > sWidth) ballPos.x -= oX;
                if (ballPos.y > sWidth) ballPos.y -= oY;
                if (ballPos.x < 0) ballPos.x += oX;
                if (ballPos.y < 0) ballPos.y += oY;
                ball.updatePosition(ballPos.x, ballPos.y);
                redrawHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ball.invalidate();
                    }
                });
            }};
        moveTimer.schedule(moveTask,10,2);
        super.onResume();
    }
    @Override
    public void onPause(){
        moveTimer.cancel();
        moveTimer = null;
        moveTask = null;
        super.onPause();
        manager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        ballSpeed.x = -event.values[0];
        ballSpeed.y = event.values[1];
    }
}

