package com.wintermute.adventuresmaster.services.player;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFileWithOpts;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Handles playing audio tracks, scenes and effects from soundboard.
 *
 * @author wintermute
 */
@Data
public class GameAudioPlayer
{
    private static GameAudioPlayer INSTANCE;
    private Map<String, AudioFileWithOpts> audioForTasks;
    private Map<String, MediaPlayer> players = new HashMap<>();

    private GameAudioPlayer() {}

    /**
     * @return instance.
     */
    public static GameAudioPlayer getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new GameAudioPlayer();
        }
        return INSTANCE;
    }

    private List<Thread> prepareScene(Context context)
    {
        List<Thread> threads = new ArrayList<>();

        final int[] wait = {0};
        if (audioForTasks.containsKey("effect"))
        {
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(context,
                Uri.parse(audioForTasks.get("effect").getAudioFiles().get(0).getUri()));
            String durationStr = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            wait[0] = Integer.parseInt(durationStr);
        }

        for (Map.Entry<String, AudioFileWithOpts> target : audioForTasks.entrySet())
        {
            Thread t = new Thread(() ->
            {
                MediaPlayer value =
                    MediaPlayer.create(context, Uri.parse(target.getValue().getAudioFiles().get(0).getUri()));
                String tag = target.getKey();
                players.put(tag, value);

                AudioInScene audioInScene = target.getValue().getAudioInScene();
                adjustVolume(tag, audioInScene.getVolume());
                setLoopForPlayer(tag, audioInScene.isRepeat());

                if (audioForTasks.containsKey("effect"))
                {
                    if (audioInScene.isPlayAfterEffect())
                    {

                        try
                        {
                            Thread.sleep(wait[0]);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                players.get(tag).start();
            });
            threads.add(t);
        }
        return threads;
    }

    /**
     * Plays audio contained in scene.
     *
     * @param context of calling activity.
     * @param audio list of prepared audio tracks with opts to play in scene.
     */
    public void playScene(Context context, List<AudioFileWithOpts> audio)
    {
        audioForTasks = new HashMap<>();
        audio.forEach(a -> preparePlayer(context, a));
        List<Thread> threads = prepareScene(context);
        threads.forEach(Thread::start);
    }

    /**
     * Prepares player for playing in scene.
     *
     * @param context of calling activity
     * @param audioFile audio file with settings.
     */
    public void preparePlayer(Context context, AudioFileWithOpts audioFile)
    {
        AudioFile file = audioFile.getAudioFiles().get(0);
        AudioInScene audio = audioFile.getAudioInScene();
        String tag = audio.getTag();
        players.put(tag, MediaPlayer.create(context, Uri.parse(file.getUri())));

        if (players.get(tag) != null)
        {
            Objects.requireNonNull(players.get(tag)).setLooping(audio.isRepeat());
            adjustVolume(tag, audio.getVolume());
            audioForTasks.put(tag, audioFile);
        }
    }

    /**
     * Set volume for target audio track.
     *
     * @param target audio track.
     * @param volume to set for the target audio track.
     */
    public void adjustVolume(String target, int volume)
    {
        if (players.get(target) != null)
        {
            Objects.requireNonNull(players.get(target)).setVolume(volume / 10f, volume / 10f);
        }
    }

    /**
     * Stops all players.
     */
    public void stopAll()
    {
        players.values().forEach(p ->
        {
            if (p != null)
            {
                p.stop();
            }
        });
    }

    /**
     * Plays a track for single audio type.
     *
     * @param context of calling activity
     * @param audio target audio file with settings.
     */
    public void playAudioType(Context context, AudioFileWithOpts audio)
    {
        String tag = audio.getAudioInScene().getTag();
        MediaPlayer target = players.get(tag) != null ? players.get(tag) : new MediaPlayer();

        if (target.isPlaying())
        {
            target.stop();
        } else
        {
            target = MediaPlayer.create(context, Uri.parse(audio.getAudioFiles().get(0).getUri()));
            AudioInScene audioInScene = audio.getAudioInScene();
            target.setVolume(audioInScene.getVolume() / 10f, audio.getAudioInScene().getVolume() / 10f);
            target.setLooping(audioInScene.isRepeat());
            target.start();
        }
        players.put(tag, target);
    }

    /**
     * Change looping state of target player.
     *
     * @param tag of audio player type.
     * @param loop state.
     */
    public void setLoopForPlayer(String tag, boolean loop)
    {
        MediaPlayer target = players.get(tag);
        if (target != null)
        {
            target.setLooping(loop);
            players.put(tag, target);
        }
    }
}