package com.wintermute.adventuresmaster.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
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
        void onSelectAudioClickListener(String tag);
    }

    /**
     * Handles clicks on play audio buttons.
     */
    public interface OnPlayAudioClick
    {
        void onPlayClickListener(String tag);
    }

    public interface OnChangedVolume
    {
        void onChangedVolume(int progress, String tag);
    }

    private OnSelectAudioClick onSelectAudioClick;
    private OnPlayAudioClick onPlayAudioClick;
    private OnChangedVolume onChangedVolume;

    private Button selectAudio;
    private CheckBox loopBox, playAfterEffectBox;
    private String sceneAudioFileTitle;

    private SeekBar volumeBar;
    private boolean repeatTrack, playAfterEffect;
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
            repeatTrack = resources.getBoolean(R.styleable.SceneAudioEntry_styleable_scene_loop, false);
            playAfterEffect = resources.getBoolean(R.styleable.SceneAudioEntry_styleable_scene_after_effect, false);
            volume = resources.getInt(R.styleable.SceneAudioEntry_styleable_scene_volume, 5);
        } finally
        {
            resources.recycle();
        }

        initComponents();

        setSceneAudioFileTitle(sceneAudioFileTitle);
        setRepeatTrack(repeatTrack);
        setPlayAfterEffect(playAfterEffect);
        setVolume(volume);
    }

    private void initComponents()
    {
        selectAudio = findViewById(R.id.audio_entry_select_audio_file);
        selectAudio.setOnClickListener(v ->
        {
            if (onSelectAudioClick != null)
            {
                onSelectAudioClick.onSelectAudioClickListener(
                    ((View) v.getParent().getParent().getParent()).getTag().toString());
            }
        });

        Button playAudio = findViewById(R.id.audio_entry_play);
        playAudio.setOnClickListener(v ->
        {
            if (onPlayAudioClick != null)
            {
                onPlayAudioClick.onPlayClickListener(((View) v.getParent().getParent()).getTag().toString());
            }
        });

        volumeBar = findViewById(R.id.audio_entry_volumebar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                setVolume(progress);
                if (onChangedVolume != null)
                {
                    onChangedVolume.onChangedVolume(progress,
                        ((View) seekBar.getParent().getParent()).getTag().toString());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        loopBox = findViewById(R.id.audio_entry_repeat_track);
        loopBox.setOnCheckedChangeListener((buttonView, isChecked) -> setRepeatTrack(isChecked));

        playAfterEffectBox = findViewById(R.id.audio_entry_play_after_effect);
        playAfterEffectBox.setOnCheckedChangeListener((buttonView, isChecked) -> setPlayAfterEffect(isChecked));
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

    public void setOnChangedVolume(OnChangedVolume onChangedVolume)
    {
        this.onChangedVolume = onChangedVolume;
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
    public boolean isRepeatTrack()
    {
        return repeatTrack;
    }

    /**
     * @param repeatTrack describes if track should be played in loop or not.
     */
    public void setRepeatTrack(boolean repeatTrack)
    {
        this.repeatTrack = repeatTrack;
        loopBox.setChecked(repeatTrack);
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

    /**
     * @return the flag if the audio entry should play once the effect audio is done or not.
     */
    public boolean isPlayAfterEffect()
    {
        return playAfterEffect;
    }

    /**
     * @param playAfterEffect to determine if audio entry should be started in player once the effect audio is
     *     done.
     */
    public void setPlayAfterEffect(boolean playAfterEffect)
    {
        this.playAfterEffect = playAfterEffect;
        playAfterEffectBox.setChecked(playAfterEffect);
        invalidate();
        requestLayout();
    }

    /**
     * Hides the checkbox for playing after effect option. This is needed for the effect audio itself.
     */
    public void disablePlayAfterEffectOption()
    {
        playAfterEffectBox.setVisibility(GONE);
    }
}
