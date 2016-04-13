package indexer.com.polar.base;

/**
 * Created by indexer on 10/31/15.
 */
public class Config {
  //API Key

  public static final String API_KEY = "JjlZcfAW8NMFWXSCIeiz";
  public static final String API_SECRET = "chrome-ext-prod";
  public static final String POCKET_KEY = "47261-6c144ddd43f45335386bdf77";
  public static final String POCKET_URI = "pocketapp47261:authorizationFinished";

  public static final String BASE_URL = "http://api.tldr.io";
  public static final String CATEGORIES = BASE_URL + "/categories";
  public static final String TLDR = BASE_URL + "/tldrs/latest";

  public static final String POCKET_BASE_URL = "https://getpocket.com/v3/oauth";
  public static final String POCKET_REQUEST_URL = POCKET_BASE_URL + "/request";
  public static final String POCKET_AUTHORIZE_URL = POCKET_BASE_URL + "/authorize";
}
