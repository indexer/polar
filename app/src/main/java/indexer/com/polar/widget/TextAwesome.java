package indexer.com.polar.widget;

/**
 * Created by indexer on 10/31/15.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextAwesome extends TextView {

  private final static String NAME = "FONTAWESOME";
  private static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);

  public TextAwesome(Context context) {
    super(context);
    init();
  }

  public TextAwesome(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {

    Typeface typeface = sTypefaceCache.get(NAME);

    if (typeface == null) {

      typeface = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
      sTypefaceCache.put(NAME, typeface);
    }

    setTypeface(typeface);
  }
}


