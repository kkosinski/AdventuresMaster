package com.wintermute.adventuresmaster.services.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFileWithOpts;
import com.wintermute.adventuresmaster.services.receiver.SceneReceiver;

import java.util.ArrayList;

public class SceneManager extends Service
{
    GameAudioPlayer player;

    @Override
    public void onCreate()
    {
        Notification notification = createNotification();
        startForeground(1, notification);
        player = GameAudioPlayer.getInstance();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        player = GameAudioPlayer.getInstance();
        player.stopAll();
        ArrayList<AudioFileWithOpts> audioList = intent.getParcelableArrayListExtra("audioList");
        player.playScene(this, audioList);

        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createNotification()
    {
        Intent broadcastIntent = new Intent(this, SceneReceiver.class);
        PendingIntent actionIntent =
            PendingIntent.getBroadcast(this, 1, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "scene")
            .setContentTitle("playing scene")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(R.drawable.end, "stop scene", actionIntent);

        return notificationBuilder.build();
    }

    @Override
    public void onDestroy()
    {
        player.stopAll();
        stopSelf();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
