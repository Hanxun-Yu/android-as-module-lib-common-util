package com.example.utils.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Crashxun dbstar-mac on 2017/2/6.
 */

public class SoundPoolUtil {
    Context context;
    SoundPool soundPool;
    int soundID;
    final String TAG = this.getClass().getSimpleName() + "_xunxun";

    public SoundPoolUtil(Context con) {
        this.context = con;
    }


    public void play(int resSoundID) {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(context, resSoundID, 1);
        soundPool.play(soundID, 1, 1, 0, 0, 1);
    }

    private MediaPlayer shootSound;

    public void shootSound() {
        AudioManager meng = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume(AudioManager.STREAM_MUSIC);
//        Log.d(TAG, "STREAM_RING:" + volume);
//        volume = meng.getStreamVolume(AudioManager.STREAM_MUSIC);
//        Log.d(TAG, "STREAM_MUSIC:" + volume);
//        volume = meng.getStreamVolume(AudioManager.STREAM_SYSTEM);
//        Log.d(TAG, "STREAM_SYSTEM:" + volume);
//        volume = meng.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
//        Log.d(TAG, "STREAM_NOTIFICATION:" + volume);

        if (volume != 0) {
            if (shootSound == null) {
                shootSound = MediaPlayer.create(context, Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    AudioAttributes.Builder attrBuilder =  new AudioAttributes.Builder();
//                    //设置音频流的合适属性
//                    attrBuilder.setLegacyStreamType(AudioManager.STREAM_RING);
//                    shootSound.setAudioAttributes(attrBuilder.build());
//                } else {
//                shootSound.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
//                }
            }
            if (shootSound != null)
                shootSound.start();
        }
    }

    private MediaPlayer videoRecordStartSound;

    public void videoRecordStartSound() {
        AudioManager meng = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (volume != 0) {
            if (videoRecordStartSound == null)
                videoRecordStartSound = MediaPlayer.create(context, Uri.parse("file:///system/media/audio/ui/VideoRecord.ogg"));

            if (videoRecordStartSound != null)
                videoRecordStartSound.start();
        }
    }

    private MediaPlayer videoRecordStopSound;

    public void videoRecordStopSound() {
        AudioManager meng = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (volume != 0) {
            if (videoRecordStopSound == null)
                videoRecordStopSound = MediaPlayer.create(context, Uri.parse("file:///system/media/audio/ui/VideoStop.ogg"));

            if (videoRecordStopSound != null)
                videoRecordStopSound.start();
        }
    }
}
