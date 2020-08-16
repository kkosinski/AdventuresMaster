package com.wintermute.adventuresmaster.database;

import android.content.Context;
import static org.mockito.Mockito.*;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Testing pre populated database.
 *
 * @author wintermute
 */
public class AppDatabaseTest
{
    private static AppDatabase appDatabase;

    @BeforeClass
    public static void createDatabase(){
        Context ctx = mock(Context.class);
        appDatabase = AppDatabase.getAppDatabase(ctx.getApplicationContext());
    }

    @Test
    public void checkMenuContent(){
        System.out.println("alles gut");
    }
}
