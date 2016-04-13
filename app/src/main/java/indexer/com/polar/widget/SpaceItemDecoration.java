package indexer.com.polar.widget;

/**
 * Created by indexer on 10/31/15.
 */

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by indexer on 9/20/15.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

  private final int halfSpace;

  public SpaceItemDecoration(int space) {
    this.halfSpace = space / 2;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {

    if (parent.getPaddingLeft() != halfSpace) {
      parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
      parent.setClipToPadding(false);
    }

    outRect.top = halfSpace;
    outRect.bottom = halfSpace;
    outRect.left = halfSpace;
    outRect.right = halfSpace;
  }
}
