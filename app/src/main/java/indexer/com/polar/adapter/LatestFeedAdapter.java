package indexer.com.polar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import indexer.com.polar.R;
import indexer.com.polar.activity.DetailActivity;
import indexer.com.polar.base.BaseAdapter;
import indexer.com.polar.data.FeedLoader;
import indexer.com.polar.model.Feed;
import indexer.com.polar.widget.TextAwesome;
import java.util.ArrayList;

/**
 * Created by indexer on 10/31/15.
 */
public class LatestFeedAdapter extends BaseAdapter<BaseAdapter.BaseViewHolder> {
  Context mContext;
  ArrayList<Feed> mCategories;
  ImageView mImageView;
  TextView mText;
  TextView mTimeSaveView;
  TextAwesome mTextSlug;
  TextAwesome mTextOrginalUrl;
  CardView mCardView;
  private final Target target = new Target() {
    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
      mImageView.setImageBitmap(bitmap);
      Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
        @Override public void onGenerated(Palette palette) {
          //work with the palette here
          setViewSwatch(mText, palette.getDarkVibrantSwatch());
          setViewSwatchCard(mCardView, palette.getVibrantSwatch());
        }
      });
    }

    @Override public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
  };
  private Cursor mCursor;

  public LatestFeedAdapter() {
    mCategories = new ArrayList<>();
  }

  public void setViewSwatchCard(CardView view, Palette.Swatch swatch) {
    if (swatch != null) {
      view.setBackgroundColor(swatch.getRgb());
      view.setVisibility(View.VISIBLE);
    }
  }

  public void setViewSwatch(TextView view, Palette.Swatch swatch) {
    if (swatch != null) {
      view.setTextColor(swatch.getTitleTextColor());
      view.setVisibility(View.VISIBLE);
    }
  }

  public void setCursor(Cursor cursor) {
    mCursor = cursor;
    notifyDataSetChanged();
  }

  @Override public long getItemId(int position) {
    mCursor.moveToPosition(position);
    return mCursor.getLong(FeedLoader.Query._ID);
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    mContext = parent.getContext();
    View itemView = inflater.inflate(R.layout.feed_row, parent, false);
    return new ViewHolder(itemView, this);
  }

  @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
    ViewHolder myHolder = ((ViewHolder) holder);
    mCursor.moveToPosition(position);
    mImageView = myHolder.mImageView;
    mCardView = myHolder.mCardView;
    mTimeSaveView = myHolder.mTimeSaved;
    mTextOrginalUrl = myHolder.mUrlText;
    mTextSlug = myHolder.mCategory;
    mText = myHolder.mText;
    if (mCategories.size() == 0) {
      final String title = mCursor.getString(FeedLoader.Query.TITLE);
      final String previewImage = mCursor.getString(FeedLoader.Query.IMAGE_URL);
      final String decribeContent = mCursor.getString(FeedLoader.Query.ORGINAL_URL);
      String timeSave = mCursor.getString(FeedLoader.Query.TIMESAVE);
      final String category = mCursor.getString(FeedLoader.Query.CATEGORY);
      Log.e("Category", "" + category);
      final String summeryBullte = mCursor.getString(FeedLoader.Query.SUMMERY);
      mText.setText(title);
      mTextOrginalUrl.setText(decribeContent);
      mTimeSaveView.setText(timeSave);
      mTextSlug.setText(
          String.format("%s %s", mContext.getResources().getString(R.string.fa_bookmark_o),
              category));

      if (previewImage != null) {
        Picasso.with(mContext)
            .load(previewImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(target);
      }

      mCardView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          Intent intent = new Intent(mContext, DetailActivity.class);
          Feed mFeed = new Feed();
          mFeed.setTitle(title);
          mFeed.setImageUrl(previewImage);
          mFeed.setSummaryString(summeryBullte);
          mFeed.setCategory(category);
          mFeed.setOrginalUrl(decribeContent);

          intent.putExtra(String.valueOf(R.string.object_tag), mFeed);
          ActivityOptionsCompat options = ActivityOptionsCompat.
              makeSceneTransitionAnimation((Activity) mContext, (View) mImageView, "feedimage");
          mContext.startActivity(intent, options.toBundle());
        }
      });
    } else {
      final String title = mCategories.get(position).getTitle();
      final String previewImage = mCategories.get(position).getImageUrl();
      final String decribeContent = mCategories.get(position).getOriginalUrl();
      String timeSave = mCategories.get(position).getTimeSaved();
      final String category = mCategories.get(position).getCategories().get(0).getName();
      final String summeryBullte = mCategories.get(position).getSummeries().get(0);

      mText.setText(title);
      mTextOrginalUrl.setText(decribeContent);
      mTimeSaveView.setText(timeSave);
      mTextSlug.setText(
          String.format("%s %s", mContext.getResources().getString(R.string.fa_bookmark_o),
              category));

      if (previewImage != null) {
        Picasso.with(mContext)
            .load(previewImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(target);
      }

      mCardView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          Intent intent = new Intent(mContext, DetailActivity.class);
          Feed mFeed = new Feed();
          mFeed.setTitle(title);
          mFeed.setImageUrl(previewImage);
          mFeed.setSummaryString(summeryBullte);
          mFeed.setCategory(category);
          mFeed.setOrginalUrl(decribeContent);

          intent.putExtra(String.valueOf(R.string.object_tag), mFeed);
          ActivityOptionsCompat options = ActivityOptionsCompat.
              makeSceneTransitionAnimation((Activity) mContext, (View) mImageView, "feedimage");
          mContext.startActivity(intent, options.toBundle());
        }
      });
    }
  }

  @Override public int getItemCount() {
    return mCursor.getCount();
  }

  public void setCategory(ArrayList<Feed> category) {
    mCategories = category;
  }

  class ViewHolder extends BaseViewHolder {
    @Bind(R.id.rowText) TextAwesome mText;
    @Bind(R.id.urlcontent) TextAwesome mUrlText;
    @Bind(R.id.previewImage) ImageView mImageView;
    @Bind(R.id.mCardView) CardView mCardView;
    @Bind(R.id.timeSaved) TextAwesome mTimeSaved;
    @Bind(R.id.mCategory) TextAwesome mCategory;

    public ViewHolder(View itemView, LatestFeedAdapter adapter) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
      mAdapter = adapter;
    }
  }
}
