package com.library.core.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyBordUtil {

	public static void showKeyboard(final View v) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager inputManager = (InputMethodManager) v
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(v, 0);
			}

		}, 500);
	}

	/**
	 * Hides the input method.
	 * 
	 * @param context
	 *            context
	 * @param view
	 *            The currently focused view
	 * @return success or not.
	 */
	public static boolean hideInputMethod(Context context, View view) {
		if (context == null || view == null) {
			return false;
		}

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		return false;
	}

	/**
	 * Show the input method.
	 * 
	 * @param context
	 *            context
	 * @param view
	 *            The currently focused view, which would like to receive soft
	 *            keyboard input
	 * @return success or not.
	 */
	public static boolean showInputMethod(Context context, View view) {
		if (context == null || view == null) {
			return false;
		}

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.showSoftInput(view, 0);
		}

		return false;
	}
	
}
