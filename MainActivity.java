package com.example.despertado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private  Viewholde mview=new Viewholde();
    private Handler had=new Handler();
    private Runnable run;
    private boolean bateria=true;

    private  boolean rustop=false;

   static int hr;
long horasdesp;


    private BroadcastReceiver bateriaraceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           int nivel=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);

        mview.tv_nv.setText(String.valueOf(nivel)+ " %");

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mview.tv_hm=findViewById(R.id.tv_hm);
        mview.tv_s=findViewById(R.id.tv_seg);


        mview.tv_nv=findViewById(R.id.tv_nivel);
        mview.cb_nv=findViewById(R.id.cb_nivel);


        mview.iv_pref=findViewById(R.id.iv_pref);
        mview.iv_sair=findViewById(R.id.iv_sair);
        mview.ll_menu=findViewById(R.id.lin_menu);



EditText et_horasdesp=findViewById(R.id.et_horasdesp);
Button despertar=findViewById(R.id.bt_atribuir);




despertar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        hr =Integer.parseInt(et_horasdesp.getText().toString());


        //Long agora= SystemClock.uptimeMillis();
        //horasdesp=Long.parseLong(et_horasdesp.getText().toString());
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());


        int hrf=Integer.parseInt(String.format("%02d",cal.get(Calendar.HOUR_OF_DAY)));


        if(hr==hrf){

            MediaPlayer mp3=MediaPlayer.create(MainActivity.this,R.raw.a);
            mp3.start();
        }


    }
});


         //clear tela

getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



//bateria

registerReceiver(bateriaraceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


//animacao oculta

        mview.ll_menu.animate().translationY(500);


//cleckbox

mview.cb_nv.setChecked(true);

        mview.cb_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bateria){
                    bateria=false;
                    mview.tv_nv.setVisibility(View.GONE);
                }else {
                    bateria=true;
                    mview.tv_nv.setVisibility(View.VISIBLE);

                }

            }
        });



       //imgemview


       mview.iv_sair.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               mview.ll_menu.animate().translationY(mview.ll_menu.getMeasuredHeight()).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime)) ;



           }
       });

        mview.iv_pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mview.ll_menu.setVisibility(View.VISIBLE);

                mview.ll_menu.animate().translationY(0).setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));


            }
        });




    }





    private void atualizahr() {

        run=new Runnable() {
            @Override
            public void run() {

                if(rustop)return;




                Calendar cal=Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());


                String hrf=String.format("%02d:%02d",cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE));
                String segf=String.format("%02d",cal.get(Calendar.SECOND));

                mview.tv_hm.setText(hrf);
                mview.tv_s.setText(segf);


                Long agora= SystemClock.uptimeMillis();



          long prox=agora+(1000-(agora%1000));

          had.postAtTime(run,prox);



            }
        };

run.run();

    }


    @Override
    protected void onResume() {
        super.onResume();


rustop=false;

        atualizahr();





    }

    @Override
    protected void onStop() {
        super.onStop();
        rustop=true;



    }

    private  static  class  Viewholde{
        TextView tv_hm,tv_s;

        CheckBox cb_nv;
        TextView tv_nv;
        ImageView iv_pref,iv_sair;LinearLayout ll_menu;



    }
}