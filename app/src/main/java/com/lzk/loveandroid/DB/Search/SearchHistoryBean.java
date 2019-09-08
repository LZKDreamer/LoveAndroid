package com.lzk.loveandroid.DB.Search;

import org.litepal.crud.LitePalSupport;

public class SearchHistoryBean extends LitePalSupport {
    private String keyWord;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
