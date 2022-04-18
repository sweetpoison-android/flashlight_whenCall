package com.company.flashlight_whencall;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Random;

public class MyService extends Service {
    CameraManager mngr;
    String CameraID1;
    boolean status=false;
    String s="";

    int mrandomnumber;
    boolean misrandomnumbergenerationon;
    final  int min = 0;
    final int max = 1000;

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service Start",Toast.LENGTH_SHORT).show();


        IntentFilter inf=new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        BroadcastReceiver br=new BroadcastReceiver() {
            boolean incomingFlag;
            @Override
            public void onReceive(Context context, Intent intent) {
                startRandomNumberGenerator();
          s=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
          TelephonyManager tm=(TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);

                switch (tm.getCallState())
                {
                    case TelephonyManager.CALL_STATE_RINGING:
                        incomingFlag=true;
                        s=s+"Ringing";
                        status=true;
                        flashlight_On();
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if(incomingFlag)
                        {
                            s=s+"Accept Call";
                            status=false;
                            flashlight_On();
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        if(incomingFlag)
                        {
                            s=s+"Decline Call";
                            status=false;
                            flashlight_On();
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        registerReceiver(br,inf);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void flashlight_On()
    {
        boolean isflashavailable=getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!isflashavailable)
        {

        }

        mngr=(CameraManager)getSystemService(Context.CAMERA_SERVICE);

        try {
            CameraID1 = mngr.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Switch_flashlight();
    }
    public void Switch_flashlight()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
           boolean bb=true;
           while(status)
           {
               if(bb)
               {
                   try {
                       mngr.setTorchMode(CameraID1,true);
                   } catch (CameraAccessException e) {
                       e.printStackTrace();
                   }
                   bb=false;
               }
               else if(bb==false)
               {
                   try {
                       mngr.setTorchMode(CameraID1,false);
                   } catch (CameraAccessException e) {
                       e.printStackTrace();
                   }
                   bb=true;
               }
               try
               {
                   Thread.sleep(100);
               }catch (InterruptedException e)
               {
                   e.printStackTrace();
               }
           }
                try {
                    mngr.setTorchMode(CameraID1,false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void startRandomNumberGenerator() {

        while (misrandomnumbergenerationon)
        {
            try {
                Thread.sleep(1000);
                if (misrandomnumbergenerationon)
                {
                    mrandomnumber = new Random().nextInt(max)+min;
                    Log.i("Service_Demo", "Thread_ID : "+Thread.currentThread().getId()+" , Random Number : "+mrandomnumber);
                }
            } catch (InterruptedException e) {
                Log.i("ServiceDemo ", "Thread Interupted");
            }
        }

    }

}
