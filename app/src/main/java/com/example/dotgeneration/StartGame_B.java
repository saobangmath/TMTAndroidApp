package com.example.dotgeneration;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotgeneration.models.Coordinates;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartGame_B extends AppCompatActivity {
    TextView timerText;

    Timer timer;
    TimerTask timerTask;
    Double timeB = 0.0;

    boolean timerStarted = false;
    static String _endpoint = "https://murmuring-forest-94314.herokuapp.com/records/add/";
    String _nric;
    int timeA;
    double errorA;

    //============================== Added "Gameplay" Initialise - 14/3/21 =========================//
    private Paint mPaint = new Paint();
    DrawingView dv ;
    TextView xCoord, yCoord;
    private ConstraintLayout touchview;
    private int mTouchTolerance;

    private List<Point> mPoints = new ArrayList<Point>();
    int levelSize=25;

    //add every new dot created into this array
    Coordinates[] dotCoordinatesArray=null;

    //hashtable key -> Integer array of (x,y) coordinates, value -> dot index
    public Hashtable<Integer,Integer> dotHashTable = new Hashtable<Integer,Integer>();
    private int tolerance =30;

    //The imaginary boundary box,maximum and minimum x/y position of the dots
    private int boundaryBoxY1 = 400;
    private int boundaryBoxY2 = 1400;
    private int boundaryBoxX1 = 100;
    private int boundaryBoxX2 = 1000;
    private double errorB=0;

    TextView warningText;
    TextView errorCountText;

    LinearLayout overlay;

    //============================== End of "Gameplay" Initialise - 14/3/21 =========================//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game_b);

        _nric = getIntent().getStringExtra("nric");
        timeA = getIntent().getIntExtra("timeA", 0);
        errorA = getIntent().getDoubleExtra("errorA", 0);

        timerText = (TextView) findViewById(R.id.timerText);

        timer = new Timer();
        startTimer();

        //============================== Added "Gameplay" onCreate() - 14/3/21 =========================//
        overlay = findViewById(R.id.overlay);
        overlay.bringToFront();
        overlay.getParent().requestLayout();
        overlay.invalidate();
        warningText = findViewById(R.id.warningText);
        warningText.setVisibility(View.GONE);

        errorCountText = findViewById(R.id.errorCountText);

        int startGenerationX = boundaryBoxX1+40;
        int startGenerationY = boundaryBoxY1;
        Coordinates c = new Coordinates(startGenerationY,startGenerationX);
        dotCoordinatesArray = new Coordinates[levelSize];
        Integer[] xYValues = new Integer[2];
        String xYValuesString;
        Integer xYValueInt;
        for(int i=0;i<levelSize;i++){
            if(startGenerationX<boundaryBoxX2){

                c=new Coordinates(startGenerationY,startGenerationX);

                dotCoordinatesArray[i]= c;
                //2 loops to create the neighbouring coordinates for one point
                for(int j =0;j<=tolerance/2;j++) {
                    for(int k=0;k<tolerance/2;k++){
                        //combine the x and y value to be used as a key in the hashtable, eg. x=300,y=400, key=300400
                        xYValues[0] = c.getColumn()+j;
                        xYValuesString = xYValues[0].toString();
                        xYValues[1] = c.getRow()+k;
                        xYValuesString = xYValuesString+xYValues[1].toString();
                        xYValueInt = Integer.parseInt(xYValuesString);
                        dotHashTable.put(xYValueInt, i);
                        Log.d("hashtable",xYValueInt+"");
                    }
                }

                for(int j =1;j<=tolerance;j++) {
                    for(int k=0;k<tolerance/2;k++){
                        xYValues[0] = c.getColumn()-j;
                        xYValuesString = xYValues[0].toString();
                        xYValues[1] = c.getRow()-k;
                        xYValuesString = xYValuesString+xYValues[1].toString();
                        xYValueInt = Integer.parseInt(xYValuesString);
                        dotHashTable.put(xYValueInt, i);
                        Log.d("hashtable",xYValueInt+"");
                    }
                }
                startGenerationX=startGenerationX+200;
            }
            else{
                startGenerationY= startGenerationY+200;
                startGenerationX = boundaryBoxX1+200;
                i--;
            }
        }

        touchview = (ConstraintLayout) findViewById(R.id.constraintlayout);
        dv = new DrawingView(this);
        touchview.addView(dv);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(40);

        xCoord = (TextView) findViewById(R.id.xcordview);
        yCoord = (TextView) findViewById(R.id.ycordview);


        //============================== End of "Gameplay" onCreate() - 14/3/21 =========================//
    }

    public void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        timeB++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(timeB);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;

        return formatTime(seconds, minutes);
    }

    private String formatTime(int seconds, int minutes)
    {
        return String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

    //============================== Added "Gameplay" DrawingView() - 14/3/21 =========================//
    public class DrawingView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private List<Point> mPoints = new ArrayList<Point>();
        private static final int TOUCH_TOLERANCE_DP = 8;
        private int     mLastPointIndex = 0;
        private boolean isPathStarted = false;

        private Paint   textPaint;
        private List<Point> tPoints = new ArrayList<Point>();

        public DrawingView(StartGame_B c) {

            super(c, null);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

            textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(50);

            mTouchTolerance = dp2px(TOUCH_TOLERANCE_DP);

            generatePoints();


        }

        private void generatePoints()
        {
            Point p0 = new Point();
            for (int i = 0; i < dotCoordinatesArray.length; i++)
            {
                Log.d("array: ", dotCoordinatesArray[i].getColumn() + "");
                Log.d("array: ", dotCoordinatesArray[i].getRow() + "");
                mPoints.add(new Point(dotCoordinatesArray[i].getColumn(), dotCoordinatesArray[i].getRow()));

                Log.d("check",dotCoordinatesArray[i].getColumn()+ "c "+dotCoordinatesArray[i].getRow());
                tPoints.add(new Point(dotCoordinatesArray[i].getColumn()+15, dotCoordinatesArray[i].getRow()-15));
            }
        }


        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);


            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);

            // TODO remove if you dont want points to be drawn
            //            for (int i = 0; i < numCols; i++) {
            //                for (int j = 0; j < numRows; j++) {
            //                    canvas.drawPoint(pixels[i][j].coord.getColumn(), pixels[i][j].coord.getRow(), mPaint);
            //                }
            //            }
            // TODO remove if you dont want points to be drawn
            for (Point point : mPoints) {
                canvas.drawPoint(point.x, point.y, mPaint);
            }

            char ch = 'A';
            for (Point tPoint: tPoints) {
                canvas.drawText(String.valueOf(ch),tPoint.x, tPoint.y, textPaint);
                ch++;
            }


        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 8;

        private void touch_start(float x, float y) {
            mPath.reset();
            Point p = mPoints.get(mLastPointIndex);
            //            mPath.moveTo(p.x, p.y);
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            xCoord.setText(String.valueOf(x));
            yCoord.setText(String.valueOf(y));
            if(isPreviousDot(x,y)){
                warningText.setVisibility(GONE);
            }
            if (cPoint(x, y, mLastPointIndex)) {
                //                mPath.reset();
                // user starts from given point so path can beis started
                isPathStarted = true;
            } else {
                // user starts move from point which doen's belongs to mPinst list
                isPathStarted = false;
                // Just prompt error here, no count to error rate

            }

            if (mLastPointIndex == levelSize-1) {
                isPathStarted = false;
            }

        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            double errorRate;
            String currentCoordinatesString;
            Integer currentCoordinatesInt;

            if (isPathStarted) {
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                    xCoord.setText(String.valueOf(x));
                    yCoord.setText(String.valueOf(y));
                    Log.d("checking", "true");
                    if (cPoint(x, y, mLastPointIndex + 1)) {
                        mPath.lineTo(mX, mY);
                        mCanvas.drawPath(mPath,  mPaint);
                        ++mLastPointIndex;
                    }


                }
            }
            if(mLastPointIndex<levelSize-1){

                currentCoordinatesInt = (int)x;
                currentCoordinatesString = currentCoordinatesInt.toString();
                currentCoordinatesInt = (int)y;
                currentCoordinatesString=currentCoordinatesString+currentCoordinatesInt.toString();
                currentCoordinatesInt = Integer.parseInt(currentCoordinatesString);

                Log.d("index, x, y",dotHashTable.get(currentCoordinatesInt)+""+currentCoordinatesInt+" " +currentCoordinatesString);

                if(dotHashTable.containsKey(currentCoordinatesInt)) {
                    Log.d("key", "true");
                    if (dotHashTable.get(currentCoordinatesInt)!=mLastPointIndex
                            && dotHashTable.get(currentCoordinatesInt)!=mLastPointIndex+1) {
                        Log.d("tag", "wrong dot, start again");
                        if(warningText.getVisibility()==GONE) {
                            warningText.setVisibility(VISIBLE);
//                            warningText.bringToFront();
//                            warningText.getParent().requestLayout();
//                            warningText.invalidate();
                            errorB++;
                            errorRate = Math.round(errorB / 25 * 100);
                            errorCountText.setText(errorB + " - " + errorRate + "%");
                        }
                        mPath.reset();
                        isPathStarted=false;
                    }
                }
            }
            else
            {
                if (mLastPointIndex == levelSize-1) {
                    isPathStarted = false;
                    timer.cancel();
                    Log.d("touch_move", getTimerText());
                    Log.d("touch_move", Math.round(errorB / 25 * 100) + "%");
                    levelSize++;
                    JSONObject data = new JSONObject();
                    try {
                        data.put("nric", _nric);
                        data.put("errorA", errorA);
                        data.put("errorB", errorB);
                        data.put("timeA", timeA);
                        data.put("timeB", timeB);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new PostToUpdateRecordDatabase().execute(data);
                }
            }
        }

        private void touch_up(float x, float y) {
            mPath.reset();
            if (cPoint(x, y, mLastPointIndex + 1) && isPathStarted) {
                // move finished
                isPathStarted = false;
            }
            else
            {

            }

        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();

                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();

                    break;

                //when they release their finger, display notification and end test
                case MotionEvent.ACTION_UP:
                    touch_up(x, y);
                    invalidate();
                    break;
            }
            return true;
        }

        /**
         * Checks if user touch point with some tolerance
         */
        private boolean cPoint(float x, float y, int pointIndex) {
            if (pointIndex == mPoints.size()) {
                // out of bounds
                return false;
            }
            Point point = mPoints.get(pointIndex);
            if (x > (point.x - mTouchTolerance) && x < (point.x + mTouchTolerance)) {
                if (y > (point.y - mTouchTolerance) && y < (point.y + mTouchTolerance)) {
                    return true;
                }
            }
            return false;
        }

        private int dp2px(int dp) {
            Resources r = getContext().getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
            return (int) px;
        }

        //checks if currentLocation is the previous dot location
        private boolean isPreviousDot(float x, float y){
            int nextPointX = mPoints.get(mLastPointIndex).x;
            int nextPointY = mPoints.get(mLastPointIndex).y;

            if((nextPointX <= x+tolerance && nextPointX >= x-tolerance
                    && nextPointY <= y+tolerance && nextPointY >= y-tolerance)){
                return true;
            }
            return false;
        }



        public List<Point> getPoints() {
            return mPoints;
        }

        public void setPoints(List<Point> points) {
            this.mPoints = points;
        }
    }

    //============================== End of "Gameplay" DrawingView() - 14/3/21 =========================//

    public class PostToUpdateRecordDatabase extends AsyncTask<JSONObject, Void, Boolean>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(JSONObject... jsonObjects) {
            boolean success = true;
            for (JSONObject data : jsonObjects) {
                try {
                    String params =  "{\"nric\":" + "\"" + data.getString("nric") + "\"" +
                                     ",\"errorA\":" + (int)data.getDouble("errorA") +
                                     ",\"errorB\":" + (int)data.getDouble("errorB")  +
                                     ",\"scoreA\":" + data.getInt("timeA")  +
                                     ",\"scoreB\":" + data.getInt("timeB")  + "}";
                    HttpURLConnection conn = (HttpURLConnection) new URL(_endpoint).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    try(OutputStream os = conn.getOutputStream()){
                        byte[] input = params.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    catch (Exception e) {
                       success = false;
                    }
                    success = success&&(conn.getResponseCode()==200);
                } catch (Exception e) {
                    success = false;
                }
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Intent intent = new Intent(StartGame_B.this, ResultsPage_B.class);
                Bundle bundle = new Bundle();
                bundle.putString("nric", _nric);
                bundle.putInt("timeB", timeB.intValue());
                bundle.putDouble("errorB", errorB);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}