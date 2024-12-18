package com.ruoyi.project.parse.filefetcher;

import java.util.List;

/**
 * @author chenl
 */
public interface FileFetcher {
    List<String> fetchFiles();
    String fetchFile();
}
