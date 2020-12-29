package ti.smsreceiver;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;


@Kroll.module(name="TiSmsReceiver", id="ti.smsreceiver")
public class TiSmsReceiverModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "TiSmsReceiverModule";
	private static final boolean DBG = TiConfig.LOGD;
	private static TiSmsReceiverModule module = null;

	public TiSmsReceiverModule()
	{
		super();
		module = this;
	}

	public static TiSmsReceiverModule getModule()
	{
		if (module != null)
			return module;
		else
			return new TiSmsReceiverModule();
	}


	@Kroll.method
	public void start(final KrollFunction callback)
	{
		SmsRetrieverClient client = SmsRetriever.getClient(TiApplication.getAppRootOrCurrentActivity());
		Task<Void> task = client.startSmsRetriever();
		KrollDict e = new KrollDict();

		task.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				e.put("success", true);
				callback.callAsync(getKrollObject(), e);
			}
		});

		task.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(Exception err) {
				e.put("error", err.getMessage());
				callback.callAsync(getKrollObject(), e);
			}
		});
	}

	public void onSmsReceived(Context context, Intent intent) {
		if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
			Bundle extras = intent.getExtras();
			Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
			// Log.d(LCAT, "inside: " + status.getStatusCode());
			// Log.d(LCAT, "CommonStatusCodes.SUCCESS: " + CommonStatusCodes.SUCCESS);
			KrollDict e = new KrollDict();
			switch(status.getStatusCode()) {
				case CommonStatusCodes.SUCCESS:
					// Get SMS message contents
					// Extract one-time code from the message and complete verification
					// by sending the code back to your server.
					String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
					e.put("success", true);
					e.put("message", message);
					module.fireEvent("received", e);
				break;
				case CommonStatusCodes.TIMEOUT:
					// Waiting for SMS timed out (5 minutes)
					// Handle the error ...
					e.put("error", "timeout");
					module.fireEvent("received", e);
				break;
			}
		}
	}
}

