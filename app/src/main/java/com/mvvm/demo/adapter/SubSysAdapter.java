package com.mvvm.demo.adapter;

import android.content.Context;

import com.mvvm.demo.R;
import com.mvvm.demo.entity.SystemDataChildBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by hzy on 2019/1/24
 * SubSysAdapter
 *
 * @author Administrator
 */
public class SubSysAdapter extends CommonAdapter<SystemDataChildBean> {

    private int itemCount = 0;

    public SubSysAdapter(Context context, List<SystemDataChildBean> datas) {
        super(context, R.layout.item_navi_grid, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SystemDataChildBean bean, int position) {
        holder.setText(R.id.tv_project, bean.getName())
                .setBackgroundRes(R.id.tv_project, bean.getChecked() ?
                        R.drawable.shape_solid_blue_corner10 : R.drawable.shape_blue_corner10_bg)
                .setTextColorRes(R.id.tv_project, bean.getChecked() ? R.color.white :
                        R.color.c_4285F4);
    }

    public void setItemCount(int count) {
        this.itemCount = count;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (itemCount > 0 && itemCount <= getDatas().size()) {
            return itemCount;
        }
        return super.getItemCount();
    }

    public void setSelectItem(int index) {
        for (SystemDataChildBean bean : getDatas()) {
            bean.setChecked(false);
        }
        getDatas().get(index).setChecked(true);
        notifyDataSetChanged();
    }
}
