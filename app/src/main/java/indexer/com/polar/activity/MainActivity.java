package indexer.com.polar.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.BindBool;
import butterknife.ButterKnife;
import indexer.com.polar.R;
import indexer.com.polar.adapter.DrawerAdapter;
import indexer.com.polar.adapter.LatestFeedAdapter;
import indexer.com.polar.base.BaseActivity;
import indexer.com.polar.callback.RestCallBack;
import indexer.com.polar.data.FeedLoader;
import indexer.com.polar.listener.DataTransferInterface;
import indexer.com.polar.model.Category;
import indexer.com.polar.model.Feed;
import indexer.com.polar.rest.RestClient;
import indexer.com.polar.sync.FeedSyncAdapter;
import indexer.com.polar.widget.LatestFeedWidget;
import indexer.com.polar.widget.SpaceItemDecoration;
import java.util.ArrayList;
import retrofit.Call;
import retrofit.Response;

public class MainActivity extends BaseActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, DataTransferInterface {

  //
  static String mTAG = "feed";
  @Bind(R.id.left_drawer) RecyclerView mRecyclerView;
  @Bind(R.id.mRecyclerLatest) RecyclerView mLatestRecyclerView;
  @Bind(R.id.drawerLayout) DrawerLayout mDrawerLayout;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.mProgress) ProgressBar mProgressBar;
  @BindBool(R.bool.isLand) boolean isLand;
  ActionBarDrawerToggle mDrawerToggle;
  Context mContext;
  SearchView mSearchView;

  ArrayList<Feed> mFeedArrayList = new ArrayList<>();
  int spacingInPixels = 2;
  //Adapter
  private DrawerAdapter mDrawerAdapter;
  private LatestFeedAdapter mLatestFeedAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    mContext = this;
    FeedSyncAdapter.initializeSyncAdapter(getApplicationContext());

    mDrawerAdapter = new DrawerAdapter(this, this);
    getSupportLoaderManager().initLoader(0, null, this);

    mRecyclerView.setAdapter(mDrawerAdapter);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    Call<ArrayList<Category>> categoryList = RestClient.getService(this).getAllcategories();
    categoryList.enqueue(new RestCallBack<ArrayList<Category>>() {
      @Override public void onResponse(Response<ArrayList<Category>> response) {
        mDrawerAdapter.setCategory(response.body());
        mDrawerAdapter.notifyDataSetChanged();
      }
    });

    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer,
        R.string.close_drawer) {

      @Override public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
        // open I am not going to put anything here)
      }

      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        // Code here will execute once drawer is closed
      }
    }; // Drawer Toggle Object Made
    mDrawerLayout.addDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
    mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putParcelableArrayList(mTAG, mFeedArrayList);
    super.onSaveInstanceState(outState);
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      mFeedArrayList = savedInstanceState.getParcelableArrayList(mTAG);
      if (mFeedArrayList != null) {
        //mLatestFeedAdapter.setCategory(mFeedArrayList);
        mLatestRecyclerView.setAdapter(mLatestFeedAdapter);
        mLatestRecyclerView.setHasFixedSize(true);
        mLatestRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        if (isLand) {
          mLatestRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
          mLatestRecyclerView.setLayoutManager(
              new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
      }
    }
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    MenuItem item = menu.findItem(R.id.menu_song_search);
    mSearchView = new SearchView(getSupportActionBar().getThemedContext());
    mSearchView.setIconifiedByDefault(true);

    MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
    MenuItemCompat.setActionView(item, mSearchView);
    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        mProgressBar.setVisibility(View.VISIBLE);
        mLatestRecyclerView.setVisibility(View.GONE);
        Call<Feed> getFeedBySearchWords =
            RestClient.getService(MainActivity.this).getFeedBySearchWords(query);
        getFeedBySearchWords.enqueue(new RestCallBack<Feed>() {
          @Override public void onResponse(Response<Feed> response) {

            mProgressBar.setVisibility(View.GONE);
            if (response.code() == 404) {
              Snackbar snackbar =
                  Snackbar.make(mRecyclerView, R.string.error_search, Snackbar.LENGTH_LONG);
              snackbar.show();
            } else {
              ArrayList<Feed> mArrayList = new ArrayList<>();
              mArrayList.add(response.body());
              mProgressBar.setVisibility(View.GONE);
              mLatestRecyclerView.setVisibility(View.VISIBLE);
              mLatestFeedAdapter.setCategory(mArrayList);
              mLatestFeedAdapter.notifyDataSetChanged();
            }
          }
        });
        return true;
      }

      @Override public boolean onQueryTextChange(String newText) {

        return false;
      }
    });

    return super.onCreateOptionsMenu(menu);
  }

  @Override protected boolean needFabButton() {
    return false;
  }

  @Override protected int getLayoutResource() {
    return R.layout.activity_main;
  }

  @Override protected boolean getHomeUpEnabled() {
    return true;
  }

  @Override protected boolean needToolbar() {
    return true;
  }

  @Override protected int toolBarIndicator() {
    return R.drawable.ic_drawer;
  }

  @Override public void setValues(String category) {
    mDrawerLayout.closeDrawers();
    Call<ArrayList<Feed>> feedByCategory =
        RestClient.getService(this).getFeedListByCategory(50, category);
    feedByCategory.enqueue(new RestCallBack<ArrayList<Feed>>() {
      @Override public void onResponse(Response<ArrayList<Feed>> response) {
        mProgressBar.setVisibility(View.VISIBLE);
        mLatestRecyclerView.setVisibility(View.GONE);
        mDrawerLayout.closeDrawers();
        if (response.body().size() > 0) {
          mProgressBar.setVisibility(View.GONE);
          mLatestRecyclerView.setVisibility(View.VISIBLE);
          mLatestFeedAdapter.setCategory(response.body());
          mLatestFeedAdapter.notifyDataSetChanged();
        } else {
          mProgressBar.setVisibility(View.GONE);
          mLatestRecyclerView.setVisibility(View.VISIBLE);
          Snackbar snackbar =
              Snackbar.make(mRecyclerView, R.string.error_search, Snackbar.LENGTH_LONG);
          snackbar.show();
        }
      }
    });
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return FeedLoader.newAllArticlesInstance(this);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (data.getCount() == 0) {
      mProgressBar.setVisibility(View.VISIBLE);
    } else {
      mProgressBar.setVisibility(View.GONE);
      mLatestRecyclerView.setVisibility(View.VISIBLE);
      mLatestFeedAdapter = new LatestFeedAdapter();
      mLatestFeedAdapter.setCursor(data);
      mLatestRecyclerView.setAdapter(mLatestFeedAdapter);
      mLatestRecyclerView.setHasFixedSize(true);
      mLatestRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
      Intent intent = new Intent(this, LatestFeedWidget.class);
      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      intent.putExtra(String.valueOf(R.string.object_tag), mLatestFeedAdapter.getLatestFeed());
      sendBroadcast(intent);
      if (isLand) {
        mLatestRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
      } else {
        mLatestRecyclerView.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
      }
    }
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {

  }
}
