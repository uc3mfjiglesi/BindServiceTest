package es.cice.bindservicetest.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import es.cice.bindservicetest.MainActivity;
import es.cice.bindservicetest.R;

public class MyBindService extends Service {

    private Messenger messenger;
    public final static int INIT_CONNECTION=0;
    public final static int MSG1=1;
    public final static int MSG2=2;
    public final static int MSG3=3;
    private Messenger clientMessenger;
    public MyBindService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyHandler handler=new MyHandler();
        messenger=new Messenger(handler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return messenger.getBinder();

    }

    public class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Notification.Builder builder;
            Notification n;
            switch(msg.what){
                case INIT_CONNECTION:
                    clientMessenger=msg.replyTo;
                    Message m=Message.obtain();
                    m.what= MainActivity.COMPLETED_CONNECTION;
                    try {
                        clientMessenger.send(m);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG1:
                    builder=new Notification.Builder(getApplicationContext());
                    builder
                            .setContentText("Mensaje 1 recibido")
                            .setSmallIcon(R.drawable.ic_msg1)
                            .setContentTitle("MSG 1");
                    n=builder.build();
                    ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(1,n);
                    break;
                case MSG2:
                    builder=new Notification.Builder(getApplicationContext());
                    builder
                            .setContentText("Mensaje 2 recibido")
                            .setSmallIcon(R.drawable.ic_msg2)
                            .setContentTitle("MSG 2");
                    n=builder.build();
                    ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(1,n);
                    break;
                case MSG3:
                    builder=new Notification.Builder(getApplicationContext());
                    builder
                            .setContentText("Mensaje 3 recibido")
                            .setSmallIcon(R.drawable.ic_msg3)
                            .setContentTitle("MSG 3");
                    n=builder.build();
                    ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(1,n);
                    break;
            }
        }
    }
}
