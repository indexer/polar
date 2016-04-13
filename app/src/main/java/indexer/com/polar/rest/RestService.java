package indexer.com.polar.rest;

import indexer.com.polar.base.Config;
import indexer.com.polar.model.Category;
import indexer.com.polar.model.Feed;
import java.util.ArrayList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by indexer on 10/31/15.
 */
public interface RestService {
  @GET(Config.CATEGORIES) Call<ArrayList<Category>> getAllcategories();

  @GET(Config.TLDR) Call<ArrayList<Feed>> getLatestFeeds();

  @GET(Config.TLDR) Call<ArrayList<Feed>> getFeedListByCategory(@Query("number") int number,
      @Query("category") String category);

  // @FormUrlEncoded @POST(Config.POCKET_REQUEST_URL) Call<ReplyCode> getPocketCode();
}
