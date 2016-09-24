package com.ucas.busbook.Activity;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.CacheManager;
import com.ucas.busbook.adapter.ListBaseAdapter;
import com.ucas.busbook.adapter.MessageAdapter;
import com.ucas.busbook.bean.Message;
import com.ucas.busbook.network.HttpMethod;
import com.ucas.busbook.network.NetConnection;

public class NoticeCenterActivity extends Activity implements
		OnRefreshListener, OnItemClickListener, OnScrollListener {
	public static final int STATE_NONE = 0;
	public static final int STATE_REFRESH = 1;
	public static final int STATE_LOADMORE = 2;
	public static final int STATE_NOMORE = 3;
	public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
	public static int mState = STATE_NONE;
	private AsyncTask<String, Void, List<Message>> mCacheTask;
	private ParserTask mParserTask;

	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView listView;
	private TextView title;
	private ImageButton back;
	private MessageAdapter mAdapter;
	private final Context context = NoticeCenterActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_notice_center);
		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	private void initData() {
		mSwipeRefreshLayout.setOnRefreshListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mSwipeRefreshLayout.setColorSchemeResources(
				R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);

		if (mAdapter != null) {
			listView.setAdapter(mAdapter);
		} else {
			mAdapter = new MessageAdapter();
			listView.setAdapter(mAdapter);
		}
	}

	@SuppressLint("InflateParams")
	private void initView() {
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View headorfoot = inflater.inflate(R.layout.listview_header_footer,
//				null);
		title = (TextView) findViewById(R.id.title_name);
		back = (ImageButton) findViewById(R.id.left_btn);
		title.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
		title.setText(R.string.message_title);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.notice_swiperefreshlayout);
		listView = (ListView) findViewById(R.id.message_listview);
		//listView.addHeaderView(headorfoot);
		//listView.addFooterView(headorfoot);
	}

	@Override
	public void onRefresh() {
		if (mState == STATE_REFRESH) {
			return;
		}
		// 设置顶部正在刷新
		listView.setSelection(0);
		setSwipeRefreshLoadingState();
		// mCurrentPage = 0;
		mState = STATE_REFRESH;
		requestData(true);
	}

	protected List<Message> parseList(JSONArray jsonArray) throws Exception {
		return new Message().getMessages(jsonArray);
	}

	@SuppressWarnings("unchecked")
	protected List<Message> readList(Serializable seri) {
		return (List<Message>) seri;
	}

	private String getCacheKey() {
		return new StringBuilder("notice").toString();
	}

	private void requestData(boolean refresh) {
		String key = getCacheKey();
		if (isReadCacheData(refresh)) {
			 System.out.println("readCacheData");
			readCacheData(key);
		} else {
			// 取新的数据
			sendRequestData();
		}
	}

	private void sendRequestData() {
		//System.out.println("sendRequestData");
		new NetConnection(Configs.SERVER_URL + "/NoticeAction",
				HttpMethod.POST, new NetConnection.SuccessCallback() {
					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
							case Configs.RESULT_STATUS_SUCCESS:
								String lastest = jsonObject.getString("lastest");
								Configs.cacheMessageTime(context, lastest);
								JSONArray jsonArray = jsonObject
										.getJSONArray("message");
								executeParserTask(jsonArray);
								break;
							default:
								readCacheData(getCacheKey());
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						readCacheData(getCacheKey());
					}
				}, Configs.KEY_ACTION, Configs.ACTION_GET_NOTICE);
	}

	private void readCacheData(String key) {
		cancelReadCacheTask();
		mCacheTask = new CacheTask(context).execute(key);
	}

	private void cancelReadCacheTask() {
		if (mCacheTask != null) {
			mCacheTask.cancel(true);
			mCacheTask = null;
		}
	}

	private void executeParserTask(JSONArray jsonArray) {
		cancelParserTask();
		mParserTask = new ParserTask(jsonArray);
		mParserTask.execute();
	}

	private void cancelParserTask() {
		if (mParserTask != null) {
			mParserTask.cancel(true);
			mParserTask = null;
		}
	}

	private boolean isReadCacheData(boolean refresh) {
		String key = getCacheKey();
		if (!NetConnection.isNetWorkConnected(context)) {
			return true;
		}
		// 第一页若不是主动刷新，缓存存在，优先取缓存的
		if (CacheManager.isExistDataCache(context, key) && !refresh) {
			return true;
		}
//		if (CacheManager.isExistDataCache(context, key)
//				&& !CacheManager.isCacheDataFailure(context, key)) {
//			return true;
//		}
		return false;
	}

	private class CacheTask extends AsyncTask<String, Void, List<Message>> {
		private final WeakReference<Context> mContext;

		private CacheTask(Context context) {
			mContext = new WeakReference<Context>(context);
		}

		@Override
		protected List<Message> doInBackground(String... params) {
			Serializable seri = CacheManager.readObject(mContext.get(),
					params[0]);
			if (seri == null) {
				return null;
			} else {
				return readList(seri);
			}
		}

		@Override
		protected void onPostExecute(List<Message> list) {
			super.onPostExecute(list);
			if (list != null) {
				executeOnLoadDataSuccess(list);
			} else {
				executeOnLoadDataError(null);
			}
			executeOnLoadFinish();
		}
	}

	private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
		private final WeakReference<Context> mContext;
		private final Serializable seri;
		private final String key;

		private SaveCacheTask(Context context, List<Message> list, String key) {
			mContext = new WeakReference<Context>(context);
			this.seri = (Serializable) list;
			this.key = key;
		}

		@Override
		protected Void doInBackground(Void... params) {
			CacheManager.saveObject(mContext.get(), seri, key);
			return null;
		}
	}

	class ParserTask extends AsyncTask<Void, Void, String> {

		private final JSONArray jsonArray;
		private boolean parserError;
		private List<Message> list;

		public ParserTask(JSONArray jsonArray) {
			this.jsonArray = jsonArray;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				List<Message> data = parseList(jsonArray);
				new SaveCacheTask(context, data, getCacheKey()).execute();
				list = data;
			} catch (Exception e) {
				e.printStackTrace();
				parserError = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (parserError) {
				readCacheData(getCacheKey());
			} else {
				executeOnLoadDataSuccess(list);
				executeOnLoadFinish();
			}
		}
	}

	protected void executeOnLoadDataSuccess(List<Message> data) {
		if (data == null) {
			data = new ArrayList<Message>();
		}

		for (int i = 0; i < data.size(); i++) {
			if (compareTo(mAdapter.getData(), data.get(i))) {
				data.remove(i);
				i--;
			}
		}
		// System.out.println(data);

		int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
		if ((mAdapter.getCount() + data.size()) == 0) {
			adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
		} else if (data.size() == 0 || (data.size() < getPageSize())) {
			adapterState = ListBaseAdapter.STATE_NO_MORE;
			mAdapter.notifyDataSetChanged();
		} else {
			adapterState = ListBaseAdapter.STATE_LOAD_MORE;
		}
		mAdapter.setState(adapterState);
		mAdapter.addData(data);
		// 判断等于是因为最后有一项是listview的状态
		if (mAdapter.getCount() == 1) {

			if (needShowEmptyNoData()) {
				// mErrorLayout.setErrorType(EmptyLayout.NODATA);
			} else {
				mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	private boolean needShowEmptyNoData() {
		return true;
	}

	protected int getPageSize() {
		return 20;
	}

	private boolean compareTo(List<Message> data, Message message) {
		int s = data.size();
		if (message != null) {
			for (int i = 0; i < s; i++) {
				if (message.getMessageId().equals(data.get(i).getMessageId())) {
					return true;
				}
			}
		}
		return false;
	}

	protected void executeOnLoadDataError(String error) {
		mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
		mAdapter.notifyDataSetChanged();

	}

	// 完成刷新
	protected void executeOnLoadFinish() {
		setSwipeRefreshLoadedState();
		mState = STATE_NONE;
	}

	/** 设置顶部正在加载的状态 */
	private void setSwipeRefreshLoadingState() {
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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println(position);
		// Message message = (Message) parent.getAdapter().getItem(position);
		if (position == mAdapter.getCount()-1) {
			return;
		}
		Message message = mAdapter.getItem(position
				- listView.getHeaderViewsCount());
		Intent intent = new Intent(context, NoticeDetailActivity.class);
		intent.putExtra("title", message.getMessageTitle());
		intent.putExtra("time", message.getMessageTime());
		intent.putExtra("content", message.getMessageContent());
		context.startActivity(intent);
		// NewIntent.startNewActivity(context, NoticeDetailActivity.class);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (mAdapter == null || mAdapter.getCount() == 0) {
			return;
		}

		// 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
		if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
			return;
		}
		// 判断是否滚动到底部

		boolean scrollEnd = false;
		try {
			if (view.getPositionForView(mAdapter.getFooterView()) == view
					.getLastVisiblePosition())
				scrollEnd = true;
		} catch (Exception e) {
			scrollEnd = false;
		}

		if (mState == STATE_NONE && scrollEnd) {
			if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
					|| mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
				// mCurrentPage++;
				mState = STATE_LOADMORE;
				requestData(false);
				// System.out.println("STATE_LOADMORE");
				mAdapter.setFooterViewLoading();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	@Override
	protected void onStart() {
		if (Configs.hasLastest()) {
			requestData(true);
		}else {
			requestData(false);
		}
		
		super.onStart();
	}

	@Override
	public void onDestroy() {
		cancelReadCacheTask();
		cancelParserTask();
		super.onDestroy();
	}

}
