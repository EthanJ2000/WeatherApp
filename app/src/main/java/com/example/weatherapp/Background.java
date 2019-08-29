package com.example.weatherapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Calendar;

public class Background extends AppCompatActivity {
    private static final String TAG = "Background";
    private ImageView background;
    private Calendar c = Calendar.getInstance();
    private int timeOfDay = c.get(Calendar.HOUR_OF_DAY);


    public Background(ImageView background) {
        this.background = background;
    }

    public WeatherStatusColor setWeatherBackground()
    {
        if (timeOfDay >= 0 && timeOfDay < 12) {
            Log.i(TAG, "setWeatherBackground: Morning");
//            theme.applyStyle(R.style.morningOverlay, true);

            return WeatherStatusColor.MORNING;
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            Log.i(TAG, "setWeatherBackground: Afternoon");
//            theme.applyStyle(R.style.afternoonOverlay, true);
            return WeatherStatusColor.AFTERNOON;
        } else if (timeOfDay >= 16 && timeOfDay < 24) {
            Log.i(TAG, "setWeatherBackground: Night");
//            theme.applyStyle(R.style.nightOverlay, true);
            return WeatherStatusColor.NIGHT;
        }

        return null;
    }
}


