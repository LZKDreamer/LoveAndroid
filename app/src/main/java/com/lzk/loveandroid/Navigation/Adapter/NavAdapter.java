package com.lzk.loveandroid.Navigation.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.Navigation.Bean.NavigationBean;
import com.lzk.loveandroid.R;
import com.lzk.loveandroid.Utils.CommonUtil;
import com.lzk.loveandroid.main.ArticleDetailActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public class NavAdapter extends BaseQuickAdapter<NavigationBean.DataBean, BaseViewHolder> {

    public NavAdapter(int layoutResId, @Nullable List<NavigationBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NavigationBean.DataBean item) {
        helper.setText(R.id.nav_item_title_tv,item.getName());
        TagFlowLayout mTagFLowLayout = helper.getView(R.id.nav_item_flow_layout);
        List<NavigationBean.DataBean.ArticlesBean> list = item.getArticles();
        mTagFLowLayout.setAdapter(new TagAdapter<NavigationBean.DataBean.ArticlesBean>(list) {
            @Override
            public View getView(FlowLayout parent, int position, NavigationBean.DataBean.ArticlesBean articlesBean) {
                TextView textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flow_tag,mTagFLowLayout,false)
                        .findViewById(R.id.flow_tag_tv);
                textView.setText(articlesBean.getTitle());
                textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(),R.color.color_bg_light_grey));
                textView.setTextColor(CommonUtil.getFlowTagBackgroudColor());
                return textView;
            }
        });

        mTagFLowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = ArticleDetailActivity.newIntent(parent.getContext(),
                        list.get(position).getTitle(),list.get(position).getLink());
                parent.getContext().startActivity(intent);
                return true;
            }
        });
    }
}
