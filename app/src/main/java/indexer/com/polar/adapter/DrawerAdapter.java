package indexer.com.polar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import indexer.com.polar.R;
import indexer.com.polar.activity.MainActivity;
import indexer.com.polar.base.BaseAdapter;
import indexer.com.polar.listener.DataTransferInterface;
import indexer.com.polar.model.Category;
import java.util.ArrayList;

/**
 * Created by indexer on 10/31/15.
 */
public class DrawerAdapter extends BaseAdapter<BaseAdapter.BaseViewHolder> {
  Context mContext;
  ArrayList<Category> mCategories;
  DataTransferInterface dtInterface;

  public DrawerAdapter(Context mContext, MainActivity dataTransferInterface) {
    mCategories = new ArrayList<>();
    dtInterface = dataTransferInterface;
  }

  public void setCategory(ArrayList<Category> candidates) {
    this.mCategories = candidates;
    notifyDataSetChanged();
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    mContext = parent.getContext();
    View itemView = inflater.inflate(R.layout.item_row, parent, false);
    return new ViewHolder(itemView, this);
  }

  @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
    ViewHolder myHolder = ((ViewHolder) holder);
    String category = mCategories.get(position).getName();
    myHolder.mText.setText(category);
  }

  @Override public int getItemCount() {
    return mCategories.size();
  }

  class ViewHolder extends BaseViewHolder {
    @Bind(R.id.rowText) TextView mText;

    public ViewHolder(final View itemView, final DrawerAdapter adapter) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
      mAdapter = adapter;

      setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         /* Polar.get()
              .trackEvent("Category seeing", mCategories.get(i).getName(), "Category for feed");*/
          dtInterface.setValues(mCategories.get(i).getName());
        }
      });
    }
  }
}
