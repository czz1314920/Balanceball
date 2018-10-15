package com.example.balanceball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyBallView extends View {
    private float x;
    private  float y;
    private final int r;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public MyBallView(Context context, float x, float y, int r){
        super(context);
        paint.setColor(Color.RED);
        this.x = x;
        this.y = y;
        this.r = r;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawCircle(x, y, r, paint);
    }
    public void updatePosition(float x, float y){
        this.x = x;
        this.y = y;
    }
}
