package com.mvvm.demo.adapter;

import android.content.Context;

import com.mvvm.demo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

/**
 * Created by hzy on 2019/1/24
 *
 * @author Administrator
 */
public class PubAdapter extends CommonAdapter<String> {

    public PubAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_pub, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String title, int position) {
        holder.setText(R.id.tv_project, StringEscapeUtils.unescapeHtml4(title));
    }
}
