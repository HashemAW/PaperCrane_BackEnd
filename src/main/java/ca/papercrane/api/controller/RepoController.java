package ca.papercrane.api.controller;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class RepoController {

    /**
     * Lists out the every file in src
     *
     * @return fileSystem The file system in a json object list
     */
    @GetMapping("/api/getFolderStructure/{repoName}")
    public List<Map<String, Object>> getFiles(@PathVariable String repoName) {
        File folder = new File("Projects/" + repoName);
        List<Map<String, Object>> fileSystem = new ArrayList<>();
        exploreFolder(folder, fileSystem, folder.getName());
        return fileSystem;
    }

    /**
     * Recursively find folders and files to put them in a List of key value pairs.
     *
     * @param folder     The starting folder.
     * @param fileSystem The file system result after finding all folders and files.
     * @param folderPath The path to the folder
     */
    private void exploreFolder(File folder, List<Map<String, Object>> fileSystem, String folderPath) {
        File[] listOfFiles = folder.listFiles();
        Map<String, Object> currentFolder = new HashMap<>();
        currentFolder.put("type", "folder");
        currentFolder.put("name", folder.getName());
        currentFolder.put("folderPath", "Projects/" + folderPath);

        // List out the children value with it's own ArrayList
        List<Map<String, Object>> children = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Map<String, Object> file = new HashMap<>();
                file.put("type", "file");
                file.put("name", listOfFiles[i].getName());
                file.put("folderPath", "Projects/" + folderPath);
                children.add(file);
            } else {
                String subFolderPath = folderPath + "/" + listOfFiles[i].getName();
                exploreFolder(listOfFiles[i], children, subFolderPath);
            }
        }
        currentFolder.put("children", children);
        fileSystem.add(currentFolder);
    }

    /**
     * Creates a new folder to disk
     *
     * @param folderName The name of the new folder, by default if name is not provided it defaults to "New Folder"
     * @param folderPath The path of the folder ex: Project/{folder name}
     */
    @PostMapping("/api/createFolder")
    public void createFolder(@RequestParam(value = "name", defaultValue = "New Folder") String folderName, @RequestParam("folderPath") String folderPath) {
        createUniqueFolder(folderName, folderPath);
    }

    /**
     * Creates a unique name for the folder by appending the (#) where # would increment if the folder exists
     *
     * @param baseName   The base name for the new folder
     * @param folderPath The path that the folder will reside in
     */
    private void createUniqueFolder(String baseName, String folderPath) {
        File folder = new File(folderPath, baseName);
        int count = 1;
        while (folder.exists()) {
            baseName = baseName + " (" + count + ")";
            folder = new File(baseName);
            count++;
        }
        folder.mkdir();
    }

    /**
     * Removes the selected folder from disk
     *
     * @param folderPath The path to the folder
     * @return A Response Entity with a HTTP status code and a message iddicating whether the folder has been
     * successfully deleted, the folder was not found, or failed to delete
     */
    @DeleteMapping("/api/deleteFolder")
    public ResponseEntity<String> deleteFolder(@RequestParam("folderPath") String folderPath) {
        File folder = new File(folderPath);

        if (!folder.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Folder not found");
        }

        try {
            // FileUtils recursively deletes all contents from the folder and the folder itself
            FileUtils.deleteDirectory(folder);
            return ResponseEntity.ok("Folder deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting folder");
        }
    }

    /**
     * Renames the folder on the disk
     *
     * @param folderPath The path of the folder that's being changed
     * @param newName    The new name of the folder
     */
    @PutMapping("/api/renameFolder")
    public void renameFolder(@RequestParam("folderPath") String folderPath, @RequestParam("newName") String newName) {
        File folder = new File(folderPath);
        File renamedFolder = new File(folder.getParentFile(), newName);
        folder.renameTo(renamedFolder);
    }

    /**
     * Saves the file from the client onto disk
     *
     * @param file       The file to be uploaded
     * @param folderPath The destination path the file will be stored in
     * @param overwrite  A boolean that determines whether the file will be overwritten or not
     * @return A ResponseEntity with the HTTP status code and a message indicating whether the file already exists,
     * upload failed, or file has been successfully uploaded
     */
    @PostMapping("/api/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("folderPath") String folderPath, @RequestParam(value = "overwrite", required = false) Boolean overwrite) {

        Path path = Paths.get(folderPath + "/" + file.getOriginalFilename());
        // return a response indicating the file already exists
        if (Files.exists(path) && overwrite == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists. Do you want to continue?");
        }
        try {
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    /**
     * Removes the file from the disk
     *
     * @param fileName   The name of the file that is to be deleted
     * @param folderPath The path to the file
     * @return A ResponseEntity with the HTTP status code and a message indicating whether the file has been
     * successfully deleted or failed to delete
     */
    @DeleteMapping("/api/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName, @RequestParam("folderPath") String folderPath) {
        String fullPath = folderPath + "/" + fileName;
        File file = new File(fullPath);

        if (file.delete()) {
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file");
        }
    }

    /**
     * Renames the selected file on the disk
     *
     * @param filePath The full path to the file
     * @param newName  The new name that the file is going to be renamed to
     */
    @PutMapping("/api/renameFile")
    public void renameFile(@RequestParam("filePath") String filePath, @RequestParam("newName") String newName) {
        File file = new File(filePath);
        File renamedFile = new File(file.getParentFile(), newName);
        file.renameTo(renamedFile);
    }

    /**
     * Retrieves a file from the disk and sends it to the client for download
     *
     * @param fileName   The name of the file
     * @param folderPath The path to the file
     * @return The file resource as a ResponseEntity
     * @throws IOException If there was an error reading the file from the disk
     */
    @PostMapping("/api/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName, @RequestParam("folderPath") String folderPath) throws IOException {
        Path path = Paths.get(folderPath + "/" + fileName);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        // Update header to include the name of the attachment
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }
}
