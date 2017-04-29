package com.udacity.stockhawk.widget;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.DetailActivity;


/**
 * IntentService which handles updating all Today widgets with the latest data
 */
public class WidgetService extends RemoteViewsService {
    public final String LOG_TAG = WidgetService.class.getSimpleName();
    private static final String[] QUOTE_COLUMNS = {
            Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE
    };
    // these indices must match the projection
    static final int QUOTE_ID = 0;
    static final int QUOTE_SYMBOL = 1;
    static final int QUOTE_PRICE = 2;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                Uri weatherForLocationUri = Contract.Quote.URI;
                data = getContentResolver().query(weatherForLocationUri,
                        QUOTE_COLUMNS,
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL+ " ASC");
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_layout_item);
                int quoteId = data.getInt(QUOTE_ID);
                String symbol = data.getString(QUOTE_SYMBOL);
                long price = data.getLong(QUOTE_PRICE);

                 views.setTextViewText(R.id.widget_symbol, symbol);
                views.setTextViewText(R.id.widget_price,"$"  + String.valueOf(price));

                final Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("symbol",symbol);
                views.setOnClickFillInIntent(R.id.widget_list_item, detailIntent);
                return views;
            }



            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_layout_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(QUOTE_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}