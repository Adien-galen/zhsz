package com.example.zhsz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhsz.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/16  7:33
 * <p/>
 * 描     述 ：下拉刷新的ListView
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final int SHTATE_PULL_REFRESH = 0;//下拉刷新
    private static final int SHTATE_RELEASE_REFRESH = 1;//释放刷新
    private static final int SHTATE_REFRESHING = 2;//正在刷新
    private int mCurrentState = SHTATE_PULL_REFRESH;// 当前状态

    @InjectView(R.id.iv_arr)
    ImageView ivArr;
    @InjectView(R.id.pb_progress)
    ProgressBar pbProgress;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_time)
    TextView tvTime;

    private int startY = -1;//滑动起点的Y坐标
    private View mHeaderView;
    private int mHeaderViewHeight;
    private RotateAnimation animDown;
    private RotateAnimation animUp;
    private View mFooterView;
    private int mFooterViewHeight;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        ButterKnife.inject(this,mHeaderView);  //不加this，不起作用
        mHeaderView.measure(0, 0);   //测量
        //得到高度
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();

        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏头布局

        initArrowAnim();
        tvTime.setText("最后刷新时间"+getCurrentTime());

    }

    /*
	 * 初始化脚布局
	 */
    private void initFooterView(){
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);

        this.addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);

        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(startY==-1){// 确保startY有效
                    startY = (int) ev.getRawY();

                }
                if(mCurrentState==SHTATE_REFRESHING)// 正在刷新时不做处理
                {
                    break;
                }
                int endY = (int) ev.getRawY();
                int dy = endY-startY;// 移动偏移量
                if(dy>0&&getFirstVisiblePosition()==0){// 只有下拉并且当前是第一个item,才允许下拉
                    int padding = dy -mHeaderViewHeight;  //计算padding
                    mHeaderView.setPadding(0,padding,0,0);  //设置padding

                    if(padding<0&&mCurrentState!=SHTATE_PULL_REFRESH){// 状态改为松开刷新
                        mCurrentState=SHTATE_PULL_REFRESH;
                        refreshState();
                    }else if(padding>0&&mCurrentState!=SHTATE_RELEASE_REFRESH){// 改为下拉刷新状态
                        mCurrentState=SHTATE_RELEASE_REFRESH;
                        refreshState();
                    }

                    return true ;
                }

                break;

            case MotionEvent.ACTION_UP:
                startY=-1;

                if(mCurrentState==SHTATE_RELEASE_REFRESH){
                    mCurrentState=SHTATE_REFRESHING;//正在刷新
                    mHeaderView.setPadding(0,0,0,0);    // 显示
                    refreshState();
                }else if (mCurrentState==SHTATE_PULL_REFRESH){
                       mHeaderView.setPadding(0,-mHeaderViewHeight,0,0); // 隐藏
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化下拉刷新动画
     */
    private void initArrowAnim() {
        // 箭头向上动画
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true); //设置动画持续执行

        // 箭头向下动画
        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true); //设置动画持续执行


    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrentState){
            case SHTATE_PULL_REFRESH: //下拉刷新
                tvTitle.setText("下拉刷新");
                ivArr.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArr.startAnimation(animDown);
                break;
            case SHTATE_RELEASE_REFRESH: //松开刷新
                tvTitle.setText("松开刷新");
                ivArr.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArr.startAnimation(animUp);
                break;
            case SHTATE_REFRESHING: //正在刷新
                tvTitle.setText("正在刷新");
                ivArr.clearAnimation();// 必须先清除动画,才能隐藏
                ivArr.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);

                if(mListener!=null){
                    mListener.onRefresh();
                }
                break;
            default:
                break;
        }
    }

    OnrefreshListener mListener;

    public void setOnrefreshListener(OnrefreshListener listener){
        mListener=listener;
    }

    public interface OnrefreshListener{
        public void onRefresh();
        public void loadMore();// 加载下一页数据
    }



    /*
	 * 收起下拉刷新的控件
	 */
    public void onRefreshComplete(boolean success){

        if(isLoadMore){// 正在加载更多...
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);// 隐藏脚布局
            isLoadMore = false;
        }else {
            mCurrentState=SHTATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");
            ivArr.setVisibility(View.VISIBLE);
            pbProgress.setVisibility(View.INVISIBLE);

            mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
            if(success){
                tvTime.setText("最后刷新时间:"+getCurrentTime());
            }
        }

    }

    /**
     * 获取当前时间
     * @return
     */
    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("currentTime："+format.format(new Date()));
        return format.format(new Date());
    }


    private boolean isLoadMore;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
            if(getLastVisiblePosition()==getCount()-1&&!isLoadMore){// 滑动到最后
                System.out.println("到底了.....");
                mFooterView.setPadding(0,0,0,0);
                setSelection(getCount()-1);

                isLoadMore = true;
                if(mListener!=null){
                    mListener.loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mItemClickListener!=null){
            mItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }
    }

    OnItemClickListener mItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener=listener;
    }


}
