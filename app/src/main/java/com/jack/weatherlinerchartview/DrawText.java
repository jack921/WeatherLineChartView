package com.jack.weatherlinerchartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jack on 2016/7/20.
 */


public class DrawText extends View {


    public DrawText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        float densityText = getResources().getDisplayMetrics().scaledDensity;
        Paint paint=new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(16*densityText);
        paint.setAntiAlias(true);
        canvas.drawText("jack",100,100,paint);

        Paint paint2=new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setTextSize(16*densityText);
        paint2.setAntiAlias(true);
        paint2.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("jack2",100,100,paint2);
    }

}
