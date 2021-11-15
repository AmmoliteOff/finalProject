package org.dataart.javaschool.servise;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathServise {
    public static Path getPath(MultipartFile file) {
        Path articlesLocation = Paths.get("src/main/resources/static/uploads");
        Path absoluteDestination = articlesLocation.normalize().toAbsolutePath();
        return absoluteDestination;
    }
}