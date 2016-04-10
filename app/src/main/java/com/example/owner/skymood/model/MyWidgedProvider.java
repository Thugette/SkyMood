package com.example.owner.skymood.model;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Created by Golemanovaa on 10.4.2016 г..
 */
public class MyWidgedProvider extends AppWidgetProvider {


    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            int widgetId = appWidgetIds[i];
        }
    }
}
