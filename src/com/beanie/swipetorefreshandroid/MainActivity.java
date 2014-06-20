package com.beanie.swipetorefreshandroid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnRefreshListener {

	private final static String TAG = "MainActivity";

	private ArrayAdapter<String> adapter;

	private SwipeRefreshLayout refreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeViews();
	}

	private void initializeViews() {

		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

		refreshLayout.setOnRefreshListener(this);
		
		// The default colors for the progress bar are not so nice.
		setColorSchemeForProgressBar();

		ListView listView = (ListView) findViewById(R.id.listView);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		adapter.addAll(getDemoData(5));
		listView.setAdapter(adapter);
	}

	// Pre-populate initial data in the list view
	private Collection<String> getDemoData(int items) {
		Collection<String> data = new ArrayList<String>();
		for (int i = 0; i < items; i++) {
			data.add("Item number " + i);
		}
		return data;
	}

	@Override
	public void onRefresh() {
		Log.i(TAG, "Refresh Requested");

		doRefresh();
	}

	// Fetch data and update listview's adapter
	private void doRefresh() {
		RefreshTask task = new RefreshTask();
		task.execute((Void) null);
	}

	private void postRefreshComplete() {
		// Stop the refresh animation
		refreshLayout.setRefreshing(false);

		// Update adapter with new data
		adapter.clear();
		adapter.addAll(getDemoData(new Random().nextInt(20)));
		adapter.notifyDataSetChanged();
	}

	private void setColorSchemeForProgressBar() {
		refreshLayout
				.setColorScheme(android.R.color.holo_orange_dark,
						android.R.color.holo_orange_light,
						android.R.color.holo_blue_dark,
						android.R.color.holo_blue_light);
	}

	class RefreshTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			postRefreshComplete();
		}

	}
}
