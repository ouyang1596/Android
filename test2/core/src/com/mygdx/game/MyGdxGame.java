package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    Texture img2;
    private static final String TAG = MyGdxGame.class.getSimpleName();
    // 背景音乐
    private Music music;

    // 音效
    private Sound sound;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        img2 = new Texture("emoqq_s_44.png");
        // 加载背景音乐, 创建 Music 实例
        music = Gdx.audio.newMusic(Gdx.files.internal("ringback.wav"));

        // 背景音乐设置循环播放
        music.setLooping(true);

        // 设置音量, 值范围 0.0 ~ 1.0
        // music.setVolume(float volume);

        // 手动暂停播放, 暂停播放后调用 play() 将从暂停位置开始继续播放
        // music.pause();

        // 手动停止播放, 停止播放后调用 play() 将从头开始播放
        // music.stop();

        // 手动播放音乐, 这里游戏启动时开始播放背景音乐
        music.play();

        // 加载音效, 创建 Sound 实例
        sound = Gdx.audio.newSound(Gdx.files.internal("music.wav"));
    }

    @Override
    public void render() {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();

        // 判读当前是否有有触摸屏幕, 手指/鼠标按下后, 直到抬起前, Gdx.input.isTouched() 将始终返回 true
        if (Gdx.input.isTouched()) {
            // Gdx.input.getX(): 表示当前触屏按下时的幕 X轴 原始屏坐标, 相对于屏幕左下角
            // Gdx.input.getY(): 表示当前触屏按下时的幕 Y原 始屏轴坐标, 相对于屏幕左下角
            Gdx.app.log(TAG, "isTouched: x==" + Gdx.input.getX() + "==y==" + Gdx.input.getY());
            drawImage();
        } else {
            drawImage();
        }

        // 判读当前指定的按键是否被按下, 指定按键被按下后, 直到弹起前, Gdx.input.isKeyPressed(KeyCode) 将始终返回 true
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            Gdx.app.log(TAG, "isKeyPressed: B键被按下");
        }


        /*
         * 通过上面的方法判读输入事件, 通常会导致监听后需要执行的代码在连续多帧中被持续执行,
         * 更多时候当有按键按下一次或触摸一次屏幕只需要执行一次, 使用 Gdx.input 中的带有 just 的方法可以满足这个需求,
         * 如下所示:
         */

        /*
         * 判断刚刚是否有一个新的触屏按下事件,
         * 如果有, Gdx.input.justTouched() 则会在当前渲染帧范围内返回  true。
         * 如果手指/鼠标一直按在屏幕上没有抬起过, 下一帧开始即使手指/鼠标依然是按着屏幕的,
         * 但由于本次触摸事件已在上一帧中判断过, 已不能算是新事件, Gdx.input.justTouched() 将返回 false。
         */
        if (Gdx.input.justTouched()) {
            Gdx.app.log(TAG, "justTouched: " + Gdx.input.getX() + Gdx.input.getY());

            if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2) {
                Gdx.app.log(TAG, "左半屏被按下, 主角跳跃一次~~");
            } else {
                Gdx.app.log(TAG, "右半屏被按下, 发射一颗子弹**");
            }
            sound.play();
        }

        /*
         * 判断刚刚是否有指定的键盘按键按下, isKeyJustPressed() 方法的返回值规则和 justTouched() 方法一样,
         */
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            Gdx.app.log(TAG, "isKeyJustPressed: B键被按下");
        }
    }

    private void drawImage() {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        float x = Gdx.input.getX() - img.getWidth() / 2;
        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - img.getHeight() / 2;
        batch.draw(img, x, y);
        if (x <= Gdx.graphics.getWidth() / 2 + img.getWidth() / 2 && x >= Gdx.graphics.getWidth() / 2 - img.getWidth() / 2 && y <= Gdx.graphics.getHeight() / 2 + img.getHeight() / 2 && y >= Gdx.graphics.getHeight() / 2 - img.getHeight() / 2) {
            batch.draw(img2, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        img2.dispose();
        // 当应用退出时释放资源
        if (music != null) {
            music.dispose();
        }
        if (sound != null) {
            sound.dispose();
        }
    }
}
