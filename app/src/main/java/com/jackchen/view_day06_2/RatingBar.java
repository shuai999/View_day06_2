package com.jackchen.view_day06_2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/19 15:01
 * Version 1.0
 * Params:
 * Description:  评分控件RatingBar
*/

public class RatingBar extends View {


    // 用BitmapFactory 解析出来的没有选中的图片
    private Bitmap mStarNormalBitmap;
    // 用BitmapFactory 解析出来的已经选中的图片
    private Bitmap mStarFocusBitmap;
    // 总共的分数
    private int mGradeNumber = 5 ;
    // 当前的分数
    private int mCurrentNumber = 0 ;


    public RatingBar(Context context) {
        this(context,null);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 以下是获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        // 获取资源id
        int starNormalId = array.getResourceId(R.styleable.RatingBar_starNormal, 0);
        if (starNormalId == 0){
            throw new RuntimeException("请设置属性 starNormal") ;
        }
        // 用BitmapFactory 解析资源
        mStarNormalBitmap = BitmapFactory.decodeResource(getResources(), starNormalId);


        int starFocusId = array.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (starFocusId == 0){
            throw new RuntimeException("请设置属性 starFocus") ;
        }
        // 用BitmapFactory 解析资源
        mStarFocusBitmap = BitmapFactory.decodeResource(getResources(), starFocusId);

        // 获取分数
        mGradeNumber = array.getInt(R.styleable.RatingBar_gradeNumber , mGradeNumber) ;
        array.recycle();

    }


    /**
     * 测量控件的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 控件的宽度
        int width = mStarFocusBitmap.getWidth() * mGradeNumber;
        int height = mStarFocusBitmap.getHeight();
        setMeasuredDimension(width , height);
    }


    /**
     * 画选中的图片和未选中的图片
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (int i = 0; i < mGradeNumber; i++) {
            // i * 星星的宽度
            int x = i * mStarFocusBitmap.getWidth() ;

            // 触摸的时候 mCurrentGrade的值是不断变化的
            if (mCurrentNumber > i){
                canvas.drawBitmap(mStarFocusBitmap , x , 0 , null);
            }else{
                canvas.drawBitmap(mStarNormalBitmap , x , 0 , null);
            }
        }
    }


    /**
     * 处理用户触摸
     * 移动、按下、抬起，处理逻辑都是一样的，判断手指的位置，根据当前位置计算出分数，然后去刷新并显示
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:   // 尽量减少onDraw()方法的调用
            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_UP:    // 尽量减少onDraw()

                // 手指的位置，计算当前的分数
                float moveX = event.getX();
                // 计算当前分数
                int currentGrade = (int) (moveX/mStarFocusBitmap.getWidth()+1);

                //范围问题
                if (currentGrade < 0){
                    currentGrade = 0 ;
                }

                if (currentGrade > mGradeNumber){
                    currentGrade = mGradeNumber ;
                }

                // 分数相同的情况下，就不要再去绘制了，尽量减少onDraw()方法的调用
                if (currentGrade == mGradeNumber){
                    return true ;
                }

                // 判断完异常情况之后，最后记录当前分数，然后重新绘制
                mCurrentNumber = currentGrade ;
                invalidate();    // 因为 invalidate()方法会调用
                break;
        }
        return true;
    }
}
