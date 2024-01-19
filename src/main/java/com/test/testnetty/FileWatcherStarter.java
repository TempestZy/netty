package com.test.testnetty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 文件监视器
 *
 * @author zhaoy
 * @date 2024-01-17 15:22:23
 */
@Component
public class FileWatcherStarter implements CommandLineRunner {

    @Autowired
    private FileWatcherService fileWatcherService;

    @Override
    public void run(String... args) throws Exception {
        String folderPathToWatch = "D:\\test";
        fileWatcherService.startWatching(folderPathToWatch);
    }
}
