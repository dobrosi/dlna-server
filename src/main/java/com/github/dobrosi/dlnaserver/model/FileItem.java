package com.github.dobrosi.dlnaserver.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.Comparator.comparing;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileItem implements Serializable, Comparable<FileItem> {
    @Override
    public int compareTo(FileItem fileItem) {
        return comparing(FileItem::getType)
            .thenComparing(
                FileItem::getTitle, Collator.getInstance(new Locale("hu", "HU")))
            .compare(this, fileItem);
    }

    public enum Type {
        DIRECTORY,
        FILE
    }

    private Type type;

    private String title;

    private String imageUrl;

    private String url;
}
