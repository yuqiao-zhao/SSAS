package com.example.ssas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Database database;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.ssas", appContext.getPackageName());
    }

    @Test
    public void queryLogin()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = new Database(appContext,"SSAS", null, 1);
        SQLiteDatabase db = database.getWritableDatabase();
        database.setDb(db);

        ReturnMsg msg = database.queryLogin("123","1223");
        assertEquals(true, msg.getSuccess());
    }
}
