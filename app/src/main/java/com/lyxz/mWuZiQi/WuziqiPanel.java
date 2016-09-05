package com.lyxz.mWuZiQi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitong on 2016/06/28.
 */
public class WuziqiPanel extends View {

    private int MAX_COUNT_IN_LINE = 5;
    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;
    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float ratioPiece = 3 * 1.0f / 4;

    private boolean mIsWhite = true;
    private static ArrayList<Point> mWhiteArray = new ArrayList<>();
    private static ArrayList<Point> mBlackArray = new ArrayList<>();

    private static boolean mGameOver;
    private static boolean mIsWhiteWinner;

    public WuziqiPanel(Context context) {
        super(context);
    }

    public WuziqiPanel(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();

    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        /**
         * 如果一个match子容器,放入一个wrap的父容器,会导致宽或高不可预知;
         * 所以添加if判断语句
         */
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int widthPiece = (int) (mLineHeight * ratioPiece);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, widthPiece, widthPiece, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, widthPiece, widthPiece, false);

    }

    /**
     * 绘制操作
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        drawPiece(canvas);

        checkGameOver();

    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);

        if (whiteWin || blackWin) {

            mGameOver = true;
            mIsWhiteWinner = whiteWin;

            String text = mIsWhiteWinner ? "白棋获胜" : "黑棋获胜";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

            showDialog(text);

        }
    }

    public void showDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(text);
        builder.setMessage("是否重新开始?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mWhiteArray.clear();
                mBlackArray.clear();
                invalidate();
                mGameOver = false;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkFiveInLine(List<Point> points) {

        for (Point p : points) {
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x, y, points);
            if (win) return true;

            win = checkVertical(x, y, points);
            if (win) return true;

            win = checkLeftDiagonal(x, y, points);
            if (win) return true;

            win = checkRightDiagonal(x, y, points);
            if (win) return true;
        }
        return false;
    }

    /**
     * 判断横向五子连珠的情况
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {

        int count = 1;
        //向左数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        //向右数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {

        int count = 1;
        //向上数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        //向右数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {

        int count = 1;
        //向上数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        //向右数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points) {

        int count = 1;
        //向上数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        //向右数
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    /**
     * 画棋子
     */
    private void drawPiece(Canvas canvas) {
        for (int i = 0; i < mWhiteArray.size(); i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 - ratioPiece) / 2) * mLineHeight,
                    (whitePoint.y + (1 - ratioPiece) / 2) * mLineHeight, null);
        }
        for (int i = 0; i < mBlackArray.size(); i++) {
            Point BlackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (BlackPoint.x + (1 - ratioPiece) / 2) * mLineHeight,
                    (BlackPoint.y + (1 - ratioPiece) / 2) * mLineHeight, null);
        }
    }


    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            /**
             * 画棋盘的横线
             * */
            //横坐标的起点和中点坐标
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            //y控制每条横线的坐标位置
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);

            /**
             * 画棋盘的竖线
             * */
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mGameOver) return false;

        if (event.getAction() == MotionEvent.ACTION_UP) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x, y);

            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {

                return false;
            }


            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;

        }
        return true;
    }

    private Point getValidPoint(int x, int y) {

        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {

            Bundle bundle = (Bundle) state;
            mGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));

            return;
        }
        super.onRestoreInstanceState(state);
    }
}