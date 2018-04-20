package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public abstract class Checksum {

    /**
     * Get an MD5 checksum as a byte array for a file.
     * @param file File to get the checksum for.
     * @return Byte array of the checksum.
     * @throws Exception
     */
    public byte[] getMD5Checksum(File file) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");

        try (InputStream is = new FileInputStream(file); DigestInputStream dis = new DigestInputStream(is, md)) {



        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] digest = md.digest();

        System.out.printf("Got checksum: %s.\n", digest);
        return digest;
    }
}
