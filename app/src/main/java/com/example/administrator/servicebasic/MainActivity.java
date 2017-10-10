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

/**
 * 왜 서비스를 사용하는가?
 *
 *
 *
 * 1. bindService 호출
 * 3. 리턴된 바인더가 onServiceConnected 의 인자로 넘어옴
 */
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
            // 값을 주고 받기 위해서 bindService 사용한다.
            case R.id.bind:
                // bind 할 대성이 없어도 bind 되게 해줌
                // bind 된 상태에서는 다시 bind 되지 않고, stopService 해도 unBind 될 때까지 풀어지지 않음
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                unbindService(connection);
                break;
        }
    }

    // 서비스와의 연결 통로라고 생각
    // 액티비티가 서비스와 통신하고 싶을 때 connection 을 통해서 한다
    ServiceConnection connection = new ServiceConnection() {
        // 서비스와 연결되는 순간 호출 -- onBind 가 호출될 때 이쪽으로 Binder 를 구현한 MyBinder 인터페이스를 넘겨준다
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // bind 를 통해 서비스를 가져올 수 있다.
            MyService.MyBinder iBinder = (MyService.MyBinder) binder;
            MyService service = iBinder.getService();
            service.print("연결되었습니다.");
        }

        // 일반적인 상황에서는 호출되지 않음 onDestroy 에서는 호출되지 않는다.
        // 서비스가 도중에 끊기거나 연결이 중단되면 호출된다.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MainActivity", "=========onServiceDisconnected");
        }
    };

}
