package indexer.com.polar.base;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import indexer.com.polar.R;

/**
 * Created by indexer on 10/31/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResource());

    if (needToolbar()) { //flag to check activity needs toolbar or not
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

      if (toolbar != null) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null && getHomeUpEnabled()) {
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          getSupportActionBar().setHomeAsUpIndicator(toolBarIndicator());
          getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
      }
    }
    FloatingActionButton floatingSearchButton =
        (FloatingActionButton) findViewById(R.id.searchFabButton);
    if (needFabButton()) { //flag to check activity needs Fab button or not
      if (floatingSearchButton != null) {
        floatingSearchButton.setVisibility(View.VISIBLE);
      }
    } else {
      if (floatingSearchButton != null) {
        floatingSearchButton.setVisibility(View.INVISIBLE);
      }
    }
  }

  protected abstract boolean needFabButton();

  protected abstract int getLayoutResource();

  protected abstract boolean getHomeUpEnabled();

  protected abstract boolean needToolbar();

  protected abstract int toolBarIndicator();
}
