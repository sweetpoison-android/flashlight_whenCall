package com.company.flashlight_whencall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Mybootreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyService.class));
        Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
    }
}
