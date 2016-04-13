package indexer.com.polar.rest;

import android.content.Context;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import indexer.com.polar.base.Config;
import java.io.IOException;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by indexer on 10/31/15.
 */
public class PocketRestClient {

  private static PocketRestClient instance;
  private RestService mService;

  private PocketRestClient(Context context) {

    OkHttpClient client = new OkHttpClient();
    client.interceptors().add(new LoggingInterceptor());

    final Retrofit restAdapter = new Retrofit.Builder().baseUrl(Config.POCKET_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();

    mService = restAdapter.create(RestService.class);
  }

  public static synchronized RestService getService(Context context) {
    return getInstance(context).mService;
  }

  private static PocketRestClient getInstance(Context context) {
    if (instance == null) {
      instance = new PocketRestClient(context);
    }
    return instance;
  }

  private class LoggingInterceptor implements Interceptor {
    private final String TAG = LoggingInterceptor.class.getSimpleName();

    @Override public Response intercept(Chain chain) throws IOException {

      Request request = chain.request();
      Request.Builder builder = request.newBuilder();
      builder.header("X-Accept", "application/json");
      request = builder.build();
      return chain.proceed(request);
    }
  }
}
