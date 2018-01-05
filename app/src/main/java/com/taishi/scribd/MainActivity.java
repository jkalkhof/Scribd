package com.taishi.scribd;

import android.app.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.taishi.scribd.adapters.RecyclerViewDataAdapter;
import com.taishi.scribd.models.SectionDataModel;
import com.taishi.scribd.models.SingleItemModel;
import com.taishi.scribd.models.book.Book;
import com.taishi.scribd.models.book.Item;
import com.taishi.scribd.services.BookService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private Toolbar toolbar;
	ArrayList<SectionDataModel> allSampleData;
	RecyclerView my_recycler_view;

	RecyclerViewDataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		allSampleData = new ArrayList<SectionDataModel>();

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setTitle("Book Finder");

		}


		String genre[] = {"Science fiction 〉","Romance 〉","Mystery 〉","Horror 〉","Travel 〉"};
		// test other genres
		//String genre[] = {"Sports > ", "Aikido > ", "Science fiction 〉","Romance 〉"};
		getAllData(genre);

		my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

		my_recycler_view.setHasFixedSize(true);

//		adapter = new RecyclerViewDataAdapter(this, allSampleData);

//		my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//		my_recycler_view.setAdapter(adapter);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	public void getAllData(String genre[]) {

		for (int i = 0; i < genre.length; i++) {

			SectionDataModel dm = new SectionDataModel();

			BookAsyncTask atClass = new BookAsyncTask(genre[i]);
			atClass.execute();

		}
	}


	public class BookAsyncTask extends AsyncTask<Void,Void,List<Item>> {
		List<Item> itemList;
		String headerTitle;
		public BookAsyncTask(String headerTitle) {
			super();
			this.headerTitle = headerTitle;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Item> doInBackground(Void... voids) {
			BookService bookService = BookService.retrofit.create(BookService.class);
			final Call<Book> call = bookService.requestBook(headerTitle);

			try {
				Book book =  call.execute().body();
				itemList = book.getItems();

				Log.v("MainActivity", "BookAsyncTask: requestBook: " + headerTitle);

				// debugging - show the book titles
//				for (int i =  0; i < itemList.size() && i < 10; i++) {
//					Item bookItem = itemList.get(i);
//					Log.v("MainActivity", "BookAsyncTask: title["+i+"]: " + bookItem.getVolumeInfo().getTitle());
//				}

//				book.getItems().get(0).getVolumeInfo().getImageLinks().getSmallThumbnail();
//				item_list = explore.getResponse().getGroups().get(0).getItems();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return itemList;
		}

		@Override
		protected void onPostExecute(List<Item> item_s) {
			super.onPostExecute(item_s);

			SectionDataModel dm = new SectionDataModel();
			dm.setHeaderTitle(headerTitle);
			dm.setAllItemsInSection(itemList);

			allSampleData.add(dm);

			adapter = new RecyclerViewDataAdapter(getApplicationContext(), allSampleData, MainActivity.this);

			my_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
			my_recycler_view.setAdapter(adapter);
		}
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {

			DialogFragment newFragment = new DialogFragment();
			newFragment.show(getSupportFragmentManager(),"test");
//			newFragment.show(getFragmentManager(), "test");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}


}
