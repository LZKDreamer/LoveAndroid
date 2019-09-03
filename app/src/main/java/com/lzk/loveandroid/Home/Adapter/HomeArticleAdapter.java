package com.lzk.loveandroid.Home.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzk.loveandroid.Home.Bean.Home.HomeArticle;
import com.lzk.loveandroid.R;

import java.util.EventListener;
import java.util.List;

/**
 * @author LiaoZhongKai
 * @date 2019/8/30.
 */
public class HomeArticleAdapter extends BaseQuickAdapter<HomeArticle.Datas,BaseViewHolder> {

    public HomeArticleAdapter(int layoutResId, @Nullable List<HomeArticle.Datas> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeArticle.Datas item) {
        //置顶
        if (helper.getLayoutPosition()<4){
            helper.getView(R.id.article_item_tag_tv).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.article_item_tag_tv).setVisibility(View.GONE);
        }
        //作者
        helper.setText(R.id.article_item_author_tv,item.getAuthor());
        //时间
        helper.setText(R.id.article_item_time_tv,item.getNiceDate());
        //标题
        helper.setText(R.id.article_item_title_tv,item.getTitle());
        //类别
        helper.setText(R.id.article_item_chapter_tv,item.getChapterName()+"/"+item.getSuperChapterName());
        //收藏
        helper.getView(R.id.article_item_collect_iv).setSelected(item.isCollect());
        helper.addOnClickListener(R.id.article_item_collect_iv);

    }
}
