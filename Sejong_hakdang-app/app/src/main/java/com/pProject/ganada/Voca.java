package com.pProject.ganada;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Voca {
    @PrimaryKey(autoGenerate = true)
    public int id;

    //사물인식, 텍스트인식 타입
    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "picture_uri")
    public String picture_uri;

    @ColumnInfo(name = "ex_sentence")
    public String ex_sentence;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture_uri() {
        return picture_uri;
    }

    public void setPicture_uri(String picture_uri) {
        this.picture_uri = picture_uri;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getExSentence() {
        return ex_sentence;
    }

    public void setExSentence(String ex_sentence) {
        this.ex_sentence = ex_sentence;
    }

    @Override
    public String toString() {
        return "Voca{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", word='" + word + '\'' +
                ", picture_uri='" + picture_uri + '\'' +
                ", ex_sentence='" + ex_sentence + '\'' +
                '}';
    }
}
