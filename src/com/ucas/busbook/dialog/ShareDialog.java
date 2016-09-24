package com.ucas.busbook.dialog;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.widget.DeviceInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class ShareDialog extends Dialog implements android.view.View.OnClickListener {
	private Context context;
	private String link;
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	public ShareDialog(Context context) {
		super(context);
		this.context = context;
	}

	public ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);

	}

	public ShareDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View shareView = getLayoutInflater().inflate(R.layout.dialog_share, null);
		shareView.findViewById(R.id.ly_share_qq).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_qqzone).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_qqwb).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_renren).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_copy_link).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_more_option).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_sina_weibo).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_weichat).setOnClickListener(this);
		shareView.findViewById(R.id.ly_share_weichat_circle).setOnClickListener(this);
		shareView.findViewById(R.id.share_cancel_btn).setOnClickListener(this);
		setContentView(shareView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_share_weichat_circle:
			shareToWeiChatCircle();
			break;
		case R.id.ly_share_weichat:
			shareToWeiChat();
			break;
		case R.id.ly_share_sina_weibo:
			shareToSinaWeibo();
			break;
		case R.id.ly_share_qq:
			shareToQQ();
			break;
		case R.id.ly_share_qqzone:
			shareToQQZone();
			break;
		case R.id.ly_share_qqwb:
			shareToQQWb();
			break;
		case R.id.ly_share_renren:
			shareToRenRen();
			break;
		case R.id.ly_share_copy_link:
			DeviceInfo.copyTextToBoard(context, "剪切板内容");
			break;
		case R.id.ly_share_more_option:
			DeviceInfo.showSystemShareOption((Activity) this.context, "", "");
			break;
		default:
			break;
		}
		this.dismiss();
	}

	private void shareToWeiChatCircle() {
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this.context, Configs.WEIXIN_APP_ID, Configs.WEIXIN_APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("这是微信朋友圈分享的测试内容");
		// 设置朋友圈title
		circleMedia.setTitle("这是微信朋友圈分享的测试标题");

		circleMedia.setTargetUrl("这是微信朋友圈分享的测试链接http://www.baidu.com");
		mController.setShareMedia(circleMedia);
		mController.postShare(this.context, SHARE_MEDIA.WEIXIN_CIRCLE, null);
	}

	private void shareToWeiChat() {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this.context, Configs.WEIXIN_APP_ID, Configs.WEIXIN_APP_SECRET);
		wxHandler.addToSocialSDK();
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent("这是微信好友分享的测试内容");
		// 设置title
		weixinContent.setTitle("这是微信好友分享的测试标题");
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl("这是微信好友分享的测试链接");
		// 设置分享图片

		mController.setShareMedia(weixinContent);
		mController.directShare(this.context, SHARE_MEDIA.WEIXIN, null);
	}

	private void shareToSinaWeibo() {
		// 设置新浪微博SSO handler
		SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
		sinaSsoHandler.setTargetUrl(this.link);
		mController.setShareType(ShareType.SHAKE);
		mController.setShareContent("新浪微博分享测试内容");

		mController.getConfig().setSsoHandler(sinaSsoHandler);

		if (OauthHelper.isAuthenticated(this.context, SHARE_MEDIA.SINA)) {
			mController.directShare(this.context, SHARE_MEDIA.SINA, null);
		} else {
			mController.doOauthVerify(this.context, SHARE_MEDIA.SINA, new SocializeListeners.UMAuthListener() {

				@Override
				public void onStart(SHARE_MEDIA arg0) {
				}

				@Override
				public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
				}

				@Override
				public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
					mController.directShare(ShareDialog.this.context, SHARE_MEDIA.SINA, null);
				}

				@Override
				public void onCancel(SHARE_MEDIA arg0) {
				}
			});
		}
	}

	private void shareToQQ() {
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) this.context, Configs.QQ_APP_ID,
				Configs.QQ_APP_KEY);
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("这是QQ好友分享功能的测试内容");
        qqShareContent.setTitle("这是QQ好友分享功能的测试标题");
        
        qqShareContent.setTargetUrl(" ");
        mController.setShareMedia(qqShareContent);
		mController.postShare(this.context, SHARE_MEDIA.QQ, null);
	}

	private void shareToQQZone() {
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) this.context, Configs.QQ_APP_ID,
				Configs.QQ_APP_KEY);
		qZoneSsoHandler.addToSocialSDK();
		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("这是QQ空间分享的测试内容");
		qzone.setTargetUrl("http://www.baidu.com");
		qzone.setTitle("QZone title");

		// qzone.setShareMedia(uMusic);
		mController.setShareMedia(qzone);
		mController.postShare(this.context, SHARE_MEDIA.QZONE, null);
	}

	private void shareToQQWb() {
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-腾讯微博。http://www.umeng.com/social");
		// 设置tencent分享内容
		mController.setShareMedia(tencent);
		mController.postShare(this.context, SHARE_MEDIA.TENCENT, null);
	}

	private void shareToRenRen() {
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler((Activity) this.context, "477468",
				"a3b08f2ff190447ca0c67c5a1240a62d", "5cad57c6265c40109c7a181655e68ba1");
		mController.getConfig().setSsoHandler(renrenSsoHandler);
		// 设置renren分享内容
		RenrenShareContent renrenShareContent = new RenrenShareContent();
		renrenShareContent.setShareContent("我正在使用班车预约系统，你也来试试吧！");
		renrenShareContent.setAppWebSite("http://www.umeng.com/social");
		mController.setShareMedia(renrenShareContent);
		mController.postShare(this.context, SHARE_MEDIA.RENREN, null);
	}

}
