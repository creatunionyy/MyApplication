package com.library.core.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;

/**
 * 获取验证码计时
 *
 */
public class TimerUtil {
	
	private String btnText;
	private Timer mTimer;
	private Button mCodeBtn;
	public int time = 5;

	public TimerUtil(Button btn, String text) {
		this.mCodeBtn = btn;
		this.btnText = text;
	}

	private final Handler upHandler = new Handler() {
		private int[] location;
		private int y;

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (time > 0) {
					mCodeBtn.setEnabled(false);
					location = new int[2];
					mCodeBtn.getLocationOnScreen(location);
					y = location[1];
					if (y > 120) {
						mCodeBtn.setText("重新发送("+time+"s)");
					}
				} else {
					mTimer.cancel();
					mCodeBtn.setEnabled(true);
					mCodeBtn.setText(btnText);
					time = 5;
				}
				break;
			default:
				break;
			}
		};

	};

	public void runTimer() {
		mTimer = new Timer(true);
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				time--;
				Message msg = upHandler.obtainMessage();
				msg.what = 1;
				upHandler.sendMessage(msg);
			}

		};
		mTimer.schedule(task, 100, 1000);
	}

}
