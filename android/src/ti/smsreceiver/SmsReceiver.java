package ti.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.appcelerator.kroll.common.Log;

public class SmsReceiver  extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		TiSmsReceiverModule module = TiSmsReceiverModule.getModule();
		module.onSmsReceived(context, intent);
	}
}
