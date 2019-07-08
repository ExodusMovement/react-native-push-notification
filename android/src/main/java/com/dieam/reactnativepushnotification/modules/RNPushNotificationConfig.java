package com.dieam.reactnativepushnotification.modules;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import android.support.v4.content.res.ResourcesCompat;

class RNPushNotificationConfig {
    private static Bundle metadata;
    private Context context;

    public RNPushNotificationConfig(Context context) {
        this.context = context;
        if (metadata == null) {
            try {
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                metadata = applicationInfo.metaData;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Log.e(RNPushNotification.LOG_TAG, "Error reading application meta, falling back to defaults");
                metadata = new Bundle();
            }
        }
    }

    public boolean getChannelVibration(String channelId) {
        return getBoolean(channelId + ".vibration_boolean", true);
    }

    public boolean getChannelLights(String channelId) {
        return getBoolean(channelId + ".lights_boolean", true);
    }

    public boolean getChannelShowBadge(String channelId) {
        return getBoolean(channelId + ".badge_boolean", true);
    }

    public boolean getChannelSilent(String channelId) {
        return getBoolean(channelId + ".silent_boolean", false);
    }

    public String getChannelSoundName(String channelId) {
        return getString(channelId + ".sound_string", "default");
    }

    public String getChannelImportanceString(String channelId) {
        return getString(channelId + ".importance_string", "default");
    }

    public String getChannelDescription(String channelId) {
        return getString(channelId + ".description_string", "default");
    }

    // backwards
    public int getNotificationColor() {
        try {
            int resourceId = metadata.getInt(RNPushNotificationConstants.KEY_NOTIFICATION_COLOR);
            return ResourcesCompat.getColor(context.getResources(), resourceId, null);
        } catch (Exception e) {
            Log.w(RNPushNotification.LOG_TAG, "Unable to find " + RNPushNotificationConstants.KEY_NOTIFICATION_COLOR + " in manifest. Falling back to default");
        }

        // Default
        return -1;
    }

    private String getString(String name, String defaultValue) {
        return metadata.getString(name, defaultValue);
    }

    private boolean getBoolean(String name, boolean defaultValue) {
        return metadata.getBoolean(name, defaultValue);
    }

    private int getColor(String name, int defaultValue) {
        Object color = metadata.get(name);
        return color == null ? defaultValue : (Integer) color;
    }
}
