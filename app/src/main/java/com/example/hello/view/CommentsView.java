package com.example.hello.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.hello.CircleMovementMethod;
import com.example.hello.data.CommentsBean;
import com.example.hello.data.UserBean;

import java.util.List;

/**
 * 评论*/
public class CommentsView extends LinearLayout {

    private Context mContext;
    private List<CommentsBean> mDatas;
    private onItemClickListener listener;
    private onItemLongClickListener longClickListener;

    public CommentsView(Context context) {
        this(context, null);
    }

    public CommentsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        this.mContext = context;
    }

    /**
     * 设置评论列表信息
     *
     * @param list
     */
    public void setList(List<CommentsBean> list) {
        mDatas = list;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setLongClickListener(onItemLongClickListener listener) {
        this.longClickListener = listener;
    }


    /**
     * 刷新评论*/
    public void notifyDataSetChanged() {
        removeAllViews();
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 10);
        for (int i = 0; i < mDatas.size(); i++) {
            View view = getView(i);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }
            addView(view, i, layoutParams);
        }
    }

    private View getView(final int position) {
        final CommentsBean item = mDatas.get(position);
        UserBean replyUser = item.getReplyUser();
        boolean hasReply = false;   // 是否回复楼主
        if (replyUser != null) {
            hasReply = true;
        }
        TextView textView = new TextView(mContext);
        textView.setTextSize(15);
        textView.setTextColor(0xff686868);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        UserBean comUser = item.getCommentsUser();
        String name = comUser.getUserName();
        if (hasReply) {
            builder.append("  ");
            builder.append(setClickableSpan(name, item.getCommentsUser()));
            builder.append(" 回复 ");
            builder.append(setClickableSpan(replyUser.getUserName(), item.getReplyUser()));

        } else {
            builder.append("  ");
            builder.append(setClickableSpan(name, item.getCommentsUser()));
        }
        builder.append(" : ");
        builder.append(setClickableSpanContent(item.getContent(), position));
        textView.setText(builder);
        // 设置点击背景色
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
//        textView.setHighlightColor(0xff000000);

        textView.setMovementMethod(new CircleMovementMethod(0xffcccccc, 0xffcccccc));

        //点击评论内容
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position, item);

                }
            }
        });
        //长按评论内容响应
        textView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
               longClickListener.onItemLongClick(position,item);
            }
            return true;
        });

        return textView;
    }

    /**
     * 设置评论内容点击事件
     *
     * @param item
     * @param position
     * @return
     */

    public SpannableString setClickableSpanContent(final String item, final int position) {
        final SpannableString string = new SpannableString(item);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                //Toast.makeText(mContext, "position: " + position + " , content: " + item, Toast.LENGTH_SHORT).show();

            }


            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 设置显示的内容文本颜色
                ds.setColor(0xff686868);
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(span, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    /**
     * 设置评论用户名字点击事件
     *
     * @param item
     * @param bean
     * @return
     */
    public SpannableString setClickableSpan(final String item, final UserBean bean) {
        final SpannableString string = new SpannableString(item);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // TODO:  评论用户名字点击事件
               // Toast.makeText(mContext, bean.getUserName(), Toast.LENGTH_SHORT).show();

            }


            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 设置显示的用户名文本颜色
                ds.setColor(0xff387dcc);
                ds.setUnderlineText(false);
            }
        };

        string.setSpan(span, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    /**
     * 定义一个点击的接口
     */
    public interface onItemClickListener {
        void onItemClick(int position, CommentsBean bean);

    }

    /**
     * 定义一个用于长按的接口
     */
    public interface onItemLongClickListener {
        void onItemLongClick(int position, CommentsBean bean);

    }


}