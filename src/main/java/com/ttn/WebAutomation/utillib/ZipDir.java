package com.ttn.WebAutomation.utillib;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This Java program demonstrates how to compress a directory in ZIP format.
 *
 * @author TTN
 */
public class ZipDir extends SimpleFileVisitor<Path> {
    private static ZipOutputStream zos;
    private Path sourceDir = null;

    public ZipDir(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    @Override
    public @NotNull FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
        try {
            // only copy files, no symbolic links
            if (attributes.isSymbolicLink()) {
                return FileVisitResult.CONTINUE;
            }
            Path targetFile = sourceDir.relativize(file);
            zos.putNextEntry(new ZipEntry(targetFile.toString()));
            byte[] bytes = Files.readAllBytes(file);
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return FileVisitResult.CONTINUE;
    }

    public static void zip(String dirPath) {
        Path sourceDir = Paths.get(dirPath.substring(0, dirPath.length() - 5));
        try {
            File theDir = new File(dirPath);
            File dest = new File(dirPath.substring(0, dirPath.length() - 5));
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            FileUtils.copyFileToDirectory(theDir, dest);


            String zipFileName = dest.toString().concat(".zip");
            zos = new ZipOutputStream(new FileOutputStream(zipFileName));

            Files.walkFileTree(sourceDir, new ZipDir(sourceDir));
            zos.close();
        } catch (Exception ex) {
            System.err.println("Error creating zip file: " + ex);
        }
    }

}