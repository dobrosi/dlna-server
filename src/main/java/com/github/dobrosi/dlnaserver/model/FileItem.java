package com.github.dobrosi.dlnaserver.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileItem implements Serializable {
    public enum Type {
        DIRECTORY,
        FILE
    }

    private Type type;

    private String title;

    private String imageUrl;

    private String url;
}
