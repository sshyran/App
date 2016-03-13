package crixec.app.imagefactory.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ExceptionHandler;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.utils.DeviceUtils;
import crixec.app.imagefactory.utils.HttpUtils;

public class FeedbackActivity extends AppCompatActivity {
	private AppCompatEditText etContact;
	private AppCompatEditText etContent;
	private AppCompatButton btDoFB;

	@SuppressLint("HandlerLeak")
	private Handler feedbackHandler = new Handler() {

		private ProgressDialog dialog;

		@Override
		public void handleMessage(Message msg) {
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2) {
				dialog = new ProgressDialog(FeedbackActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(true);
				dialog.setMessage(getString(R.string.feedback_sending));
				dialog.show();
			} else {
				dialog.dismiss();
				if (msg.what == 1) {
					Toast.makeShortText(getString(R.string.no_available_network));

				} else if (msg.what == 2) {
					Toast.makeShortText(getString(R.string.connect_to_server_failure));
				} else {
					btDoFB.setEnabled(false);
					Toast.makeShortText(getString(R.string.feedback_thanks));
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.drawer_item_name)[0]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_feedback);
		etContact = (AppCompatEditText) findViewById(R.id.activity_feedback_et_contact);
		etContent = (AppCompatEditText) findViewById(R.id.activity_feedback_et_content);
		btDoFB = (AppCompatButton) findViewById(R.id.activity_feedback_bt_do_feedback);
		btDoFB.setEnabled(false);
		etContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
				// TODO: Implement this method
			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				// TODO: Implement this method
			}

			@Override
			public void afterTextChanged(Editable p1) {
				// TODO: Implement this method
				if (p1.toString().length() > 5) {
					btDoFB.setEnabled(true);
				} else {
					btDoFB.setEnabled(false);
				}
			}
		});
		btDoFB.setOnClickListener(new AppCompatButton.OnClickListener() {
			@Override
			public void onClick(View p1) {
				// TODO: Implement this method
				String user = "联系方式:" + etContact.getText().toString();
				String content = "\n反馈内容:" + etContent.getText().toString() + "\n";
				new SendFeedback(user + content + ExceptionHandler.getSystemInfo()).start();

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO: Implement this method
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public class SendFeedback extends Thread {
		private String content;

		public SendFeedback(String content) {
			this.content = content;
		}

		@Override
		public void run() {
			// TODO: Implement this method
			super.run();
			feedbackHandler.sendEmptyMessage(2);
			try {
				Debug.i("Network status : " + DeviceUtils.hasNetwork());
				if (!DeviceUtils.hasNetwork()) {
					feedbackHandler.sendEmptyMessage(1);
				} else {
					String params = "method=1&content=" + content + "&filename=Feedback_" + DeviceUtils.getSystemTime()
							+ ".txt";
					// Debug.writeLog(ImageFactory.SERVER_URL + "?" + params);
					HttpUtils.doPost(ImageFactory.SERVER_URL, params);
					feedbackHandler.sendEmptyMessage(0);
				}
			} catch (Exception e) {
				feedbackHandler.sendEmptyMessage(2);
				ExceptionHandler.handle(e);
				return;
			}
		}

	}
}
