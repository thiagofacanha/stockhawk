package com.udacity.stockhawk.widget;


import android.app.IntentService;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;


/**
 * IntentService which handles updating all Today widgets with the latest data
 */
public class WidgetService extends IntentService {
    RecyclerView recyclerView;


    public WidgetService() {
        super("WidgetService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update

    }


}