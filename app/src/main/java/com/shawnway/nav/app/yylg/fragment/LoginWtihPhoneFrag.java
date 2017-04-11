package com.shawnway.nav.app.yylg.fragment;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;

import java.util.Calendar;

public class LoginWtihPhoneFrag extends Fragment implements OnClickListener{
	private static final String TAG = "LoginWithPhoneFrag";
	private long sendTime;
	String lastphone;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_login_with_phone, container, false);
		setListener(view);
		return view;
	}
	
	
	private void resend(Button v) {
		if (Constants.dummy) {
			sendTime=Calendar.getInstance().getTimeInMillis();
			setCountDown(sendTime, v);
		}
	}


	private void summitValCode() {
		if (Constants.dummy) {
			getActivity().finish();
		}
		ToastUtil.show(getActivity(), "登录成功", 1000);
		
	}


	private void getValCode() {
		
		
	}
	MyCountDownTimer timer;

	private void setCountDown(long sendtime, final Button sendBtn) {

		long curTime = Calendar.getInstance().getTimeInMillis();
		long remainTime = (getResources().getInteger(
				R.integer.regist_countdown_time)*1000
				- curTime + sendTime);
		Log.d(TAG, "curTime:" + curTime + "  sendTime:" + sendtime);
		if (timer!=null)timer.cancel();
		timer=new MyCountDownTimer(remainTime, 1000, sendBtn);
		timer.start();
	}

	private String rebuildResendStr(long sendtime) {
		long curTime = Calendar.getInstance().getTimeInMillis();
		long remainTime = (getResources().getInteger(
				R.integer.regist_countdown_time)
				- curTime + sendtime);
		Log.d(TAG, "curTime:" + curTime + "  sendTime:" + sendtime);
		return getResources().getString(R.string.regist_countdown_btn) + "("
				+ remainTime + ")";
	}

	class MyCountDownTimer extends CountDownTimer{
		private TextView sendBtn;

		public MyCountDownTimer(long millisInFuture, long countDownInterval,Button button) {
			super(millisInFuture, countDownInterval);
			sendBtn=button;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			sendBtn.setText("重新发送" + "("
					+ millisUntilFinished/1000 + ")");// 设置倒计时时间

		}
		
		@Override
		public void onFinish() {
			sendBtn.setText("重新获取验证码");
			sendBtn.setEnabled(true);// 重新获得点击
		}
	}
	
	private void setListener(View v){
		v.findViewById(R.id.login_send_customercode).setOnClickListener(this);
		v.findViewById(R.id.login_confirm_btn).setOnClickListener(this);
		v.findViewById(R.id.login_resend).setOnClickListener(this);
		v.findViewById(R.id.back).setOnClickListener(this);
	}

	private void sendCode() {
	}

	private void setToBeforePanel() {
		View view=getView();
		view.findViewById(R.id.login_phone_input_scroller).setVisibility(View.VISIBLE);
		view.findViewById(R.id.login_phone_confirm_scroller).setVisibility(View.GONE);
		((EditText)view.findViewById(R.id.login_input_name)).setText("");
	}

	private void setToConfrimPanel() {
		View view=getView();
		String phone=((TextView)view.findViewById(R.id.login_input_name)).getText().toString();
		view.findViewById(R.id.login_phone_input_scroller).setVisibility(View.GONE);
		view.findViewById(R.id.login_phone_confirm_scroller).setVisibility(View.VISIBLE);

		View backbtn=view.findViewById(R.id.back);
		backbtn.setVisibility(View.VISIBLE);

		TextView phoneTxt = (TextView) view.findViewById(R.id.regist_msg_sended_text);
		phoneTxt.setText(phone);
		if (!phone.equals(lastphone)) {
			lastphone=phone;
			sendTime= Calendar.getInstance().getTimeInMillis();
		}
		final Button sendBtn = (Button) view.findViewById(R.id.login_resend);
		setCountDown(sendTime, sendBtn);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_send_customercode:
			sendCode();
			setToConfrimPanel();
			ToastUtil.showShort(getContext(),"服务器暂不支持验证码登陆");
			break;
		case R.id.login_confirm_btn:
			summitValCode();
			break;
		case R.id.login_resend:
			resend((Button)v);
			case R.id.back:
				setToBeforePanel();
		default:
			break;
		}
		
	}


}
