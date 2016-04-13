package indexer.com.polar.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by indexer on 10/31/15.
 */
public abstract class BaseAdapter<VH extends BaseAdapter.BaseViewHolder>
    extends RecyclerView.Adapter<VH> {

  private AdapterView.OnItemClickListener mOnItemClickListener;

  public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  private void onItemHolderClickListener(VH itemHolder) {
    if (mOnItemClickListener != null) {
      mOnItemClickListener.onItemClick(null, itemHolder.itemView, itemHolder.getLayoutPosition(),
          itemHolder.getItemId());
    }
  }

  public static class BaseViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {
    protected BaseAdapter<BaseViewHolder> mAdapter;

    public BaseViewHolder(View itemView) {
      super(itemView);
    }

    @Override public void onClick(View v) {
      mAdapter.onItemHolderClickListener(this);
    }
  }
}
