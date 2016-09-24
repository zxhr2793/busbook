package com.ucas.busbook.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ucas.busbook.R;
import com.ucas.busbook.bean.Order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	public List<Order> listItems;
	private boolean[] hasChecked;
	public HashMap<Integer, Integer> visiblecheck;// 用来记录是否显示checkBox
	public HashMap<Integer, Boolean> ischeck;
	public HashMap<Integer, String> checked;
	public HashMap<Integer, Object> checkedItem;
	public boolean isEditing = false;

	public OrderListAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@SuppressLint("UseSparseArrays")
	public OrderListAdapter(Context context, List<Order> listItems) {
		this.mInflater = LayoutInflater.from(context);
		this.listItems = listItems;
		this.context = context;
		visiblecheck = new HashMap<Integer, Integer>();
		ischeck = new HashMap<Integer, Boolean>();
		checked = new HashMap<Integer, String>();
		checkedItem = new HashMap<Integer, Object>();
		for (int i = 0; i < listItems.size(); i++) {
			visiblecheck.put(i, View.GONE);
			ischeck.put(i, false);
		}
		hasChecked = new boolean[getCount()];
	}

	private void checkedChange(int selectedId) {
		hasChecked[selectedId] = !hasChecked[selectedId];
	}

	public boolean hasChecked(int checkedId) {
		return hasChecked[checkedId];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int selectedId = position;
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.order_list_info, null);
			holder = new ViewHolder();
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.order_checked);
			holder.bookedSource = (TextView) convertView.findViewById(R.id.booked_source);
			holder.bookedDesti = (TextView) convertView.findViewById(R.id.booked_desti);
			holder.takeOffDate = (TextView) convertView.findViewById(R.id.takeoff_date);
			holder.takeOffWeekDay = (TextView) convertView.findViewById(R.id.takeoff_weekday);
			holder.takeOffTime = (TextView) convertView.findViewById(R.id.takeoff_time);
			holder.ticketState = (TextView) convertView.findViewById(R.id.booked_order_state);
			holder.cancelTicketBtn = (Button) convertView.findViewById(R.id.booked_ticket_cancel_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Order order = listItems.get(position);
		holder.bookedSource.setText(order.getSource().toString());
		holder.bookedDesti.setText(order.getDesti().toString());
		holder.takeOffDate.setText(order.getTakeOffDate().toString());
		holder.takeOffWeekDay.setText(order.getTakeOffWeekDay().toString());
		holder.takeOffTime.setText(order.getTakeOffTime().toString());
		holder.ticketState.setText(order.getTicketState().toString());
		holder.checkBox.setVisibility(visiblecheck.get(position));
		holder.checkBox.setChecked(ischeck.get(position));
		// checked.put(position, order.getId());
		// holder.checkBox
		// .setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// checkedChange(selectedId);
		//// Toast.makeText(context, "position is checked?"+isChecked,
		// Toast.LENGTH_LONG).show();
		// }
		//
		// });
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isEditing && holder.checkBox.getVisibility() == 0) {
					boolean isChecked = holder.checkBox.isChecked();
					holder.checkBox.setChecked(!isChecked);
					ischeck.put(selectedId, !isChecked);
					notifyDataSetChanged();
					if (isChecked) {
						checked.remove(selectedId);
						checkedItem.remove(selectedId);
					} else {
						checked.put(selectedId, order.getId());
						checkedItem.put(selectedId, order);
					}
					// System.out.println(isChecked);
				}
			}
		});

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				return false;
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		public CheckBox checkBox;
		public TextView bookedSource;
		public TextView bookedDesti;
		public TextView takeOffDate;
		public TextView takeOffWeekDay;
		public TextView takeOffTime;
		public TextView ticketState;
		public Button cancelTicketBtn;

	}

	public List<Order> getData() {
		return listItems == null ? (listItems = new ArrayList<Order>()) : listItems;
	}

}
