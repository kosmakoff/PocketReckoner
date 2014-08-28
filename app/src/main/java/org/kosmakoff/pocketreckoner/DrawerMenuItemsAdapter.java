package org.kosmakoff.pocketreckoner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by okosmakov on 30.07.13.
 */
public class DrawerMenuItemsAdapter extends BaseAdapter {

	public Context getContext() {
		return mContext;
	}

	private DrawerMenuItem[] mItems;

	private Context mContext;

	public DrawerMenuItemsAdapter(Context context, DrawerMenuItem[] items) {
		mContext = context;
		mItems = items;
	}

	@Override
	public int getCount() {
		return mItems.length;
	}

	@Override
	public Object getItem(int i) {
		return mItems[i];
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		if (convertView != null) {
			view = convertView;
		} else {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.drawer_list_item, parent, false);
		}

		DrawerMenuItem menuItem = (DrawerMenuItem) getItem(position);

		TextView tv = (TextView) view.findViewById(R.id.drawer_list_item_caption);
		tv.setText(menuItem.getTitle());

		ImageView iv = (ImageView) view.findViewById(R.id.drawer_list_item_image);
		iv.setImageResource(menuItem.getDrawableId());

		return view;
	}
}
