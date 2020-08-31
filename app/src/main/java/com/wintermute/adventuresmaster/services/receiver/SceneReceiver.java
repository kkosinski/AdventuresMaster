package com.wintermute.adventuresmaster.services.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.wintermute.adventuresmaster.services.player.SceneManager;

/**
 * Receives stoping service over button click on notification.
 *
 * @author wintermute
 */
public class SceneReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.stopService(new Intent(context, SceneManager.class));
    }
}
