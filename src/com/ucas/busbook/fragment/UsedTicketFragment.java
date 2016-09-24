package com.ucas.busbook.fragment;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;

public class UsedTicketFragment extends BaseOrderFragment {
	public String getAction() {
		return Configs.ACTION_ORDER_LIST_HISTORY;
	}
	
	public CharSequence getContent(){
		return getResources().getString(R.string.used_warn);
	}
	
	public String getWarnContent(){
		return getResources().getString(R.string.without_used_ticket);
	}
}
