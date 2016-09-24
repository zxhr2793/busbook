package com.ucas.busbook.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.adapter.OrderListAdapter;
import com.ucas.busbook.adapter.OrderListAdapter.ViewHolder;
import com.ucas.busbook.bean.Order;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.CommonDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.CancelOrder;
import com.ucas.busbook.network.CancelOrder.FailCallback;
import com.ucas.busbook.network.CancelOrder.SuccessCallback;
import com.ucas.busbook.network.HttpMethod;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UnUsedTicketFragment extends BaseOrderFragment implements OnClickListener, OnCheckedChangeListener {
	private RelativeLayout relativeLayout;
	private boolean selectAll = false;
	Button select_all;

	// private boolean isEditing = false;

	public String getAction() {
		return Configs.ACTION_ORDER_LIST;
	}

	public CharSequence getContent() {
		return getResources().getString(R.string.unused_warn);
	}

	public String getWarnContent() {
		return getResources().getString(R.string.without_unused_ticket);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		OrderListAdapter adapter = getAdapter();
		for (int i = 0; i < adapter.getData().size(); i++) {
			adapter.visiblecheck.put(i, View.VISIBLE);
		}
		adapter.isEditing = true;
		selectAll = false;
		getListView().setAdapter(adapter);
		getRelativeLayout().setVisibility(View.VISIBLE);
		// getRelativeLayout().findViewById(R.id.select_all_order);
		// Button select_btn = (Button)
		// getRelativeLayout().findViewById(R.id.select_all_order);
		select_all = (Button) getRelativeLayout().findViewById(R.id.select_all_order);
		select_all.setOnClickListener(this);
		select_all.setText("全选");
		getRelativeLayout().findViewById(R.id.cancel_select).setOnClickListener(this);
		getRelativeLayout().findViewById(R.id.delete_selected_order).setOnClickListener(this);
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.checkBox.setOnCheckedChangeListener(this);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		// Toast.makeText(getActivity().getApplicationContext(), "position
		// ischecked?"+position, Toast.LENGTH_LONG).show();
	}

	public RelativeLayout getRelativeLayout() {
		return relativeLayout;
	}

	public void setRelativeLayout(RelativeLayout relativeLayout) {
		this.relativeLayout = relativeLayout;
	}

	@Override
	public void onClick(View v) {
		OrderListAdapter adapter = getAdapter();
		// getListView().setAdapter(adapter);
		// OrderListAdapter adapter = new
		// OrderListAdapter(getActivity().getApplicationContext(),
		// getOrderData());
		switch (v.getId()) {
		case R.id.select_all_order: {
			if (!selectAll) {
				for (int i = 0; i < adapter.getData().size(); i++) {
					adapter.visiblecheck.put(i, View.VISIBLE);
					adapter.ischeck.put(i, true);
					adapter.checked.put(i, adapter.getData().get(i).getId());
					adapter.checkedItem.put(i, adapter.getData().get(i));
				}
				adapter.isEditing = true;
				adapter.notifyDataSetChanged();
				// getListView().setAdapter(adapter);
				select_all.setText("全不选");
				selectAll = true;
				// System.out.println(adapter.ischeck);
			} else {
				// OrderListAdapter adapter = new
				// OrderListAdapter(getActivity().getApplicationContext(),getOrderData());
				for (int i = 0; i < adapter.getData().size(); i++) {
					adapter.visiblecheck.put(i, View.VISIBLE);
					adapter.ischeck.put(i, false);
					adapter.checked.remove(i);
					adapter.checkedItem.remove(i);
				}
				adapter.isEditing = true;
				adapter.notifyDataSetChanged();
				// getListView().setAdapter(adapter);
				select_all.setText("全选");
				selectAll = false;
				// System.out.println(adapter.ischeck);
			}
			// adapter.notifyDataSetChanged();

		}

			break;
		case R.id.cancel_select: {
			// OrderListAdapter adapter = new
			// OrderListAdapter(getActivity().getApplicationContext(),getOrderData());
			for (int i = 0; i < adapter.getData().size(); i++) {
				adapter.visiblecheck.put(i, View.GONE);
				adapter.ischeck.put(i, false);
			}
			adapter.isEditing = false;
			adapter.checked.clear();
			adapter.checkedItem.clear();
			adapter.notifyDataSetChanged();
			// getListView().setAdapter(adapter);
			getRelativeLayout().setVisibility(View.GONE);
		}
			break;
		case R.id.delete_selected_order: {
			// System.out.println(adapter.ischeck);
			// System.out.println(adapter.checked);
			if (!adapter.ischeck.containsValue(true)) {
				Toast.makeText(getActivity().getApplicationContext(), "至少选择一项", Toast.LENGTH_LONG).show();
				return;
			}
			deleteChecked();
		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		onRefresh();
		getRelativeLayout().setVisibility(View.GONE);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// System.out.println("ischecked" + isChecked);
	}
	
	public void onRefresh() {
		super.onRefresh();
		getRelativeLayout().setVisibility(View.GONE);
	}

	public void deleteChecked() {

		final CommonDialog dialog = new CommonDialog(getActivity(), R.style.customDialog);
		dialog.show();
		dialog.setMessage("你确定要退订所选的车票吗？");
		dialog.setPositiveButton(getResources().getString(R.string.dialog_ok_btn), new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				sendToServer();
			}
		});
		dialog.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public void sendToServer() {
		final OrderListAdapter adapter = getAdapter();
		List<Order> list = new ArrayList<Order>();
		for (int set:adapter.checkedItem.keySet()) {
			Order order = (Order) adapter.checkedItem.get(set);
			list.add(order);
		}
//		System.out.println(list);

		final LoadingDialog dialog = APPDialog.showLoadingDialog(getActivity(),
				getResources().getString(R.string.deleting));

		new CancelOrder(Configs.SERVER_URL + "/CancelAction", HttpMethod.POST, new SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					switch (jsonObject.getInt(Configs.KEY_STATUS)) {
					case Configs.RESULT_STATUS_SUCCESS: {
						dialog.dismiss();
						for (int set : adapter.checked.keySet()) {
							adapter.getData().remove(adapter.checkedItem.get(set));
							adapter.ischeck.put(set, false);
						}
						if (adapter.getData().isEmpty()) {
							setNoDataView();
							getRelativeLayout().setVisibility(View.GONE);
							return;
						}
						adapter.checked.clear();
						adapter.checkedItem.clear();
						adapter.notifyDataSetChanged();
						setAdapter(adapter);
						getListView().invalidate();
					}
						break;

					default:
						dialog.dismiss();
						ErrorUtils.networkOrActionError(jsonObject.getInt(Configs.KEY_STATUS), getActivity());
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new FailCallback() {

			@Override
			public void onFail(int errorCode) {
				dialog.dismiss();
				ErrorUtils.networkOrActionError(errorCode, getActivity());
			}
		}, list);

	}

}
