package indexer.com.polar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by indexer on 10/31/15.
 */
public class Category implements Parcelable {
  public static final Creator<Category> CREATOR = new Creator<Category>() {
    @Override public Category createFromParcel(Parcel in) {
      return new Category(in);
    }

    @Override public Category[] newArray(int size) {
      return new Category[size];
    }
  };
  String _id;
  String name;
  String slug;
  String type;

  protected Category(Parcel in) {
    _id = in.readString();
    name = in.readString();
    slug = in.readString();
    type = in.readString();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(_id);
    dest.writeString(name);
    dest.writeString(slug);
    dest.writeString(type);
  }
}
