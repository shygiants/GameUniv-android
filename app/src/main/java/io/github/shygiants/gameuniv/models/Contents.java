package io.github.shygiants.gameuniv.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.shygiants.simplephotopicker.models.Photo;

/**
 * Created by SHYBook_Air on 2016. 1. 14..
 */
public class Contents {

    public static class Page {
        private String type;
        private String image;
        private int imageNum;
        private String description;

        private transient Photo photo;

        public Page(Photo photo, String description) {
            type = "image";
            this.photo = photo;
            // TODO: Trim unnecessary white spaces
            this.description = description;
        }
    }

    private String title;
    private String description;
    private List<Page> pages;

    private transient Photo titlePhoto;

    public Contents(String title, String desc, Photo titlePhoto) {
        this.title = title;
        description = desc;
        pages = new ArrayList<>();
        this.titlePhoto = titlePhoto;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public File[] getPhotoFiles() {
        int size = 1;
        for (Page page : pages)
            if (page.type.equals("image")) size++;
        File[] files = new File[size];
        files[0] = getFile(titlePhoto);
        int i = 1;
        for (Page page : pages)
            if (page.type.equals("image")) {
                files[i] = getFile(page.photo);
                page.imageNum = i;
                i++;
            }

        return files;
    }

    private File getFile(Photo photo) {
        return new File(photo.getImageUri().getPath());
    }
}
