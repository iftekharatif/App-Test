package com.test.sample;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AutomatedBackup {

    private static final String FTP_SERVER = "ftp.gnu.org";
    private static final int FTP_PORT = 21;
    private static final String USERNAME = "";  // Default anonymous access
    private static final String PASSWORD = "";  // No password needed for anonymous access
    private static final String LOCAL_FILE_PATH = "C:\\Users\\mahbo\\git\\repository\\AppAUT\\src\\test\\java\\com\\test\\sample\\application_health.log";  // Update this path
    private static final String REMOTE_FILE_PATH = "\\files\\gnu\\";  // Remote file path on the server

    public static void file_backup() {
        FTPClient ftpClient = new FTPClient();

        try {
            // Connect to the FTP server
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // File to upload
            File localFile = new File(LOCAL_FILE_PATH);

            // Upload the file
            try (FileInputStream inputStream = new FileInputStream(localFile)) {
                boolean success = ftpClient.storeFile(REMOTE_FILE_PATH, inputStream);
                if (success) {
                    System.out.println("File uploaded successfully.");
                } else {
                    System.out.println("File upload failed.");
                    Assert.fail();
                }
            }

            // Logout and disconnect
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
    @Test
    public void test_backup() {
    	file_backup();
    }

}


	

