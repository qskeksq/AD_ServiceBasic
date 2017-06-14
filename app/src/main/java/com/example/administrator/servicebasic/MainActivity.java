package com.example.administrator.servicebasic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.bind).setOnClickListener(this);
        findViewById(R.id.unbind).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MyService.class);
        switch (v.getId()){
            case R.id.start:
                startService(intent);
                break;
            case R.id.stop:
                stopService(intent);
                break;
            case R.id.bind:
                bindService(intent, connection, Context.BIND_AUTO_CREATE); // binf 할 대성이 없어도 bind 되게 해줌
                break;
            case R.id.unbind:
//                unbindService(intent);
                break;
        }
    }

    // 액티비티가 서비스와 통신하고 싶을 때 connection 을 통해서 한다
    ServiceConnection connection = new ServiceConnection() {
        // 서비스와 연결되는 순간 호출 -- onBind 가 호출될 때 이쪽으로 binder 객체를 넘겨준다.
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("MainActivity", "onServiceConnected");
            MyService.MyBinder iBinder = (MyService.MyBinder) binder;
            MyService service = iBinder.getService();
            service.print("연결되었습니다.");
        }
        // 일반적인 상황에서는 호출되지 않음 onDestroy 에서는 호출되지 않는다.
        // 서비스가 도중에 끊기거나 연결이 중단되면 호출된다.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MainActivity", "onServiceDisconnected");
        }
    };

}
