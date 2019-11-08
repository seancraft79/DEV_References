
# RecyclerView

### Layout RecyclerView
```
    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="none"/>

```

### Layout Item
```
	<?xml version="1.0" encoding="utf-8"?>
	<FrameLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:minHeight="35dp"
	    android:paddingTop="5dp"
	    android:layout_gravity="center">

	    <ImageView ...

	    <TextView ...

	</FrameLayout>
```

### Adapter
```
	public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.AlertViewHolder> {

	    private Context context;
	    private List<Item> items;

	    public AlertListAdapter(Context context, List<Item> items) {
		this.context = context;
		this.items = items;
	    }

	    @NonNull
	    @Override
	    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

		View itemView = LayoutInflater.from(viewGroup.getContext())
			.inflate(R.layout.item_layout, viewGroup, false);
		return new AlertViewHolder(itemView);
	    }

	    @Override
	    public void onBindViewHolder(@NonNull AlertViewHolder alertViewHolder, int i) {

		final Item item = items.get(i);
		
		// Do something else with alertViewHolder
	    }

	    @Override
	    public int getItemCount() {
		return items.size();
	    }

	    public void removeItem(int position) {

		Log.d(AlertRecyclerView.TAG, "removeItem: " + position);

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

	    static class AlertViewHolder extends RecyclerView.ViewHolder {

		ImageView icon;
		TextView alertType, alertMessage;
		RelativeLayout viewBackground, viewForeground;

		public AlertViewHolder(@NonNull View itemView) {
		    super(itemView);

		    icon = itemView.findViewById(R.id.alert_icon);
		    alertType = itemView.findViewById(R.id.log_type);
		    alertMessage = itemView.findViewById(R.id.log_message);
		    viewBackground = itemView.findViewById(R.id.view_background);
		    viewForeground = itemView.findViewById(R.id.view_foreground);
		}
	    }
	}
```

### ViewHolder
```
	public class AlertViewHolder extends RecyclerView.ViewHolder {

		ImageView icon;
		TextView alertType, alertMessage;
		RelativeLayout viewBackground, viewForeground;

		public AlertViewHolder(@NonNull View itemView) {
		    super(itemView);

		    icon = itemView.findViewById(R.id.alert_icon);
		    alertType = itemView.findViewById(R.id.log_type);
		    alertMessage = itemView.findViewById(R.id.log_message);
		    viewBackground = itemView.findViewById(R.id.view_background);
		    viewForeground = itemView.findViewById(R.id.view_foreground);
		}
	    }
```

### ItemTouchHelper
```
	package com.metarobotics.playvandi.AlertRecycler;

	import android.graphics.Canvas;
	import android.support.v7.widget.RecyclerView;
	import android.support.v7.widget.helper.ItemTouchHelper;
	import android.view.View;

	public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
	    private RecyclerItemTouchHelperListener listener;

	    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
		super(dragDirs, swipeDirs);
		this.listener = listener;
	    }

	    @Override
	    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
		return true;
	    }

	    @Override
	    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
		if (viewHolder != null) {
		    final View foregroundView = ((AlertListAdapter.AlertViewHolder) viewHolder).viewForeground;

		    getDefaultUIUtil().onSelected(foregroundView);
		}
	    }

	    @Override
	    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
					RecyclerView.ViewHolder viewHolder, float dX, float dY,
					int actionState, boolean isCurrentlyActive) {
		final View foregroundView = ((AlertListAdapter.AlertViewHolder) viewHolder).viewForeground;
		getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
			actionState, isCurrentlyActive);
	    }

	    @Override
	    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		final View foregroundView = ((AlertListAdapter.AlertViewHolder) viewHolder).viewForeground;
		getDefaultUIUtil().clearView(foregroundView);
	    }

	    @Override
	    public void onChildDraw(Canvas c, RecyclerView recyclerView,
				    RecyclerView.ViewHolder viewHolder, float dX, float dY,
				    int actionState, boolean isCurrentlyActive) {
		final View foregroundView = ((AlertListAdapter.AlertViewHolder) viewHolder).viewForeground;

		getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
			actionState, isCurrentlyActive);
	    }

	    @Override
	    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
	    }

	    @Override
	    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
		return super.convertToAbsoluteDirection(flags, layoutDirection);
	    }

	    public interface RecyclerItemTouchHelperListener {
		void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
	    }
	}

```

### Init recyclerview
```
		List<Item> itemList = new ArrayList<>();
		mAlertAdapter = new AlertListAdapter(context, itemList);

		RecyclerView recyclerView = findViewById(R.id.recycler_view);

		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		// recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
		recyclerView.setAdapter(mAlertAdapter);

		// add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
		ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
		new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

		itemList.clear();

		// refreshing recycler view
		mAlertAdapter.notifyDataSetChanged();
```