package com.lzk.loveandroid.DB.Search;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class SearchHistoryBean extends LitePalSupport {
    private String keyWord;
    private Date updateDate;//更新时间

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
