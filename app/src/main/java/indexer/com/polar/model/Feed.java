package indexer.com.polar.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Feed implements Parcelable {

  public static final Creator<Feed> CREATOR = new Creator<Feed>() {
    @Override public Feed createFromParcel(Parcel in) {
      return new Feed(in);
    }

    @Override public Feed[] newArray(int size) {
      return new Feed[size];
    }
  };
  private String _id;
  private String originalUrl;
  private String title;
  private String mCategory;
  private List<Category> categories = new ArrayList<>();
  private String imageUrl;
  private String timeSaved;
  private ArrayList<String> summaryBullets = new ArrayList<>();
  private String summaryString;

  public Feed() {

  }

  private Feed(Parcel in) {
    _id = in.readString();
    originalUrl = in.readString();
    title = in.readString();
    imageUrl = in.readString();
    timeSaved = in.readString();
    mCategory = in.readString();
    summaryString = in.readString();
  }

  public String getSummaryString() {
    return summaryString;
  }

  public void setSummaryString(String summaryString) {
    this.summaryString = summaryString;
  }

  public String getCategory() {
    return mCategory;
  }

  public void setCategory(String category) {
    mCategory = category;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getTimeSaved() {
    return timeSaved;
  }

  public void setTimeSaved(String timeSaved) {
    this.timeSaved = timeSaved;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOrginalUrl(String orginalUrl) {
    this.originalUrl = orginalUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ArrayList<String> getSummeries() {
    return summaryBullets;
  }

  public void setSummeries(ArrayList<String> summeries) {
    summaryBullets = summeries;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(_id);
    parcel.writeString(originalUrl);
    parcel.writeString(title);
    parcel.writeString(imageUrl);
    parcel.writeString(timeSaved);
    parcel.writeString(mCategory);
    parcel.writeString(summaryString);
  }
}
