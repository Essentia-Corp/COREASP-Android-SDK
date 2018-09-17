package com.coreasp_sdk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;

import com.coreasp.CoreAspManager;
import com.coreasp.CorePushManager;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CorePushManagerTest extends TestCase {

    Context appContext = InstrumentationRegistry.getTargetContext();

    //初期化処理
    @Before
    public void setUp() throws Exception {
        super.setUp();

        SharedPreferences preferences = appContext.getSharedPreferences(CorePushManager.COREPUSH_SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("COREPUSH_APP_USER_ID_KEY");
        editor.remove("COREPUSH_CATEGORY_IDS_KEY");
        editor.remove("COREPUSH_MULTI_CATEGORY_IDS_KEY");
        editor.apply();

        CoreAspManager.initialize(appContext);
    }

    @Test
    public void testConfigKey() {
        assertEquals("389e00f9718b40f360b3846944c7d7b4", CorePushManager.getInstance().getConfigKey());
    }

    @Test
    public void testActivity() {
        assertEquals(MainActivity.class, CorePushManager.getInstance().getActivity());
    }

    @Test
    public void testIconResourceId() {
        assertEquals(R.mipmap.ic_launcher, CorePushManager.getInstance().getIconResourceId());
    }

    @Test
    public void testIconAccentColor() {
        assertEquals(ContextCompat.getColor(appContext, R.color.colorAccent), CorePushManager.getInstance().getIconAccentColor());
    }

    @Test
    public void testNotificationStyle() {
        assertEquals(CorePushManager.NOTIFICATION_STYLE_DIALOG_AND_STATUS_BAR, CorePushManager.getInstance().getNotificationStyle());
    }

    @Test
    public void testDebugLogEnabled() {
        boolean debug = CoreAspManager.getInstance().isDebugLogEnabled();
        assertTrue(debug);
    }

    @Test
    public void testNotificationCollapsed() {
        boolean isNotificationCollapsed = CorePushManager.getInstance().isNotificationCollapsed();
        assertFalse(isNotificationCollapsed);
    }

}