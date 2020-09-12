package com.wintermute.adventuresmaster.services.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFileWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Light;
import com.wintermute.adventuresmaster.services.light.ColorHelper;
import com.wintermute.adventuresmaster.services.network.RestGun;
import com.wintermute.adventuresmaster.services.receiver.SceneReceiver;

import java.util.ArrayList;

/**
 * Service responsible for starting the {@link GameAudioPlayer} and sending notification to user.
 *
 * @author wintermute
 */
public class SceneManager extends Service
{
    public static final int NOTIFICATION_CHANNEL_ID = 1;
    public static final int NOTIFICATION_REQUEST_ID = 2;
    private GameAudioPlayer player;
    private Visualizer audioOutput;

    @Override
    public void onCreate()
    {
        createNotificationChannel();
        Notification notification = createNotification();
        startForeground(NOTIFICATION_CHANNEL_ID, notification);
        player = GameAudioPlayer.getInstance();
        super.onCreate();
    }

    private void changeLightOnNoise(Light light)
    {
        int rate = Visualizer.getMaxCaptureRate();
        RestGun restGun = new RestGun(this);

        audioOutput = new Visualizer(0);
        audioOutput.setDataCaptureListener(new Visualizer.OnDataCaptureListener()
        {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate)
            {
                float intensity = ((float) waveform[0] + 128f) / 256;
                if (intensity != 0.0)
                {
                    restGun.adjustLightForScene(light);
                    audioOutput.release();
                }
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate)
            {

            }
        }, rate, true, false);
        audioOutput.setEnabled(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        player = GameAudioPlayer.getInstance();
        player.stopAll();
        ArrayList<AudioFileWithOpts> audioList = intent.getParcelableArrayListExtra("audioList");

        if (audioList != null) {
            player.playScene(this, audioList);
        }

        Light light = intent.getParcelableExtra("light");
        if (light != null)
        {
            changeLightOnNoise(light);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createNotification()
    {
        Intent broadcastIntent = new Intent(this, SceneReceiver.class);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, NOTIFICATION_REQUEST_ID, broadcastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "scene")
            .setContentTitle("playing scene")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(R.drawable.end, "stop scene", actionIntent);

        return notificationBuilder.build();
    }

    private void createNotificationChannel()
    {
        CharSequence name = "channelName";
        String description = "description";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel("scene", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy()
    {
        player.stopAll();
        if (audioOutput != null)
        {
            audioOutput.release();
        }
        stopSelf();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
