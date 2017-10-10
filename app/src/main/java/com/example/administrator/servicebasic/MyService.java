package com.example.administrator.servicebasic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {

    public MyService() {
        Log.i("MyService", "Constructor");
    }

    // 컴포넌트는 바인더를 통해 서비스에 접근할 수 있다.
    public class MyBinder extends Binder {

        public MyBinder(){

        }

        public MyService getService(){
            return MyService.this;
        }
    }

    // 생성된 서비스를 넘겨주는 역할을 주로 한다.
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MyService", "==========onBind");
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService", "==========onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**
         * 서비스는 거의 포어그라운드로 사용한다고 보면 됨.
         */
        // 포어그라운드 서비스하기
        // 포어그라운드 번호
        int FLAG = 1746;

        // 포어그라운드 서비스에서 보여질 노티바 만들기
        // 참고로 여기서 사용하는 컨텍스트가 서비스의 컨텍스트이기 때문에 포어그라운드 서비스는 서비스에서만 사용할 수 있다.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification noti = builder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("노티 타이틀")
                .build();
        // 링크도 걸어주고, xml 로 자유롭게 커스터마이징 할 수 있다.


        // 노티바 노출시키기
        // 노티피케이션 매니저를 통해 노티바를 출력
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(FLAG, noti);


        Log.i("MyService", "==========onStartCommand");
        for (int i = 0; i < 1000; i++) {
            System.out.println(i+" 서비스에서 동작중입니다");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyService", "==========onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("MyService", "==========onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("MyService", "==========onRebind");
        super.onRebind(intent);
    }

    public void print(String string){
        Log.i("MyAcyivity", string);
    }

}
