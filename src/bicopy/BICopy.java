/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bicopy;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import configuration.Configuration;
import configuration.ConfigurationReader;
import java.io.File;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Hung.LM2
 */
public class BICopy {

    /**
     * @param args the command line arguments
     */
    public static Configuration config = ConfigurationReader.readConfig();

    public static void main(String[] args) {
        // TODO code application logic here
        Session session = null;
        Channel channel = null;
        File sourceFolder = new File(config.getSourceFolder());
        File[] files = sourceFolder.listFiles();
        try {
            JSch ssh = new JSch();

            session = ssh.getSession(config.getUsername(), config.getHost(), 22);
            session.setPassword(Decrypt(config.getPassword(), "xyz"));
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;

            for (int i = 0; i < files.length; i++) {
                if (config.getFileType().equals("*")) {
                    sftp.put(files[i].getPath(), config.getTargetFolder() + files[0].getName());
                }else if(files[i].getPath().substring(files[i].getPath().length()-3).equals(config.getFileType())){
                    sftp.put(files[i].getPath(), config.getTargetFolder() + files[0].getName());
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String Decrypt(String pwd, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] encrypted = DatatypeConverter.parseBase64Binary(pwd);
        String decrypted = new String(cipher.doFinal(encrypted));

        return decrypted;

    }

}
