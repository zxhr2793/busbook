package com.ucas.busbook.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ucas.busbook.R;
import com.ucas.busbook.bean.Message;

public class MessageAdapter extends ListBaseAdapter<Message> {

	@SuppressLint("InflateParams")
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		//System.out.println("convertView == null?--->>>"+(convertView == null));
		if (convertView == null || convertView.getTag()== null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.message_list, null);
			holder = new viewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.message_title);
			holder.time = (TextView) convertView
					.findViewById(R.id.message_time);
			holder.content = (TextView) convertView
					.findViewById(R.id.message_content);
			holder.detail = (TextView) convertView
					.findViewById(R.id.message_details);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
			//System.out.println("falseholder == null?--->>>"+(holder == null));
		}
		//System.out.println("holder == null?--->>>"+(holder == null));
		Message messages = mDatas.get(position);
		//System.out.println(position +"title:"+messages.getMessageTitle());
		holder.title.setText(messages.getMessageTitle().toString());
		holder.time.setText(messages.getMessageTime());
		holder.content.setText(messages.getMessageContent());
		holder.detail.setText(R.string.message_details);
		return convertView;
	}

	public final class viewHolder {
		public TextView title;
		public TextView time;
		public TextView content;
		public TextView id;
		public TextView detail;
	}

}
