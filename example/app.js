// This is a test harness for your module
// You should do something interesting in this harness
// to test out the module and to provide instructions
// to users on how to use it by example.


// open a single window

var sms_receiver = require('ti.smsreceiver');

var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label = Ti.UI.createLabel({
	text: "Start SMS Receiver"
});

label.addEventListener("click", () => {
	sms_receiver.start(e => {
		if (e.success) {
			label.test = "Waiting for SMS...";
		} else {
			alert(e.error);
		}
	})
});

sms_receiver.addEventListener("received", e => {
	if (e.success) {
		label.text = "SMS Received: " + e.message;
	} else {
		alert(e.error);
	}
});

win.add(label);
win.open();