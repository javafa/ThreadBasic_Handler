package com.kodonho.android.threadbasic_handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button btnStart, btnStartHandler;

    SubThread thread;
    LooperHandler handlerThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thread = new SubThread(handler);

        tv = (TextView) findViewById(R.id.textView);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.start();
            }
        });

        handlerThread = new LooperHandler(this, "Looper Handler");

        btnStartHandler = (Button) findViewById(R.id.btnStartHandler);
        btnStartHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThread.start();
                run();
                handlerThread.hideProgress();
            }
        });

    }

    public void run(){
        try{
            int sum = 10;
            for(int i=0;i<30;i++){
                sum += i;
                Thread.sleep(100);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static final int SET_RESULT = 1;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case SET_RESULT:
                    int temp = msg.arg1;
                    tv.setText("Result = "+ temp);
                    break;
            }
        }
    };

    class LooperHandler extends HandlerThread {
        Context context;
        ProgressDialog progress;
        public LooperHandler(Context context, String name) {
            super(name);
            this.context = context;
        }

        @Override
        protected void onLooperPrepared() {
            progress = new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle("Progress Bar Title");
            progress.setMessage("Message");
            progress.setCancelable(false);
            progress.show();
            Log.i("LooperHandler","진행상태 보여주기~~~~~~");
        }

        public void hideProgress(){
            progress.dismiss();
            try{
                // 프로그래스 다이얼로그가 해제되기 전에 quit 이 호출되면 화면상에 남아있게되므로
                // quit을 하기전에 1초정도 시간을 둔다
                Thread.sleep(1000);
            }catch (Exception e){};

            quit();
        }
    }


    class SubThread extends Thread{
        Handler mainHandler;

        public SubThread(Handler mHandler){
            mainHandler = mHandler;
        }

        public void run(){
            int sum = 0;
            for(int i=0;i<10000;i++){
                sum += i;
            }
            Message msg = new Message();
            msg.what = SET_RESULT;
            msg.arg1 = sum;
            mainHandler.sendMessage(msg);
        }
    }
}
