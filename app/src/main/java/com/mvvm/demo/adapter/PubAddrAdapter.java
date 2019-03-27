package com.mvvm.demo.adapter;

import android.content.Context;

import com.mvvm.demo.R;
import com.mvvm.demo.entity.PubAddrListChild;
import com.mvvm.demo.listener.OnCollectListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

/**
 * Created by hzy on 2019/1/24
 *
 * @author Administrator
 */
public class PubAddrAdapter extends CommonAdapter<PubAddrListChild> {


    private OnCollectListener mOnCollectListener;

    public void setmOnCollectListener(OnCollectListener mOnCollectListener) {
        this.mOnCollectListener = mOnCollectListener;
    }

    public PubAddrAdapter(Context context, List<PubAddrListChild> datas) {
        super(context, R.layout.item_pub_addr, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PubAddrListChild bean, int position) {
        holder.setText(R.id.tv_project, StringEscapeUtils.unescapeHtml4(bean.getTitle()))
                .setText(R.id.tv_time, "时间:" + bean.getNiceDate())
                .setImageResource(R.id.imv_like, bean.isCollect() ? R.drawable.icon_like : R.drawable.icon_unlike)
                .setOnClickListener(R.id.imv_like, v -> {
                    mOnCollectListener.onCollect(bean.isCollect(), bean.getId(), position);
                });
    }
}
