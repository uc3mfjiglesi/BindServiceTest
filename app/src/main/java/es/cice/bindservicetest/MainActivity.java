package es.cice.bindservicetest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import es.cice.bindservicetest.services.MyBindService;

public class MainActivity extends AppCompatActivity {

    private Messenger messenger;
    public static final String TAG="MainActivity";
    private Button connectionBtn,sendMessageBtn;
    private ActivityHandler mHandler;
    public final static int COMPLETED_CONNECTION=1;
    private TextView monitor;

    public class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger=new Messenger(iBinder);
            connectionBtn.setEnabled(false);
            sendMessageBtn.setEnabled(true);
            Messenger serviceMessenger=new Messenger(mHandler);
            Message msg=Message.obtain();
            msg.what=MyBindService.INIT_CONNECTION;
            msg.replyTo=serviceMessenger;
            Log.d(TAG,"recibido el IBinder del servicio conectado...");
            try {
                Log.d(TAG,"completando la conexion con el servicio...");
                messenger.send(msg);
            } catch (RemoteException e) {
                Log.d(TAG,"imposible completar la conexion...");
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger=null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionBtn= (Button) findViewById(R.id.btn1);
        sendMessageBtn= (Button) findViewById(R.id.btn2);
        sendMessageBtn.setEnabled(false);
        mHandler=new ActivityHandler();
        monitor= (TextView) findViewById(R.id.monitor);
    }

    public void sendMessage(View v){
        Random rnd=new Random();
        int msgType=rnd.nextInt(3);
        Message msg=Message.obtain();
        msg.what=msgType+1;
        Log.d(TAG,"Enviando mensaje de tipo " + msg.what);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bindToService(View v){
        bindService(new Intent(this, MyBindService.class),new MyServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    public class ActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case COMPLETED_CONNECTION:
                    monitor.setText("Conexion completada...");
                    break;
            }
        }
    }



}
