package com.lailaps.download;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class ResourceIDSafekeeper {

    private static final Logger LOG = Logger.getLogger(ResourceIDSafekeeper.class);
    private static final String FILE_NAME = ".lailaps";
    private Set<String> downloadIDs = new HashSet<>();
    private final File saveFile;

    ResourceIDSafekeeper(String rootPath) {
        saveFile = new File(rootPath + File.separator + FILE_NAME);
    }

    void loadIDsFromFile() {
        try {
            if (saveFile.exists()) {
                downloadIDs = new HashSet<>(FileUtils.readLines(saveFile, "utf-8"));
            }
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    void saveIDsToFile() {
        try {
            if (saveFile.exists()) {
                //we can't write to a hidden file, so unhide it first.
                unhideSaveFile();
            }
            FileUtils.writeLines(saveFile, downloadIDs);
            hideSaveFile();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private void unhideSaveFile() {
        try {
            Files.setAttribute(saveFile.toPath(), "dos:hidden", false);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private void hideSaveFile() {
        try {
            Files.setAttribute(saveFile.toPath(), "dos:hidden", true);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public void add(String id) {
        downloadIDs.add(id);
    }

    boolean hasID(String resourceID) {
        return downloadIDs.contains(resourceID);
    }
}
