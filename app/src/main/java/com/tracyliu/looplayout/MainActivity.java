package com.tracyliu.looplayout;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LoopLayout mLoopLayout;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoopLayout = findViewById(R.id.loop);
        mLoopLayout.requestFocus();
        final String[] mSelectNames = getResources().getStringArray(R.array.dtv_select);

        mLoopLayout.setOnItemSelectListener(new LoopLayout.OnItemSelectListener() {
            @Override
            public void onItemselect(int position) {
                index = position;
            }
        });

        mLoopLayout.mImageView03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, mSelectNames[index], Toast.LENGTH_SHORT).show();
            }
        });

        mLoopLayout.setOnKeyListener(new LoopLayout.OnKeyListener() {

                                         @Override
                                         public boolean onKey(View v, int keyCode, KeyEvent event) {

                                             if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                                 if (event.getAction() == KeyEvent.ACTION_UP) {
                                                     AnimationDrawable animationDrawable = (AnimationDrawable) mLoopLayout.mImageView03.getBackground();
                                                     animationDrawable.run();
                                                 }
                                             }


                                             if (event.getAction() == KeyEvent.ACTION_UP) {

                                                 if (keyCode == KeyEvent.KEYCODE_ENTER
                                                         || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                                                     Toast.makeText(MainActivity.this, mSelectNames[index], Toast.LENGTH_SHORT).show();
                                                 }
                                                 return true;
                                             }
                                             return false;
                                         }
                                     }

        );
    }
}
