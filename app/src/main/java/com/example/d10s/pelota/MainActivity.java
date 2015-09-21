package com.example.d10s.pelota;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity implements SensorEventListener{

    public CustomDrawableView mCustomDrawableView = null;
    public ShapeDrawable mDrawable=new ShapeDrawable();

    private SensorManager sensorManager = null;

    public static int x;
    public static int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // the values you were calculating originally here were over 10000!
            x = ((int) event.values[0])*10; //Math.pow(event.values[1], 2);
            y = ((int) event.values[1])*10; //Math.pow(event.values[2], 2);

        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

        }
    }

    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CustomDrawableView extends View
    {
        final int width = this.getWidth();
        final int height = this.getHeight();

        public CustomDrawableView(Context context)
        {
            super(context);
            Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds(0, 0, disp.getWidth(), disp.getHeight());
        }

        protected void onDraw(Canvas canvas)
        {
            Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            //RectF oval = new RectF( width/2, height/2, (width/2)+50 , (height/2)+50); // set bounds of rectangle
            RectF oval = new RectF( (disp.getWidth()/2)+x, (disp.getHeight()/2)+y, (disp.getWidth()/2)+50+x , (disp.getHeight()/2)+50+y); // set bounds of rectangle
            Paint p = new Paint(); // set some paint options
            p.setColor(Color.BLUE);
            canvas.drawOval(oval, p);
            invalidate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
