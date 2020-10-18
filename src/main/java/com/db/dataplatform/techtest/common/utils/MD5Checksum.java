package com.db.dataplatform.techtest.common.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Checksum {

    private MD5Checksum() {
        // Intentionally left blank
    }

    public static String calculateMD5Checksum(String body) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(body.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter
                .printHexBinary(digest).toLowerCase();
    }

}
