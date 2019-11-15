
# RecyclerView androidX

### layout
```
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
        android:padding="10dp"
        android:gravity="start"
        android:text="Result text"
        android:textSize="10sp">

    </androidx.recyclerview.widget.RecyclerView>
```

### RecyclerViewAdapter + ViewHolder + Item
```
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> items;

    public RecyclerViewAdapter(@NonNull Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_dialog_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.tvMessage.setText(item.getMessage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(int position) {

        items.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void pushItem(Item item) {
        int position = items.size();
        items.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void restoreItem(Item item, int position) {
        items.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_item);
        }
    }

    public static class Item {
        public Item(String message) {
            this.message = message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        String message;
    }
}
```

### Init
```
        List<RecyclerViewAdapter.Item> listItems = new ArrayList<>();

        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), listItems);

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAlertListAdapter);

        mRecyclerViewAdapter.notifyDataSetChanged();
```

