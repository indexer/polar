package indexer.com.polar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import indexer.com.polar.R;
import indexer.com.polar.base.BaseActivity;
import indexer.com.polar.model.Feed;
import indexer.com.polar.widget.TextAwesome;
import java.util.ArrayList;

public class DetailActivity extends BaseActivity {
  public static String strSeparator = "__,__";
  @Bind(R.id.previewImage) ImageView mImageView;
  @Bind(R.id.title) TextView mTextViewTitle;
  @Bind(R.id.summery) LinearLayout mLinearLayout;
  @Bind(R.id.orginal_web) TextView mOriginalWeb;
  Feed mFeed;

  public static ArrayList<String> convertStringToArray(String str) {
    ArrayList<String> arr = new ArrayList<>();
    String[] item = str.split(strSeparator);
    for (int i = 0; i < item.length; i++) {
      arr.add(item[i]);
    }
    return arr;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    mFeed = getIntent().getParcelableExtra(String.valueOf(R.string.object_tag));
    Picasso.with(this)
        .load(mFeed.getImageUrl())
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(mImageView);
    mTextViewTitle.setText(mFeed.getTitle());
    mOriginalWeb.setText(mFeed.getOriginalUrl());
    ArrayList<String> stringArrayList = convertStringToArray(mFeed.getSummaryString());
    for (int position = 0; position < stringArrayList.size(); position++) {
      TextAwesome mText = new TextAwesome(this);
      mText.setText(
          getResources().getString(R.string.fa_laptop) + " " + stringArrayList.get(position));
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT));
      params.setMargins(8, 8, 8, 8);
      mText.setLayoutParams(params);
      mText.setTextSize(18);
      mLinearLayout.addView(mText);
    }
    AdView mAdView = (AdView) findViewById(R.id.adView);
    // Create an ad request. Check logcat output for the hashed device ID to
    // get test ads on a physical device. e.g.
    // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
    AdRequest adRequest =
        new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
    mAdView.loadAd(adRequest);
  }

  @Override protected boolean needFabButton() {
    return false;
  }

  @Override protected int getLayoutResource() {
    return R.layout.activity_detail;
  }

  @Override protected boolean getHomeUpEnabled() {
    return true;
  }

  @Override protected boolean needToolbar() {
    return true;
  }

  @Override protected int toolBarIndicator() {
    return R.drawable.ic_action_name;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.detail, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        supportFinishAfterTransition();
        break;
      case R.id.menu_share:
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setText(mFeed.getOriginalUrl())
            .getIntent(), getString(R.string.share)));
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
