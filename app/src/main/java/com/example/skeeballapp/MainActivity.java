package com.example.skeeballapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.*;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout backgroundLayout;
    private LinearLayout ballSpaceLayout;

    private SurfaceView surfaceView;

    private Canvas canvas;

    private TextView colorTextView;

    private int score = 0;
    private int rightBallColor = 0;

    private ImageView redIVSelect, blueIVSelect, greenIVSelect, yellowIVSelect, ballIV;
    private CardView redIVBorder, blueIVBorder, greenIVBorder, yellowIVBorder;
    private CardView[] ballBorders;
    private int ballColor = 1;

    private FlingAnimation flingX;
    private FlingAnimation flingY;
    private final float[] flingXMin = {0};
    private final float[] flingXMax = {1085};
    private final float[] flingYMin = {925};
    private final float[] veloX = {0};
    private final float[] veloY = {0};

    boolean turnDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundLayout = findViewById(R.id.relativeLayout);
        ballSpaceLayout = findViewById(R.id.layout2);
        surfaceView = findViewById(R.id.surfaceView);
        colorTextView = findViewById(R.id.colorTextView);
        // https://stackoverflow.com/questions/5981098/set-the-background-image-of-a-surfaceview
        // https://stackoverflow.com/questions/11833460/android-surfaceview-cannot-build-when-add-callback
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surfaceView.setZOrderOnTop(true);
                surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
                canvas = surfaceView.getHolder().lockCanvas();
                surfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        // make colorTextView have a retro font with yellow color
        colorTextView.setTextColor(getResources().getColor(R.color.white));
        int rand = (int) (Math.random() * 4) + 1;
        rightBallColor = rand;
        switch (rightBallColor){
            case 1:
                colorTextView.setText("Red");
                break;
            case 2:
                colorTextView.setText("Blue");
                break;
            case 3:
                colorTextView.setText("Green");
                break;
            case 4:
                colorTextView.setText("Yellow");
                break;
        }
        colorTextView.setY(colorTextView.getY() - 55);
        colorTextView.setX(backgroundLayout.getWidth()/2 - colorTextView.getWidth()/2 - 45);
        redIVSelect = findViewById(R.id.redIVSelect); blueIVSelect = findViewById(R.id.blueIVSelect); greenIVSelect = findViewById(R.id.greenIVSelect); yellowIVSelect = findViewById(R.id.yellowIVSelect);
        redIVBorder = findViewById(R.id.redIVBorder); blueIVBorder = findViewById(R.id.blueIVBorder); greenIVBorder = findViewById(R.id.greenIVBorder); yellowIVBorder = findViewById(R.id.yellowIVBorder);
        redIVBorder.setCardBackgroundColor(getResources().getColor(R.color.black));
        blueIVBorder.setCardBackgroundColor(getResources().getColor(R.color.black));
        greenIVBorder.setCardBackgroundColor(getResources().getColor(R.color.black));
        yellowIVBorder.setCardBackgroundColor(getResources().getColor(R.color.black));
        ballBorders = new CardView[]{redIVBorder, blueIVBorder, greenIVBorder, yellowIVBorder};
        ballIV = findViewById(R.id.ballIV);
        ballIV.setVisibility(View.INVISIBLE);
        redIVSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBallSelectColor(1);
            }
        });
        blueIVSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBallSelectColor(2);
            }
        });
        greenIVSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBallSelectColor(3);
            }
        });
        yellowIVSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeBallSelectColor(4);
            }
        });

        flingX = new FlingAnimation(ballIV, DynamicAnimation.X);
        flingY = new FlingAnimation(ballIV, DynamicAnimation.Y);
        flingX.setFriction(.2f);
        flingY.setFriction(.2f);
        flingY.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                // TODO: Score calc
                int x = (int) ballIV.getX() + 82;
                int y = (int) ballIV.getY() + 82;
                double r = 50;
                int s = 0;
                if(y < flingYMin[0]){
                    // red area
                    if(isTouching(x,y,r,543,670,260)){
                        // 100 points
                        if(isTouching(x,y,r,370,470,50) || isTouching(x,y,r,716,470,50)){
                            // 100 points
                            System.out.println("100 points");
                            score += 100;
                            s = 100;
                        } else if(isTouching(x,y,r,543,470,40)){
                            // 50 points
                            System.out.println("50 points");
                            score += 50;
                            s = 50;
                        } else if(isTouching(x,y,r,543,665,145)){
                            // green area
                            if(isTouching(x,y,r,543,555,40)){
                                // 40 points
                                System.out.println("40 points");
                                score += 40;
                                s = 40;
                            } else if(isTouching(x,y,r,543,665,40)){
                                // 30 points
                                System.out.println("30 points");
                                score += 30;
                                s = 30;
                            } else {
                                // 20 points
                                System.out.println("20 points");
                                score += 20;
                                s = 20;
                            }
                        } else {
                            // 10 points
                            System.out.println("10 points");
                            score += 10;
                            s = 10;
                        }
                    } else {
                        System.out.println("0 points");
                    }

                } else {
                    System.out.println("Out of bounds");
                }
                scoreAlert(s);
            }
        });
        flingY.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                int x = (int) ballIV.getX();
                int y = (int) ballIV.getY();
                if(y >= flingYMin[0]) {
                    int minX = (int) ((-1*y + 2000)/3.252173913);
                    int maxX = (int) ((y +1400)/3.2521739138);
                    flingX.setMinValue(minX); flingXMin[0] = minX;
                    flingX.setMaxValue(maxX); flingXMax[0] = maxX;
                    if (x <= minX + 5) {
                        float veloX2 = (float) ((veloX[0]) * -(.75));
                        float veloY2 = (float) ((veloY[0]) * (.75));
                        ballIV.setX(minX + 10);
                        flingX.setStartVelocity(veloX2);
                        flingY.setStartVelocity(veloY2);
                        System.out.println("Starting bounce from left");
                        flingX.start();
                        flingY.start();
                        ballIV.animate().rotationBy(360).setDuration(1000);
                    } else if (x >= maxX - 5) {
                        float veloX2 = (float) ((veloX[0]) * -(.75));
                        float veloY2 = (float) ((veloY[0]) * (.75));
                        ballIV.setX(maxX - 10);
                        flingX.setStartVelocity(veloX2);
                        flingY.setStartVelocity(veloY2);
                        System.out.println("Starting bounce from right");
                        flingX.start();
                        flingY.start();
                        ballIV.animate().rotationBy(-360).setDuration(1000);
                    }
                } else if(y <= 410){
                    flingXMin[0] = 0;
                    flingXMax[0] = 1085;
                    float veloY2 = (float) ((veloY[0]) * -(.15));
                    ballIV.setY(415);
                    flingY.setStartVelocity(veloY2);
                    System.out.println("Starting bounce from top");
                    flingY.start();
                    ballIV.animate().rotationBy(360).setDuration(1000);
                }
            }
        });
    }

    private void changeBallSelectColor(int color) {
        if(ballColor == color) {
            ballColor = 0;
            for (int i = 0; i < ballBorders.length; i++) {
                ballBorders[i].setCardBackgroundColor(getResources().getColor(R.color.black));
            }
            ballIV.setVisibility(View.INVISIBLE);
            return;
        }
        ballColor = color;
        drawLines();
        for (int i = 0; i < ballBorders.length; i++) {
            if (i == (color-1)) {
                ballBorders[i].setCardBackgroundColor(getResources().getColor(R.color.white));
            } else {
                ballBorders[i].setCardBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if(color == 1){
            ballIV.setImageResource(R.mipmap.red_ball_foreground);
        } else if(color == 2){
            ballIV.setImageResource(R.mipmap.blue_ball_foreground);
        } else if(color == 3){
            ballIV.setImageResource(R.mipmap.green_ball_foreground);
        } else if(color == 4){
            ballIV.setImageResource(R.mipmap.yellow_ball_);
        }
        ballIV.setVisibility(View.VISIBLE);

        if(rightBallColor == color){
            redIVSelect.setEnabled(false);
            blueIVSelect.setEnabled(false);
            greenIVSelect.setEnabled(false);
            yellowIVSelect.setEnabled(false);
            canMoveBall(true);
        } else {
            redIVSelect.setEnabled(true);
            blueIVSelect.setEnabled(true);
            greenIVSelect.setEnabled(true);
            yellowIVSelect.setEnabled(true);
            canMoveBall(false);
        }
    }

    private void canMoveBall(boolean canMove){
        if(canMove) {
            ballIV.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            System.out.println("Pressed");
                            return true;
                        case MotionEvent.ACTION_UP:
                            if (ballIV.getX() > flingXMin[0] && ballIV.getX() < flingXMax[0] && ballIV.getY() > flingYMin[0]) {
                                System.out.println("Released");
                                flingX.setStartVelocity(motionEvent.getX());
                                flingY.setStartVelocity(motionEvent.getY());
                                veloX[0] = motionEvent.getX();
                                veloY[0] = motionEvent.getY();
                                flingX.start();
                                flingY.start();
                                ballIV.animate().rotationBy(360).setDuration(1000);
                                canMoveBall(false);
                            }
                            return true;
                    }
                    return false;
                }
            });
        } else {
            ballIV.setOnTouchListener(null);
        }
    }

    private void drawLines(){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        Paint paint1 = new Paint();
        paint1.setColor(Color.RED);
        paint1.setStrokeWidth(10);
        canvas = surfaceView.getHolder().lockCanvas();
//
//        canvas.drawCircle(543,670,260,paint1); // red area
//        canvas.drawCircle(543,905,40,paint); // 10 points
//
//
//        canvas.drawCircle(543,470,40,paint); // 50 points
//
//        canvas.drawCircle(370,470,50,paint); // 100 points
//
//        canvas.drawCircle(716,470,50,paint); // 100 points
//        canvas.drawCircle(543,665,145,paint); // green area
//        canvas.drawCircle(543,665,50,paint1); // 30 points
//
//        canvas.drawCircle(543,555,40,paint1); // 40 points
//        canvas.drawCircle(543,780,40,paint1); // 20 points
//
//        canvas.drawLine(0, backgroundLayout.getHeight(), 345,925 , paint);
//        // (0,2045) (345,925)
//        canvas.drawLine(backgroundLayout.getWidth(),backgroundLayout.getHeight(),(backgroundLayout.getWidth()-345),925,paint);
//        // (1080,2045) (735,925)
//        canvas.drawLine(0,925,backgroundLayout.getWidth(),925,paint);
//
//        canvas.drawCircle(ballIV.getX() + 82, ballIV.getY() + 82, 3, paint1);
//        canvas.drawLine(ballIV.getX()+82-50,ballIV.getY()+82,ballIV.getX()+182-50,ballIV.getY()+82,paint);
//        System.out.println("Start: " + (ballIV.getX()+82-50) + " " + (ballIV.getY()+82) + " End: " + (ballIV.getX()+182-50) + " " + (ballIV.getY()+82));

//        canvas.drawLine(0,410,1080,410,paint);
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
    }

    private boolean isTouching(int ballX, int ballY, double ballRadius, int circleX, int circleY, int circleRadius){
        int x = ballX - circleX;
        int y = ballY - circleY;
        int distance = (int) Math.sqrt(x*x + y*y);
        if(distance <= ballRadius + circleRadius){
            return true;
        }
        return false;
    }

    private void scoreAlert(int s){
        // alert score through popup
        Snackbar snackbar = Snackbar.make(backgroundLayout, "You scored " + s + " your total is " + score + " :)", Snackbar.LENGTH_LONG);
        backgroundLayout.addView(snackbar.getView());
        snackbar.show();
    }


}