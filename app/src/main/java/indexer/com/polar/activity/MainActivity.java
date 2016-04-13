package indexer.com.polar.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
  @BindBool(R.bool.isLand) boolean isLand;
  ActionBarDrawerToggle mDrawerToggle;
  Context mContext;

  ArrayList<Feed> mFeedArrayList = new ArrayList<>();
  int spacingInPixels = 8;
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

  @Override public void setValues(ArrayList<?> feed) {
    if (feed != null) {
      mDrawerLayout.closeDrawers();
      mLatestFeedAdapter.setCategory((ArrayList<Feed>) feed);
      mLatestFeedAdapter.notifyDataSetChanged();
    }
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return FeedLoader.newAllArticlesInstance(this);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
    intent.setAction("com.polar.Broadcast");
    intent.putExtra("image_url",
        "http://www.eugenewei.com/blog/2015/8/17/the-decline-of-the-phone-call");
    sendBroadcast(intent);

    mLatestFeedAdapter = new LatestFeedAdapter();
    mLatestFeedAdapter.setCursor(data);
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

  @Override public void onLoaderReset(Loader<Cursor> loader) {

  }
}
