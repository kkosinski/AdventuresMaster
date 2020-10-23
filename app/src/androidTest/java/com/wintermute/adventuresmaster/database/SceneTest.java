package com.wintermute.adventuresmaster.database;

import static org.junit.Assert.assertFalse;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioInScene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Board;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Scene;
import com.wintermute.adventuresmaster.database.entity.tools.gm.SceneDesc;
import com.wintermute.adventuresmaster.database.repository.BoardRepository;
import com.wintermute.adventuresmaster.database.repository.SceneRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SceneTest
{

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void createScene() throws InterruptedException
    {
        Context context = ApplicationProvider.getApplicationContext();

        Board board = new Board("test board", "scenes", true, -1L);
        BoardRepository boardRepo = new BoardRepository(context);
        board.setId(boardRepo.storeBoard(board));

        SceneRepository sceneRepo = new SceneRepository(context);
        Scene scene = new Scene("test scene", board.getId());
        scene.setId(sceneRepo.storeScene(scene));

        AudioFile audioFile = new AudioFile("test_uri");
        audioFile.setId(sceneRepo.storeAudioFile(audioFile));

        AudioInScene audioInScene = new AudioInScene(2, false, false, "effect");
        audioInScene.setInScene(scene.getId());
        audioInScene.setAudioFile(audioFile.getId());
        audioInScene.setId(sceneRepo.storeSceneAudio(audioInScene));

        List<SceneDesc> scenes = LiveDataTestUtil.getValue(sceneRepo.getScenesForBoard(board.getId()));

        assertFalse(scenes.isEmpty());
    }
}
