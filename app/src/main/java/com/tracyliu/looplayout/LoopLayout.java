package com.tracyliu.looplayout;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tracyliu on 2016/8/23.
 */
public class LoopLayout extends LinearLayout implements View.OnClickListener {


    private static final String TAG = "LoopLayout";
    public int LOOP_NUM;

    public List<LauncherBean> mLooplist;

    private int[] mDTVAnim = {
            R.drawable.anim_dtv,
            R.drawable.anim_market,
            R.drawable.anim_app,
            R.drawable.anim_setting,
            R.drawable.anim_browser,
            R.drawable.anim_local,

    };

    private int[] mDTVFronts = {

            R.drawable.front_dvr_off,
            R.drawable.front_market,
            R.drawable.front_app,
            R.drawable.front_setting_off,
            R.drawable.front_browser,
            R.drawable.front_local
    };
    private int[] mDTVImages = {
            R.drawable.img_dtv,
            R.drawable.img_market,
            R.drawable.img_app,
            R.drawable.img_setting,
            R.drawable.img_browser,
            R.drawable.img_local,
    };

    private String[] mSelectNames;

    private TextView mTextView01;
    private TextView mTextView02;
    private TextView mTextView03;
    private TextView mTextView04;
    private TextView mTextView05;

    private ImageView mImageView01;
    private ImageView mImageView02;
    public ImageView mImageView03;
    private ImageView mImageView04;
    private ImageView mImageView05;

    private ImageView iv_left_arrow;
    private ImageView iv_right_arrow;
    private Context mContext;
    private int mPosition = 0;

    private OnItemSelectListener mSelectListener;

    public interface OnItemSelectListener {
        void onItemselect(int position);
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        mSelectListener = listener;
    }


    public LoopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSelectNames = getResources().getStringArray(R.array.dtv_select);
        initViews();
    }

    public LoopLayout(Context context) {
        super(context);
        mContext = context;
        mSelectNames = getResources().getStringArray(R.array.dtv_select);
        initViews();
    }


    private void initViews() {

        mLooplist = new ArrayList<>();
        for (int i = 0; i < mDTVAnim.length; i++) {

            LauncherBean bean = new LauncherBean(mDTVAnim[i], mDTVFronts[i], mDTVImages[i], mSelectNames[i]);
            mLooplist.add(bean);
        }

        LOOP_NUM = mLooplist.size();

        View view = LayoutInflater.from(mContext).inflate(R.layout.loop, this, true);//attachToRoot必须为true
        mTextView01 = (TextView) view.findViewById(R.id.tv_01);
        mTextView02 = (TextView) view.findViewById(R.id.tv_02);
        mTextView03 = (TextView) view.findViewById(R.id.tv_03);
        mTextView04 = (TextView) view.findViewById(R.id.tv_04);
        mTextView05 = (TextView) view.findViewById(R.id.tv_05);

        mImageView01 = (ImageView) view.findViewById(R.id.iv_01);
        mImageView02 = (ImageView) view.findViewById(R.id.iv_02);
        mImageView03 = (ImageView) view.findViewById(R.id.iv_03);
        mImageView04 = (ImageView) view.findViewById(R.id.iv_04);
        mImageView05 = (ImageView) view.findViewById(R.id.iv_05);

        iv_left_arrow = findViewById(R.id.iv_left_arrow);
        iv_right_arrow = findViewById(R.id.iv_right_arrow);

        mImageView01.setOnClickListener(this);
        mImageView02.setOnClickListener(this);
        mImageView04.setOnClickListener(this);
        mImageView05.setOnClickListener(this);

        iv_left_arrow.setOnClickListener(this);
        iv_right_arrow.setOnClickListener(this);


        setViews(0, true);
    }


    private void setViews(int i, boolean flag) {

        mTextView01.setText(mLooplist.get(i % LOOP_NUM).getIconName());
        mTextView02.setText(mLooplist.get((i + 1) % LOOP_NUM).getIconName());
        mTextView03.setText(mLooplist.get((i + 2) % LOOP_NUM).getIconName());
        mTextView04.setText(mLooplist.get((i + 3) % LOOP_NUM).getIconName());
        mTextView05.setText(mLooplist.get((i + 4) % LOOP_NUM).getIconName());

        mImageView01.setImageResource(mLooplist.get(i % LOOP_NUM).getImages());
        mImageView02.setImageResource(mLooplist.get((i + 1) % LOOP_NUM).getImages());

        if (flag) {
            mImageView03.setBackgroundResource(mLooplist.get((i + 2) % LOOP_NUM).getAnim());
        } else {
            mImageView03.setBackgroundResource(mLooplist.get((i + 2) % LOOP_NUM).getFronts());
        }

        mImageView04.setImageResource(mLooplist.get((i + 3) % LOOP_NUM).getImages());
        mImageView05.setImageResource(mLooplist.get((i + 4) % LOOP_NUM).getImages());
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (gainFocus) {
            setViews(mPosition, true);
            AnimationDrawable animationDrawable = (AnimationDrawable) mImageView03.getBackground();
            animationDrawable.run();
        } else {
            setViews(mPosition, false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            mPosition++;
        } else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            mPosition--;
        }

        int positon = syncPosition(mPosition);
        setViews(positon, true);

        if (mSelectListener != null) {
            int index = syncPosition(mPosition + 2);
            mSelectListener.onItemselect(index);
        }
        return super.onKeyDown(keyCode, event);
    }

    private int syncPosition(int index) {

        if (index < 0) {
            index = LOOP_NUM - 1;
        } else if (index > LOOP_NUM - 1) {
            index = index % LOOP_NUM;
        }
        return index;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_01:
                mPosition = mPosition - 2;
                break;
            case R.id.iv_02:
                mPosition = mPosition - 1;
                break;
            case R.id.iv_04:
                mPosition = mPosition + 1;
                break;
            case R.id.iv_05:
                mPosition = mPosition + 2;
                break;
            case R.id.iv_left_arrow:
                mPosition--;
                break;
            case R.id.iv_right_arrow:
                mPosition++;
                break;
            default:
                break;
        }

        int position = syncPosition(mPosition);
        setViews(position, true);
    }
}
