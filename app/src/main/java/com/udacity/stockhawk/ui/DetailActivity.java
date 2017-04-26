package com.udacity.stockhawk.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udacity.stockhawk.R;

import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class DetailActivity extends AppCompatActivity {
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("symbol") != null) {
            symbol = bundle.getString("symbol");
            TextView tx = (TextView) findViewById(R.id.title);
            tx.setText(symbol);

            new CheckSymbolTask().execute(symbol);
        }
    }

    /**
     * Created to check if a symbol is valid.
     */
    class CheckSymbolTask extends AsyncTask<String, Void, List<HistoricalQuote>> {
        private String symbol;

        protected List<HistoricalQuote> doInBackground(String... symbols) {
            List<HistoricalQuote> histQuotes = null;
            try {
                symbol = symbols[0];
                Calendar fromCalendar = Calendar.getInstance();
                Calendar toCalendar = Calendar.getInstance();
                fromCalendar.add(Calendar.DAY_OF_MONTH, -7);
                Stock stock = YahooFinance.get(symbol, fromCalendar, toCalendar, Interval.DAILY);
                histQuotes = stock.getHistory();
            } catch (Exception e) {
            }
            return histQuotes;
        }

        protected void onPostExecute(final List<HistoricalQuote> result) {
            if (result.size() == 7) {
                GraphView graph = (GraphView) findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint(0, 1),
                        new DataPoint(1, 0),
                        new DataPoint(2, 2),
                        new DataPoint(3, 0),
                        new DataPoint(4, 1)
                });
                graph.addSeries(series);
            }
        }
    }
}
