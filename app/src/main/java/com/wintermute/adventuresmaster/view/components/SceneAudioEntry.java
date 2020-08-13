package com.wintermute.adventuresmaster.view.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.wintermute.adventuresmaster.R;

/**
 * Custom component representing entry of audio file within a scene.
 *
 * @author wintermute
 */
public class SceneAudioEntry extends LinearLayout
{
    /**
     * Handles clicks on select audio buttons.
     */
    public interface OnSelectAudioClick
    {
        void onSelectAudioClickListener();
    }

    /**
     * Handles clicks on play audio buttons.
     */
    public interface OnPlayAudioClick
    {
        void onPlayClickListener();
    }

    private OnSelectAudioClick onSelectAudioClick;
    private OnPlayAudioClick onPlayAudioClick;

    private Button selectAudio;
    private CheckBox loopBox;
    private String sceneAudioFileTitle;

    private SeekBar volumeBar;
    private boolean playInLoop;
    private int volume;

    /**
     * Creates an instance.
     *
     * @param context of calling activity.
     * @param attrs attribute set of calling activity.
     */
    public SceneAudioEntry(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        inflate(context, R.layout.audio_entry, this);

        TypedArray resources = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SceneAudioEntry, 0, 0);
        try
        {
            sceneAudioFileTitle = resources.getString(R.styleable.SceneAudioEntry_styleable_scene_title);
            playInLoop = resources.getBoolean(R.styleable.SceneAudioEntry_styleable_scene_loop, false);
            volume = resources.getInt(R.styleable.SceneAudioEntry_styleable_scene_volume, 5);
        } finally
        {
            resources.recycle();
        }

        initComponents();

        setSceneAudioFileTitle(sceneAudioFileTitle);
        setPlayInLoop(playInLoop);
        setVolume(volume);
    }

    private void initComponents()
    {
        selectAudio = findViewById(R.id.selectAudioFile);
        selectAudio.setOnClickListener(v ->
        {
            if (onSelectAudioClick != null)
            {
                onSelectAudioClick.onSelectAudioClickListener();
            }
        });

        Button playAudio = findViewById(R.id.play);
        playAudio.setOnClickListener(v ->
        {
            if (onPlayAudioClick != null)
            {
                onPlayAudioClick.onPlayClickListener();
            }
        });

        volumeBar = findViewById(R.id.volumebar);
        loopBox = findViewById(R.id.loopAudio);
    }

    /**
     * Assigns the click listener interface.
     *
     * @param onSelectAudioClick interface to bind.
     */
    public void setOnSelectAudioClick(OnSelectAudioClick onSelectAudioClick)
    {
        this.onSelectAudioClick = onSelectAudioClick;
    }

    /**
     * Assigns the click listener interface.
     *
     * @param onPlayAudioClick interface to bind.
     */
    public void setOnPlayAudioClick(OnPlayAudioClick onPlayAudioClick)
    {
        this.onPlayAudioClick = onPlayAudioClick;
    }

    /**
     * @return scene audio title file in this component.
     */
    public String getSceneAudioFileTitle()
    {
        return sceneAudioFileTitle;
    }

    /**
     * @param sceneAudioFileTitle audio file title.
     */
    public void setSceneAudioFileTitle(String sceneAudioFileTitle)
    {
        this.sceneAudioFileTitle = sceneAudioFileTitle;
        selectAudio.setText(sceneAudioFileTitle);
        invalidate();
        requestLayout();
    }

    /**
     * @return if selected audio file in this scene should be played in loop.
     */
    public boolean isPlayInLoop()
    {
        return playInLoop;
    }

    /**
     * @param playInLoop state.
     */
    public void setPlayInLoop(boolean playInLoop)
    {
        this.playInLoop = playInLoop;
        loopBox.setChecked(playInLoop);
        invalidate();
        requestLayout();
    }

    /**
     * @return volume set for this audio file.
     */
    public int getVolume()
    {
        return volume;
    }

    /**
     * @param volume to set for this audio file.
     */
    public void setVolume(int volume)
    {
        this.volume = volume;
        volumeBar.setProgress(volume);
        invalidate();
        requestLayout();
    }
}
