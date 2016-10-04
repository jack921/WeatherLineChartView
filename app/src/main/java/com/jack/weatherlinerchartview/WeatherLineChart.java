package com.jack.weatherlinerchartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2016/7/18.
 */

public class WeatherLineChart extends View {

    //圆点旁边字体的大小
    private int CircleTextSize;
    //字体颜色
    private int CircleTextColor;
    //高的温度的线的颜色
    private int MinLineColor;
    //低的温度的线的颜色
    private int MaxLineColor;
    //圆点的颜色
    private int CircleColor;
    //画线的画笔
    private Paint LinePaint;
    //画圆点的画笔
    private Paint CirclePaint;
    //画字的画笔
    private Paint TextPaint;
    //存储Max轴的数据
    private List<Float> YValueMax=new ArrayList<>();
    //存储Min轴的数据
    private List<Float> YValueMin=new ArrayList<>();
    //控件的高度
    private int ChartHeight=0;
    //控件的长度
    private int ChartWidth=0;
    //缓存X轴的数据
    private List<Float> XValueWidth=new ArrayList<>();
    //画出Y轴最大值的数据
    private List<Float> mYAxisMax=new ArrayList<>();
    //画出Y轴最小值的数据
    private List<Float> mYAxisMin=new ArrayList<>();
    //设置透明度
    private int ChartAlpha=0;
    //圆点的半径
    private float mRadius=0;
    //折线的粗细
    private float StrokeWidth=0;
    //文字和上下的边的间隔
    private float marginHeigh=0;

    private int WrapcontentWidth;
    private int WrapcontentHight;

    private Display display =null;

    public List<Float> getYValueMin() {
        return YValueMin;
    }

    public void setYValueMin(List<Float> YValueMin) {
        this.YValueMin = YValueMin;
    }

    public List<Float> getYValueMax() {
        return YValueMax;
    }

    public void setYValueMax(List<Float> YValueMax) {
        this.YValueMax = YValueMax;
    }

    public List<Float> getXValueWidth() {
        return XValueWidth;
    }

    public void setXValueWidth(List<Float> XValueWidth) {
        this.XValueWidth = XValueWidth;
    }

    public WeatherLineChart(Context context) {
        this(context,null);
    }

    public WeatherLineChart(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }


    public WeatherLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化各参数
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(
                attrs,R.styleable.WeatherLineChart,defStyleAttr,0);
        int numCount=typedArray.getIndexCount();
        for(int i=0;i<numCount;i++){
            int attr= typedArray.getIndex(i);
            switch(attr){
                case R.styleable.WeatherLineChart_MaxLineColor:
                      MaxLineColor=typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.WeatherLineChart_MinLineColor:
                      MinLineColor=typedArray.getColor(attr,Color.BLUE);
                    break;
                case R.styleable.WeatherLineChart_CircleTextColor:
                      CircleTextColor=typedArray.getColor(attr,Color.GRAY);
                    break;
                case R.styleable.WeatherLineChart_CircleTextSize:
                      CircleTextSize=typedArray.getDimensionPixelSize(attr,(int)TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,15,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.WeatherLineChart_CircleColor:
                      CircleColor=typedArray.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.WeatherLineChart_ChartAlpha:
                      ChartAlpha=typedArray.getInt(attr,220);
                    break;
            }
        }
        typedArray.recycle();

        float density=getResources().getDisplayMetrics().density;
        mRadius = 3 * density;
        StrokeWidth=density*3;
        marginHeigh=density*10;

        display=((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        WrapcontentWidth=display.getWidth();
        WrapcontentHight=display.getHeight();

        //初始化画线的画笔
        LinePaint=new Paint();
        LinePaint.setAntiAlias(true);
        LinePaint.setStyle(Paint.Style.STROKE);
        LinePaint.setStrokeWidth(StrokeWidth);
        LinePaint.setAlpha(ChartAlpha);

        //初始化画圆点的画笔
        CirclePaint=new Paint();
        CirclePaint.setAntiAlias(true);
        CirclePaint.setColor(CircleColor);
        CirclePaint.setAlpha(ChartAlpha);

        //初始化画字的画笔
        TextPaint=new Paint();
        TextPaint.setAntiAlias(true);
        TextPaint.setTextSize(CircleTextSize);
        TextPaint.setColor(CircleTextColor);
        TextPaint.setTextAlign(Paint.Align.CENTER);
        TextPaint.setAlpha(ChartAlpha);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthModel=MeasureSpec.getMode(widthMeasureSpec);
        int heightModel=MeasureSpec.getMode(heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width=0;
        int height=0;
        if(widthModel==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            width=(int)(getPaddingLeft()+getPaddingRight()+(WrapcontentWidth/3));
        }
        if(heightModel==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            height=(int)(getPaddingBottom()+getPaddingTop()+(WrapcontentHight/3));
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ChartHeight=getHeight();
        ChartWidth=getWidth();
        if(XValueWidth!=null&&mYAxisMax!=null&&mYAxisMin!=null){
            XValueWidth.clear();
            mYAxisMax.clear();
            mYAxisMin.clear();
        }
        //初始化X轴的值
        initXValueData();
        //初始化Y轴的值
        initYValueData();
        //画出最大值的线
        DrawLine(canvas,XValueWidth,mYAxisMax,YValueMax,true);
        //画出最小值得线
        DrawLine(canvas,XValueWidth,mYAxisMin,YValueMin,false);
    }

    //初始化X轴的值
    public void initXValueData(){
        //得到数据的个数
        int XNum=YValueMax.size();
        //得到距离最左边的距离
        float BaseWidth=ChartWidth/(XNum*2);
        //得到各点之间的间隔
        float tempWdith=BaseWidth*2;
        for(int i=0;i<XNum;i++){
            //得到各点的具体X轴坐标
            XValueWidth.add(BaseWidth);
            BaseWidth+=tempWdith;
        }
    }

    //初始化Y轴的值
    public void initYValueData(){
        //获取最大值
        float tempMax=YValueMax.get(0);
        //获取最小值
        float tempMin=YValueMax.get(0);

        //算出最高温度的最大值的最小值
        for(int i=1;i<YValueMax.size();i++){
            if(tempMax<YValueMax.get(i)){
                tempMax=YValueMax.get(i);
            }
            if(tempMin>YValueMax.get(i)){
                tempMin=YValueMax.get(i);
            }
        }

        //和最高温度的最大值和最小值比较进而得到所有数据的最大值和最小值
        for(int i=1;i<YValueMin.size();i++){
            if(tempMax<YValueMin.get(i)){
                tempMax=YValueMin.get(i);
            }
            if(tempMin>YValueMin.get(i)){
                tempMin=YValueMin.get(i);
            }
        }

      //温差
      float parts=tempMax-tempMin;
      //y轴一端到控件一端的距离
      float length = CircleTextSize+mRadius+marginHeigh;
      //y轴高度
      float yAxisHeight = ChartHeight-length*2;

        if(parts==0){
            //都为零没有温差
            for(int i=0;i<YValueMax.size();i++){
                mYAxisMax.add((float) (ChartHeight/2));
                mYAxisMin.add((float) (ChartHeight/2));
            }
        }else{
            //有温差
            //最小高度值
            float partVlaue=yAxisHeight/parts;
            for(int i=0;i<YValueMax.size();i++){
                //具体的Y轴坐标数据
                mYAxisMax.add(ChartHeight-partVlaue*(YValueMax.get(i)-tempMin)-length);
                mYAxisMin.add(ChartHeight-partVlaue*(YValueMin.get(i)-tempMin)-length);
            }
        }
    }

    //画线
    public void DrawLine(Canvas canvas,List<Float> XValue,List<Float> mYAxis,List<Float> YValue,boolean top){
        for(int i=0;i<XValue.size();i++){
            if(top){
                LinePaint.setColor(MaxLineColor);
                //画具体温度数据
                canvas.drawText(YValue.get(i)+"",XValue.get(i),mYAxis.get(i)-mRadius,TextPaint);
            }else{
                LinePaint.setColor(MinLineColor);
                //画具体温度数据
                canvas.drawText(YValue.get(i)+"",XValue.get(i),mYAxis.get(i)+CircleTextSize+mRadius,TextPaint);
            }
            if(i!=XValue.size()-1){
                //画每两点之间的连线
                canvas.drawLine(XValue.get(i),mYAxis.get(i),XValue.get(i+1),mYAxis.get(i+1),LinePaint);
            }
            //画每一点的原点
            canvas.drawCircle(XValue.get(i),mYAxis.get(i),mRadius,CirclePaint);
        }
    }

}
