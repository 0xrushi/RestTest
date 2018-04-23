package com.example.h4x3d.myapplication;

public class GridItemModel {
    private String name;
    private int thumbnail;

    public GridItemModel(String name,int thumbnail){
        this.name=name;
        this.thumbnail=thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
