package org.yuhanxun.libcommonutil.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.yuhanxun.libcommonutil.R;


public class LoadingDialog extends Dialog {

	// private Context context = null;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	private static LoadingDialog loadingDialog = null;

	private boolean respondKeyBack = false;
	private Context mContext;

	public boolean isRespondKeyBack() {
		return respondKeyBack;
	}

	public void setRespondKeyBack(boolean respondKeyBack) {
		this.respondKeyBack = respondKeyBack;
	}

	public LoadingDialog(Context context) {
		super(context);
		this.mContext = context;
		 
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public static LoadingDialog createDialog(Context context) {
		loadingDialog = new LoadingDialog(context, R.style.CustomProgressDialog);
		loadingDialog.setContentView(R.layout.loading_default_progress_dialog);
		loadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return loadingDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (loadingDialog == null) {
			return;
		}
//		ImageView iv = (ImageView) loadingDialog.findViewById(R.id.loadingImageView);
//		AnimationDrawable ad = (AnimationDrawable) iv.getBackground();
//		ad.start();
//		ImageView imageView = (ImageView) loadingDialog.findViewById(R.id.loadingImageView);
//		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//		animationDrawable.start();
	}

	/**
	 * 
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	// public LoadingDialog setTitile(String strTitle) {
	// return loadingDialog;
	// }

	/**
	 * 
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingDialog setMessage(String strMessage) {
//		TextView tvMsg = (TextView) loadingDialog.findViewById(R.id.id_tv_loadingmsg);
//
//		if (tvMsg != null) {
//			tvMsg.setText(strMessage);
//		}
		return loadingDialog;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (respondKeyBack)
				loadingDialog.dismiss();
		}
		return true;
	}

}
