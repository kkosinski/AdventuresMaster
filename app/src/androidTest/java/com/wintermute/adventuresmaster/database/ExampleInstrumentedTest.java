package com.wintermute.adventuresmaster;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.wintermute.adventuresmaster.database.TestDatabase;
import com.wintermute.adventuresmaster.database.entity.tools.gm.AudioFile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    TestDatabase testDb;

    @Test
    public void test(){
        // Context of the app under test.
        Context ctx = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(ctx, TestDatabase.class).build();

        AudioFile target = new AudioFile("/path/to/test6");
        long insert = testDb.audioFileDao().insert(target);

        Assert.assertEquals(1, insert);

        System.out.println(insert);
    }
}
