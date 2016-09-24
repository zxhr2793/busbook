package com.ucas.busbook.fragment;

import java.util.List;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.SetListViewHeight;
import com.ucas.busbook.adapter.OrderListAdapter;
import com.ucas.busbook.bean.Order;
import com.ucas.busbook.network.OrderList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BaseOrderFragment extends Fragment implements
		OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener,OnItemClickListener{
	public static final int STATE_NONE = 0;
	public static final int STATE_REFRESH = 1;
	public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
	public static int mState = STATE_NONE;

	private String student_id, token;
	private ListView listView;
	private OrderListAdapter listAdapter;
	private TextView usedOrderNoResult;
	private TextView warncontent;
	private List<Order> orderData;
	protected SwipeRefreshLayout mSwipeRefreshLayout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.order_detail_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = (ListView) getView().findViewById(R.id.used_ticket_order);
		usedOrderNoResult = (TextView) getView().findViewById(
				R.id.used_ticket_without_result);
		mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(
				R.id.swiperefreshlayout);
		warncontent = (TextView) getView().findViewById(R.id.warnning_content);
		initView(view);
	}

	private void initView(View view) {
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		warncontent.setText(getContent());
//		listView.setOnItemClickListener(this);		
//		listView.setOnItemLongClickListener(this);
	}

	private void loadOrder(String action) {
		student_id = getActivity().getIntent().getStringExtra(
				Configs.KEY_STUDENT_ID);
		token = getActivity().getIntent().getStringExtra(Configs.KEY_TOKEN);
		new OrderList(student_id, action, token,
				new OrderList.SuccessCallback() {
					@Override
					public void onSuccess(List<Order> orderInfo) {
						listView.setVisibility(View.VISIBLE);
						usedOrderNoResult.setVisibility(View.GONE);
						setOrderData(orderInfo);
						listAdapter = new OrderListAdapter(getActivity()
								.getApplicationContext(), orderInfo);
						listView.setAdapter(listAdapter);
						SetListViewHeight
								.setListViewHeightBasedOnChildren(listView);
						setSwipeRefreshLoadedState();
					}
				}, new FailCallback() {
					public void onFail(int errorCode) {
						switch (errorCode) {
						case Configs.RESULT_STATUS_NO_ORDER: {
							setNoDataView();
							setSwipeRefreshLoadedState();
						}
							break;

						default:
							setSwipeRefreshLoadedState();
							break;
						}
					}
				});
	}

	@Override
	public void onRefresh() {
		if (mState == STATE_REFRESH) {
			return;
		}
		// 设置顶部正在刷新
		setSwipeRefreshLoadingState();
		mState = STATE_REFRESH;
		requestData(true);
	}

	/** 设置顶部正在加载的状态 */
	protected void setSwipeRefreshLoadingState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(true);
			// 防止多次重复刷新
			mSwipeRefreshLayout.setEnabled(false);
		}
	}

	/** 设置顶部加载完毕的状态 */
	private void setSwipeRefreshLoadedState() {
		if (mSwipeRefreshLayout != null) {
			mSwipeRefreshLayout.setRefreshing(false);
			mSwipeRefreshLayout.setEnabled(true);
		}
		mState = STATE_NONE;
	}

	public CharSequence getContent() {
		return null;
	}

	public String getAction() {
		return null;
	}

	public String getWarnContent() {
		return null;
	}
	
	public ListView getListView(){
		return listView;
	}
	
	public OrderListAdapter getAdapter(){
		return listAdapter;
	}

	public List<Order> getOrderData() {
		return orderData;
	}

	public void setOrderData(List<Order> orderData) {
		this.orderData = orderData;
	}

	protected void requestData(boolean refresh) {
		if (refresh) {
			loadOrder(getAction());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {		
		return false;
	}

	@Override
	public void onStart() {
		super.onStart();
		onRefresh();
	}

	@Override
	public void onResume() {
		super.onResume();
//		onRefresh();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		System.out.println(position);
	}
	
	public void setNoDataView(){
		listView.setVisibility(View.GONE);
		usedOrderNoResult.setVisibility(View.VISIBLE);
		usedOrderNoResult.setText(getWarnContent());
	}
	
	public void setAdapter(OrderListAdapter adapter){
		listView.setAdapter(adapter);
	}
	

}
