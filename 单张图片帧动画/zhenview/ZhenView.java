package com.mosheng.common.view.zhenview;
/**
 * Created by Ryan on 2016/12/23.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mosheng.control.tools.AppLogs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 单张图片帧动画视图工具
 */
public class ZhenView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "ZanView";
    private SurfaceHolder surfaceHolder;
    private Paint mPaint;
    private int index = 0;
    private int rate = 60;
    //    private int columns = 7;
//    private int raws = 7;
    private SparseArray<WeakReference<Bitmap>> weakBitmaps;
    private BitmapFactory.Options options;
    private int mFrameCount = 0;
    private boolean isRepeat;//动画是否重复

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    /**
     * 负责绘制的工作线程
     */
    private DrawThread drawThread;

    public ZhenView(Context context) {
        this(context, null);
    }

    public ZhenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZhenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setZOrderOnTop(true);
        /**设置画布  背景透明*/
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        weakBitmaps = new SparseArray<WeakReference<Bitmap>>();
        options = new BitmapFactory.Options();
        options.inSampleSize = 1;
    }

    private BitmapRegionDecoder bitmapRegionDecoder;
    private List<Frame> mFrames = new ArrayList<>();

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        if (drawThread == null) {
//            drawThread = new DrawThread();
//            drawThread.start();
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (surfaceHolder) {
            if (drawThread != null) {
                drawThread.isRun = false;
                drawThread = null;
            }
        }
    }

    class DrawThread extends Thread {
        boolean isRun = true;

        @Override
        public void run() {
            super.run();
            long time = 0;
            /**绘制的线程 死循环 不断的跑动*/
            while (isRun) {
                time = System.currentTimeMillis();
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();
                        /**清除画面*/
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                        canvas.drawColor(Color.parseColor("#ffff00"));
                        if (index > mFrames.size() - 1) {
                            isRun = false;
                            continue;
                        }
                        Frame frame = mFrames.get(index);
                        if (frame.getW() > 0) {
                            Rect rect = new Rect(frame.getX(),
                                    frame.getY(), frame.getX() + frame.getW(),
                                    frame.getY() + frame.getH());
                            WeakReference<Bitmap> weakBitmap = weakBitmaps
                                    .get(index);
                            Bitmap bitmap = null;
                            if (weakBitmap == null) {
                                bitmap = bitmapRegionDecoder.decodeRegion(rect,
                                        options);
                                weakBitmaps.put(index,
                                        new WeakReference<Bitmap>(bitmap));
                            } else {
                                bitmap = weakBitmap.get();
                                if (bitmap == null) {
                                    bitmap = bitmapRegionDecoder.decodeRegion(
                                            rect, options);
                                    weakBitmaps.put(index,
                                            new WeakReference<Bitmap>(bitmap));
                                }
                            }
                            if (bitmap == null) {
                                surfaceHolder.unlockCanvasAndPost(canvas);
                                continue;
                            }

                            int dwidth = frame.getW();
                            int dheight = frame.getH();
                            int vwidth = getWidth();
                            int vheight = getHeight();

                            float scale;
                            int dx;
                            int dy;
                            scale = Math.min((float) vwidth / (float) dwidth,
                                    (float) vheight / (float) dheight);
//                            AppLogs.PrintLog("Ryan", "vwidth--" + vwidth + "--vheight---" + vheight);
//                            if (scale < 1) {
                            Float swidth = dwidth * scale;
                            Float sheight = dheight * scale;

//                            float bitmapScale = mScalePer * (index + 1);
//                            if (bitmapScale <= 1.0f) {
//                                swidth = bitmapScale * swidth;
//                                sheight = bitmapScale * sheight;
//                            }

                            dx = (int) ((vwidth - swidth) * 0.5f/* + 0.5f*/);
                            dy = (int) ((vheight - sheight) * 0.5f/* + 0.5f*/);
                            dwidth = swidth.intValue();
                            dheight = sheight.intValue();
                            bitmap = Bitmap.createScaledBitmap(bitmap, dwidth, dheight, false);
//                            } else {
//                                dx = (int) ((vwidth - dwidth) * 0.5f);
//                                dy = (int) ((vheight - dheight) * 0.5f);
//                            }

//                            mPaint.setXfermode(new PorterDuffXfermode(
//                                    PorterDuff.Mode.CLEAR));
//                            canvas.drawPaint(mPaint);
//                            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//                            canvas.drawBitmap(map,
//                                    (int) (WIDTH - (map.getWidth() * 1) - 150),
//                                    (int) (HEIGHT - (map.getHeight() / 2)),
//                                    mPaint);
//                            canvas.drawBitmap(map, (int) (WIDTH + 150),
//                                    (int) (HEIGHT - (map.getHeight() / 2)),
//                                    mPaint);
                            canvas.drawBitmap(bitmap, dx, dy, mPaint);
//                            drawPlateNumber(canvas, scale, dx, dy);

                            if (bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                            if (index >= mFrames.size() - 1) {
                                if (isRepeat) {
                                    index = 0;
                                } else {
                                    ZhenView.this.stop();
                                }
                            } else {
                                index++;
                            }
                        }
                    }
                } catch (Exception e) {
                    isRun = false;
                    AppLogs.PrintLog("Ryan", "e--" + e.getLocalizedMessage());
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    /**用于控制绘制帧率*/
                    Thread.sleep(Math.max(0, rate - (System.currentTimeMillis() - time)));
                } catch (InterruptedException e) {

                }
            }
            index = 0;
            mFrames.clear();
            for (int i = 0; i < weakBitmaps.size(); i++) {
                WeakReference<Bitmap> bitmapWeakReference = weakBitmaps.get(i);
                if (bitmapWeakReference != null) {
                    Bitmap bitmap = bitmapWeakReference.get();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }
            weakBitmaps.clear();
        }
    }

    /**
     * 画车牌号码
     */
    private void drawPlateNumber(Canvas canvas, float scale, int dx, int dy) {
        mPaint.setColor(Color.parseColor("#ff00ff"));
        int x = (int) (41 * scale);
        int y = (int) (231 * scale);
        int w = (int) (33 * scale);
        int h = (int) (13 * scale);
        canvas.drawRect(new Rect(x + dx, y + dy, x + w + dx, y + h + dy), mPaint);
        String testString = "8899";
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setTextSize(16);
        mPaint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        mPaint.getTextBounds(testString, 0, testString.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        int HorizonCenter = w / 2 - bounds.width() / 2;
        canvas.drawText(testString, x + dx + HorizonCenter, y + dy + baseline, mPaint);
    }

    private void initImg(int columns, int raws, int frameCount) {
        if (mFrames != null) {
            mFrames.clear();
        }
        mFrameCount = frameCount;
        int count = 0;
        int width = bitmapRegionDecoder.getWidth() / columns;
        int height = bitmapRegionDecoder.getHeight() / raws;
        for (int i = 0; i < raws; i++) {
            if (count >= mFrameCount) {
                break;
            }
            for (int j = 0; j < columns; j++) {
                Frame frame = new Frame();
                frame.setX(j * width);
                frame.setY(i * height);
                frame.setW(width);
                frame.setH(height);
                mFrames.add(frame);
                count++;
                if (count >= mFrameCount) {
                    break;
                }
            }
        }
    }

    /**
     * @param inputStream 图片文件流
     * @param columns     帧动画列数
     * @param raws        帧动画行数
     */
    public void setImage(InputStream inputStream, int columns, int raws, int frameCount) {
        try {
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
                    inputStream,
                    false);
            initImg(columns, raws, frameCount);
        } catch (IOException e) {
        }
    }

    /**
     * @param file    图片文件
     * @param columns 帧动画列数
     * @param raws    帧动画行数
     */
    public void setImage(File file, int columns, int raws, int frameCount) {
        try {
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
                    file.getAbsolutePath(),
                    false);
            initImg(columns, raws, frameCount);
        } catch (IOException e) {

        }
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void stop() {
        if (drawThread != null) {
            drawThread.isRun = false;
            drawThread = null;
        }
    }

    public void start() {
        if (drawThread == null) {
            drawThread = new DrawThread();
            drawThread.start();
        }
    }

    public boolean isRunning() {
        if (drawThread != null) {
            return drawThread.isRun;
        }
        return false;
    }

}
