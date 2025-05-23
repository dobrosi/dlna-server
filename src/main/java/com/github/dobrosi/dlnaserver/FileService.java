package com.github.dobrosi.dlnaserver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import com.github.dobrosi.dlnaserver.configuration.FileServerConfiguration;
import com.github.dobrosi.dlnaserver.model.FileItem;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static java.util.Comparator.comparing;

@Data
@Service
@ManagedResource(
        objectName = "FileBeans:name=FileService",
        description = "FileService Bean")
public class FileService {
    private StreamService streamService;
    private FileServerConfiguration fileServerConfiguration;

    @Autowired
    public FileService(FileServerConfiguration fileServerConfiguration, StreamService streamService) {
        this.fileServerConfiguration = fileServerConfiguration;
        this.streamService = streamService;
    }

    public List<FileItem> list(String path) throws IOException {
        path = calculatePath(path);
        Path fullPath = getFullPath(path);
        if (!fullPath.startsWith(fileServerConfiguration.getPath())) {
            fullPath = getFullPath("/");
        }
        ArrayList<FileItem> fileItems = new ArrayList<>();
        if (!fullPath.equals(fileServerConfiguration.getPath())) {
            FileItem parent = new FileItem();
            parent.setTitle("..");
            parent.setType(FileItem.Type.DIRECTORY);
            parent.setUrl(path.substring(0, path.lastIndexOf("/")));
            fileItems.add(parent);
        }
        try (Stream<Path> stream = Files.list(fullPath)) {
            fileItems.addAll(stream.map(this::toFileItem)
                    .sorted((f1, f2) -> comparing(FileItem::getType)
                            .thenComparing(
                                    FileItem::getTitle, Collator.getInstance(new Locale("hu", "HU")))
                            .compare(f1, f2))
                    .toList());
        }
        return fileItems;
    }

    public void upload(InputStream inputStream, String filename, String path, String parentPath) throws IOException {
        path = calculatePath(path);
        parentPath = calculatePath(parentPath);
        Path targetPath = getFullPath(parentPath, path);
        Files.createDirectories(targetPath);
        Files.copy(inputStream, targetPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
    }

    private static String calculatePath(String path) {
        path = Strings.isEmpty(path) ? "/" : path;
        path = path.startsWith("/") ? path : "/" + path;
        return path;
    }

    private Path getFullPath(String path) {
        return Paths.get(fileServerConfiguration.getPath()
                        .toString() + path)
                .normalize();
    }

    private Path getFullPath(String parentPath, String path) {
        return getFullPath(parentPath + path);
    }

    private FileItem toFileItem(Path path) {
        FileItem fileItem = new FileItem();
        fileItem.setTitle(path.getFileName()
                .toString());
        fileItem.setType(path.toFile()
                .isDirectory() ? FileItem.Type.DIRECTORY : FileItem.Type.FILE);
        fileItem.setUrl(path.toString()
                .replace(
                        fileServerConfiguration.getPath()
                                .toString(), ""));
        return fileItem;
    }

    public ResponseEntity<StreamingResponseBody> prepareDownloadContent(
            String url, String rangeHeader, boolean inline) {
        return streamService.prepareDownloadContent(getFullPath(url).toFile(), rangeHeader, inline);
    }
}
