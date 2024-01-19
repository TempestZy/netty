package com.test.testnetty;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

/**
 * 文件监视
 *
 * @author zhaoy
 * @date 2024-01-17 15:17:31
 */
@Service
public class FileWatcherService {

    private WatchService watchService;

    public void startWatching(String folderPath) throws IOException {
        Path path = Paths.get(folderPath);
        watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

        Thread watchThread = new Thread(() -> {
            try {
                processEvents();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        watchThread.setDaemon(true);
        watchThread.start();
    }

    public void stopWatching() {
        try {
            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processEvents() throws InterruptedException, IOException {
        while (true) {
            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path createdFilePath = (Path) event.context();
                    System.out.println("新增了文件：" + createdFilePath.getFileName());
                    // 处理文件新增
                    copyFile(createdFilePath);
                } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    Path deletedFilePath = (Path) event.context();
                    System.out.println("删除了文件：" + deletedFilePath.getFileName());
                    // 处理文件删除
                    deleteFile(deletedFilePath);
                }
            }

            key.reset();
        }
    }

    private void copyFile(Path filePath) throws IOException {
        // 实现文件复制逻辑，将文件复制到另一服务器
        // 使用Files.copy()方法进行文件复制
    }

    private void deleteFile(Path filePath) throws IOException {
        // 实现文件删除逻辑，在另一服务器中删除相应文件
        // 使用Files.delete()方法进行文件删除
    }
}
