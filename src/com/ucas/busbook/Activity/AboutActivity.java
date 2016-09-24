package com.ucas.busbook.Activity;

import com.ucas.busbook.R;
import com.ucas.busbook.dialog.ShareDialog;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener{
	private TextView title;
	private ImageButton back;
	private UMSocialService mController;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		initView();
		initUmeng();
	}

	private void initUmeng() {
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		configPlatforms();
		setShareContent();
		
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_name);
		back = (ImageButton) findViewById(R.id.left_btn);
		title.setText(getResources().getString(R.string.about));
		back.setVisibility(View.VISIBLE);
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				NewIntent.startNewActivity(AboutActivity.this,
//						UsercenterActivity.class);
//
//				AboutActivity.this.finish();
				onBackPressed();
			}
		});
		findViewById(R.id.share).setOnClickListener(this);
	}	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share:
//			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
//                    SHARE_MEDIA.DOUBAN,
//                    SHARE_MEDIA.RENREN);
//			mController.openShare(AboutActivity.this, false);
			ShareDialog dialog = new ShareDialog(AboutActivity.this, R.style.customDialog);
			dialog.show();
			break;

		default:
			break;
		}
	
	}
	
	/**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加人人网SSO授权
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(AboutActivity.this,
        		"477468", "a3b08f2ff190447ca0c67c5a1240a62d",
                "5cad57c6265c40109c7a181655e68ba1");
        mController.getConfig().setSsoHandler(renrenSsoHandler);

        // 添加QQ、QZone平台
        addQQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }
    
    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(AboutActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能。http://www.umeng.com/social");

        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(AboutActivity.this,
                "477468", "a3b08f2ff190447ca0c67c5a1240a62d",
                "5cad57c6265c40109c7a181655e68ba1");
        mController.getConfig().setSsoHandler(renrenSsoHandler);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
        weixinContent.setTitle("友盟社会化分享组件-微信");
        weixinContent.setTargetUrl("http://www.umeng.com/social");
        
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia
                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
        circleMedia.setTargetUrl("http://www.umeng.com/social");
        mController.setShareMedia(circleMedia);

        // 设置renren分享内容
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent("我正在使用班车预约系统，你也来试试吧！");
        renrenShareContent.setAppWebSite("http://www.umeng.com/social");
        mController.setShareMedia(renrenShareContent);
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent("share test");
        qzone.setTargetUrl("http://www.umeng.com");
        qzone.setTitle("QZone title");
       
        // qzone.setShareMedia(uMusic);
        mController.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
        qqShareContent.setTitle("hello, title");
        
        qqShareContent.setTargetUrl("http://www.umeng.com/social");
        mController.setShareMedia(qqShareContent);

        TencentWbShareContent tencent = new TencentWbShareContent();
        tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-腾讯微博。http://www.umeng.com/social");
        // 设置tencent分享内容
        mController.setShareMedia(tencent);

        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-短信。http://www.umeng.com/social");
        // sms.setShareImage(urlImage);
        mController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent
                .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-新浪微博。http://www.umeng.com/social");
        mController.setShareMedia(sinaContent);

    }
    
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx967daebe835fbeac";
        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(AboutActivity.this, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(AboutActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(AboutActivity.this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(AboutActivity.this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			NewIntent.startNewActivity(AboutActivity.this,
//					UsercenterActivity.class);
//
//			AboutActivity.this.finish();
//			return true;
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
//	}
}
