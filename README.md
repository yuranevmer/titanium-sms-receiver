# ti.smsreceiver

Titanium android module to receive with OTP SMS code.

## Installation

1. Unpack the module and place it inside the `/modules/android` folder of your project.
Edit the modules section of your `tiapp.xml` file to include this module:
    ```xml
    <modules>
        <module platform="android">ti.smsreceiver</module>
    </modules>
    ```

4. add `receiver` in `android/application` section in `tiapp.xml`
    ```xml
    <receiver android:name="ti.smsreceiver.SmsReceiver" android:exported="true" android:permission="com.google.android.gms.auth.api.phone.permission.SEND">
        <intent-filter>
            <action android:name="com.google.android.gms.auth.api.phone.SMS_RETRIEVED"/>
        </intent-filter>
    </receiver>

    ```

## Methods

- **start**(callback) - returns `Ti.UI.View` with GPay button inside.
    - e.success (Boolean)
    - e.error (String)

## Events

- **received**
    - e.success (Boolean)
    - e.error (String)
    - e.message (String) - SMS content

## Example
    
```js
var sms_receiver = require('ti.smsreceiver');

sms_receiver.start(e => {
    if (e.success) {
        label.text = "Waiting for SMS...";
    } else {
        alert(e.error);
    }
});

sms_receiver.addEventListener("received", e => {
    if (e.success) {
        label.text = "SMS Received: " + e.message;
    } else {
        alert(e.error);
    }
});
```