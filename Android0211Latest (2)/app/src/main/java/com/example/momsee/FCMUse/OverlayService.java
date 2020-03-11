package com.example.momsee.FCMUse;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.example.momsee.R;

public class OverlayService extends Service {
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Button momsee, unlock, stop,Call;
    private BlockThread block;
    final Handler handler1 = new Handler();
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    LayoutInflater inflater;
    ConstraintLayout layout;
    View temp;
    long Point,Duration;

    private String getForegroundPackageName() {

        String packageName = "";
        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            final long endTime = System.currentTimeMillis();
            final long beginTime = endTime - 100000;
            final UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    packageName = event.getPackageName();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            setting = getSharedPreferences("Set", 0);
            editor = setting.edit();

            Duration = 60*1000*setting.getLong("Freetime",30);
            editor.putLong("Duration", Duration);
            editor.commit();

            block = new BlockThread();
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (ConstraintLayout) inflater.inflate(R.layout.activity_overlay, null);
            TextView Remain = layout.findViewById(R.id.textRemain);
            Remain.setText(String.valueOf(setting.getLong("Freetime",30)));
            Call = layout.findViewById(R.id.btnCall);
            momsee = layout.findViewById(R.id.momsee);

            momsee.setOnClickListener(v -> {
                if (v.getId() == R.id.momsee) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.android0211");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            unlock = layout.findViewById(R.id.unlock);
            unlock.setOnClickListener(v -> {
                if (v.getId() == R.id.unlock) {
                    editor.putLong("Point", System.currentTimeMillis());
                    editor.putLong("Duration", Duration);
                    editor.putLong("Freetime",0);
                    editor.commit();
                    Remain.setText(String.valueOf(setting.getLong("Freetime",30)));
                    block.SleepForSecond(Duration);
                }
            });
            stop = layout.findViewById(R.id.stop);
            stop.setOnClickListener(v -> {
                if (v.getId() == R.id.stop) {
                    stopSelf();
                }
            });
            Call.setOnClickListener(v -> {
                if (v.getId() == R.id.btnCall) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.samsung.android.dialer");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,               //�׻� �� ������ �ְ�. status bar �ؿ� ����. ��ġ �̺�Ʈ ���� �� ����.
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            block.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        block.Stop();//이거 추가해서 언락이됨
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class BlockThread extends Thread {
        boolean flag = true;
        long Second = 0;

        public void run() {
            while (flag) {
                int i = 0;
                long Point = setting.getLong("Point", 0);
                long Duration = setting.getLong("Duration", 0);
                Second = ( Point + Duration) - System.currentTimeMillis();
                String Name = getForegroundPackageName();
                try {
                    if (Second > 0) {
                        if (temp != null) {
                            handler1.post(() ->
                                    mWindowManager.removeViewImmediate(layout));
                        }
                        temp = null;
                        Thread.sleep(Second);
                        Second = 0;
                        flag = true;
                        editor.putLong("Point", 0);
                        editor.putLong("Duration", 0);
                        editor.commit();
                    }

                    else {
                         if(Name.equals("com.example.android0211")||Name.equals("com.samsung.android.dialer")){
                            if (temp != null) {
                                handler1.post(() ->
                                        mWindowManager.removeViewImmediate(layout));
                                temp = null;
                            }
                        }
                        else {
                            if(temp == null) {
                                try {
                                    handler1.post(() -> {
                                        try {
                                            mWindowManager.addView(layout, mParams);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                temp = layout;
                            }
                        }
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(temp!=null)
                handler1.post(() -> mWindowManager.removeViewImmediate(layout));
        }

        public void Stop() {
            this.flag = false;
        }

        public void SleepForSecond(long Second) {
            this.Second = Second;
        }
    }

}