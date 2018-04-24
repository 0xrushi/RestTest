package com.example.h4x3d.myapplication;

public class GridItemModel {
    private String name;
    private String thumbnail;

    public GridItemModel(){}

    public GridItemModel(String name,String thumbnail){
        this.name=name;
        this.thumbnail=thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
