package com.mosheng.live.utils;

import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 单张图片帧动画工具类
 */
public class AnimationBitmapUtils {
    public static class MyFrame {
        int duration;
        int x;
        int y;
        int w;
        int h;
        Drawable drawable;
        boolean isReady = false;
    }

    /**
     * @param stream      图片流
     * @param columns     列数
     * @param raws        行数
     * @param frameCount  帧图片数量
     * @param duration    每帧动画时间（暂时不支持单个帧设置）
     * @param repeatCount 重复次数（0 只执行一次，以此类推）
     * @param imageView   控件
     * @param onStart     动画开始回调
     * @param onComplete  动画结束回调
     */
    public static void animateFromStream(InputStream stream, int columns, int raws, int frameCount, int duration, final int repeatCount,
                                         final ImageView imageView, final Runnable onStart,
                                         final Runnable onComplete) {
        loadStream(stream, columns, raws, frameCount, duration, imageView,
                new OnDrawableLoadedListener() {
                    @Override
                    public void onDrawableLoaded(List<MyFrame> myFrames, BitmapRegionDecoder bitmapRegionDecoder) {
                        if (onStart != null) {
                            onStart.run();
                        }
                        animateStreamManually(myFrames, bitmapRegionDecoder, repeatCount, imageView, onComplete);
                    }
                });
    }

    private static void loadStream(final InputStream stream, int columns, int raws, int frameCount, int duration, final ImageView imageView,
                                   final OnDrawableLoadedListener onDrawableLoadedListener) {
        loadFromStream(stream, columns, raws, frameCount, duration, imageView, onDrawableLoadedListener);
    }


    private static void loadFromStream(final InputStream resourceId, final int columns, final int raws, final int frameCount, final int duration,
                                       final ImageView imageView,
                                       final OnDrawableLoadedListener onDrawableLoadedListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(resourceId, false);
                    int count = 0;
                    int width = bitmapRegionDecoder.getWidth() / columns;
                    int height = bitmapRegionDecoder.getHeight() / raws;
                    final ArrayList<MyFrame> frames = new ArrayList<>();
                    for (int i = 0; i < raws; i++) {
                        if (count >= frameCount) {
                            break;
                        }
                        for (int j = 0; j < columns; j++) {
                            MyFrame frame = new MyFrame();
                            frame.x = j * width;
                            frame.y = i * height;
                            frame.w = width;
                            frame.h = height;
                            frame.duration = duration;
                            frames.add(frame);
                            count++;
                            if (count >= frameCount) {
                                break;
                            }
                        }
                    }
//                    final ArrayList<MyFrame> myFrames = new ArrayList<MyFrame>();
//                    for (int i = 0; i < frames.size(); i++) {
//                        Frame frame = frames.get(i);
//                        if (frame.getW() > 0) {
//                            Rect rect = new Rect(frame.getX(),
//                                    frame.getY(), frame.getX() + frame.getW(),
//                                    frame.getY() + frame.getH());
//                            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect,
//                                    options);
//                            MyFrame myFrame = new MyFrame();
//                            myFrame.bitmap = bitmap;
//                            myFrame.duration = duration;
//                            myFrames.add(myFrame);
//                        }
//                    }

                    // Run on UI Thread
                    new Handler(imageView.getContext().getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (onDrawableLoadedListener != null) {
                                onDrawableLoadedListener.onDrawableLoaded(frames, bitmapRegionDecoder);
                            }
                        }
                    });
                } catch (IOException e) {

                }
            }
        }).run();
    }

    private static void animateStreamManually(List<MyFrame> myFrames, BitmapRegionDecoder bitmapRegionDecoder, int repeatCount,
                                              ImageView imageView, Runnable onComplete) {
        animateStreamManually(myFrames, bitmapRegionDecoder, repeatCount, imageView, onComplete, 0);
    }

    private static void animateStreamManually(final List<MyFrame> myFrames, final BitmapRegionDecoder bitmapRegionDecoder, final int repeatCount,
                                              final ImageView imageView, final Runnable onComplete,
                                              final int frameNumber) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        final MyFrame thisFrame = myFrames.get(frameNumber);
        if (frameNumber == 0) {
            Rect rect = new Rect(thisFrame.x,
                    thisFrame.y, thisFrame.x + thisFrame.w,
                    thisFrame.y + thisFrame.h);
            thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                    .getResources(), bitmapRegionDecoder.decodeRegion(rect, options));
        } else {
            //将上一个bitmap回收
            MyFrame previousFrame = myFrames.get(frameNumber - 1);
            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
            previousFrame.drawable = null;
            previousFrame.isReady = false;
        }

        imageView.setImageDrawable(thisFrame.drawable);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Make sure ImageView hasn't been changed to a different Image
                // in this time
                if (imageView.getDrawable() == thisFrame.drawable) {
                    if (frameNumber + 1 < myFrames.size()) {
                        MyFrame nextFrame = myFrames.get(frameNumber + 1);

                        // Animate next frame
                        if (nextFrame.isReady)
                            animateStreamManually(myFrames, bitmapRegionDecoder, repeatCount, imageView, onComplete,
                                    frameNumber + 1);
                        else {
                            nextFrame.isReady = true;
                        }
                    } else {
                        if (repeatCount > 0) {
                            animateStreamManually(myFrames, bitmapRegionDecoder, repeatCount - 1, imageView, onComplete);
                        } else {
                            for (int i = 0; i < myFrames.size(); i++) {
                                MyFrame myFrame = myFrames.get(i);
                                if (myFrame.drawable != null) {
                                    ((BitmapDrawable) myFrame.drawable).getBitmap().recycle();
                                    myFrame.drawable = null;
                                }
                            }
                            imageView.setImageDrawable(null);
                        }
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    }
                }
            }
        }, thisFrame.duration);

        // Load next frame
        if (frameNumber + 1 < myFrames.size()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyFrame nextFrame = myFrames.get(frameNumber + 1);
                    Rect rect = new Rect(nextFrame.x,
                            nextFrame.y, nextFrame.x + nextFrame.w,
                            nextFrame.y + nextFrame.h);

                    nextFrame.drawable = new BitmapDrawable(imageView
                            .getContext().getResources(),
                            bitmapRegionDecoder.decodeRegion(rect, options));
                    if (nextFrame.isReady) {
                        // Animate next frame
                        animateStreamManually(myFrames, bitmapRegionDecoder, repeatCount, imageView, onComplete,
                                frameNumber + 1);
                    } else {
                        nextFrame.isReady = true;
                    }

                }
            }).run();
        }
    }


    public interface OnDrawableLoadedListener {
        public void onDrawableLoaded(List<MyFrame> frames, BitmapRegionDecoder bitmapRegionDecoder);
    }
}
