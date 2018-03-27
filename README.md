# LoopLayout
a Layout for TV Launcher



自定义View LoopLayout 继承 LinearLayout




![LoopLayoutgif](C:\Users\DELL\Desktop\LoopLayoutgif.gif)


需求：选中的Item要有动画效果；Launcher主页上的Item支持循环。点击非选中item后，item来到中间选中位置。



自定义控件一般可以分为两种：继承控件、复合控件。前者是通过继承View或其子类，重写onDraw()方法来绘制View的显示内容。后者通常继承ViewGroup及其子类（LinearLayout、RelativeLayout、FrameLayout等），内部添加其他控件，从而组合成新的复合控件。本次即为复合控件。

通常来说，我们会定义好一个layout.xml文件，然后在构造器中通过调用inflate(R.layout.my_layout,this,true)加载该xml文件。可以在构造器或onFinishInflate()方法来获取（建议第二种，保证控件已经完全加载好）子控件对象，然后设置属性。

值得注意的两点：

1.在构造器中通过调用inflate(R.layout.my_layout,this,true)加载该xml文件。可以在构造器或onFinishInflate()方法来获取（建议第二种，保证控件已经完全加载好）子控件对象；

2.如果需要从资源文件中加载该自定义控件，则必须重写Constructor(Context context, AttributeSet attrs)此构造方法；



    public LoopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSelectNames = getResources().getStringArray(R.array.dtv_select);
        initViews();
    }
    //从xml Inflate view
    public LoopLayout(Context context) {
        super(context);
        mContext = context;
        mSelectNames = getResources().getStringArray(R.array.dtv_select);
        initViews();
    }

两个构造方法，区别为在代码中创建View的时候用View(Context)，从xml Inflate view的时候重写View(Context,Attributeset)。

     private void initViews() {

            mLooplist = new ArrayList<>();
            for (int i = 0; i < mDTVAnim.length; i++) {
                LauncherBean bean = new LauncherBean(mDTVAnim[i], mDTVFronts[i], mDTVImages[i], mSelectNames[i]);
                mLooplist.add(bean);
            }

            LOOP_NUM = mLooplist.size();

            //attachToRoot必须为true 否则不能添加到root布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.loop, this, true);
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

            iv_left_arrow = view.findViewById(R.id.iv_left_arrow);
            iv_right_arrow = view.findViewById(R.id.iv_right_arrow);

            mImageView01.setOnClickListener(this);
            mImageView02.setOnClickListener(this);
            mImageView04.setOnClickListener(this);
            mImageView05.setOnClickListener(this);

            iv_left_arrow.setOnClickListener(this);
            iv_right_arrow.setOnClickListener(this);

            setViews(0, true);
        }

有焦点的时候播放动画

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

支持遥控器操作

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            mPosition++;
        } else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
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

同步index位置

    private int syncPosition(int index) {

        if (index < 0) {
            index = LOOP_NUM - 1;
        } else if (index > LOOP_NUM - 1) {
            index = index % LOOP_NUM;
        }
        return index;
    }

支持点击

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
