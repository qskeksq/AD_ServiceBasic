# Android Service

#### 서비스는 무엇인가
- 서비스는 인터페이스이다. 구현체가 필요함
- 1. 스레드는 액티비티가 강제 종료되면 끝이지만 서비스는 onStartCommand의 반환값에 따라 다시 살아난다.
- 2. 스레드는 프로세스 내부에서는 메모리를 공유하지만 외부 프로세스에 접근할 수 없다. 서비스는 외부 프로세스에 접근 가능하기 때문에 알림, 위치, 노티, 네트워크를 통한 데이터 전송 및 수신, 음악 재생, 파일 I/O, 컨텐트 프로바이더와의 상호작용이 가능하다.
- 3. 서비스 인터페이스 구현체 중 하나인 IntentService를 통해 여러 요청 서비스를 sequential하게 처리할 수 있다.
- 4. 액티비티가 종료된 상태에서도 작동한다. 사용자가 앱에서 벗어나더라도 계속 실행된다는 의미이다.

#### Service

1. 서비스 실행중 - started 상태
    - 서비스가 시작되면 자신을 호출한 컴포넌트와는 별개의 생명주기로 활동하며, 끝났다면 stop해 줘야 한다.
    - startService
    - stopService/stopSelf

2. 다른 컴포넌트와 bind 됨
    - unbind할 대상이 없을 경우 unbind 하면 오류 발생
    - bindService 
    - unbindService

3. 생명주기
    - onCreate() : 서비스가 살아있는 동안 사용할 자원들을 세팅한다
    - onStartCommand() : 다른 컴포넌트에서 startService() 할 경우 호출, background에서 실행되기 시작한다
    - onBind() : 다른 컴포넌트에서 bindService()를 호출할 경우 호출, bind는 목적이 통신을 위해 사용하는 것이므로 호출한 컴포넌트에서 서비스를 사용할 수 있도록 인터페이스를 리턴한다.
    - onDestroy() : 서비스 생명주기 가장 마지막에 호출됨으로 내가 사용했던 자원들을 모두 정리해 줘야 한다

4. 구현
    - Service 상속
    - IntentService 상속 : worker 스레드를 만들어서 요청들을 한번에 하나씩 처리한다. 여러 요청을 동시에 처리해야 하는 상황이 아니라면 이 서비스를 사용하자

5. 설명
    - 어떤 컴포넌트가 startService()를 호출해서 서비스를 실했했다면 onStartCommand()가 호출. stop해주기 전까지 계속 실행된다
    - bindService()를 호출해서 서비스를 생성했다면 onStartCommnad()가 호출되지는 않으며, 그 액티비티에 bind되어 있는 동안에만 실행된다. 따라서 서비스를 bind한 컴포넌트들이 모두 unbind되면 시스템은 서비스를 모두 종료한다
    - started 되지 않은 상황에서 bind 하면 서비스가 생성되지만, started가 되지는 않는다. 따라서 unbind 하면 서비스가 onDestroy되어 종료된다. startServce를 통해 started된 상황에서 bind하고 unbind 하면 바인딩만 사라지고 onDestroy가 호출되지는 않는다. 따라서 onDestroy가 호출되려면 일단 startService가 되어야 한다. bindService만 호출하면 unbind로 destroy만 할 수 있다.
    - 서비스는 메모리가 부족하면 시스템이 강제로 종료할 수 있다. 다만. 현재 사용자가 사용중인 컴포넌트에 bind되어 있을 경우 다른 서비스를 먼저 종료한다. 서비스가 foreground로 실행되고 있다면 시스템에서 강제 종료하지 않는다. 서비스는 강제 종료될 경우 재시작 될 때를 대비해서 설계해야 한다. 이는 onStartCommand()가 리턴해 주는 값에 따라 다르게 동작한다. 
    - START_NOT_STICKY, START_STICKY, START_REDELIVER_INTENT

```java
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
```
#### 컴포넌트(액티비티)와 서비스 bind

```java
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
```

>서비스에 구현

```java
// 컴포넌트는 바인더를 통해 서비스에 접근할 수 있다.
public class MyBinder extends Binder {

    public MyBinder(){

    }

    public MyService getService(){
        return MyService.this;
    }
}
```

#### foreground 서비스

```java
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
```