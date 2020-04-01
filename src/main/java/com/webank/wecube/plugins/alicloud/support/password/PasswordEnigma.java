package com.webank.wecube.plugins.alicloud.support.password;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author howechen
 */
public interface PasswordEnigma {

    String DEFAULT_CIPHER = "CIPHER_A";
    String DEFAULT_ALGO = "PKCS5";
    Charset CHAR_SET = StandardCharsets.UTF_8;

    Map<String, String> CIPHER_MAP = new HashMap<String, String>() {{
        put(DEFAULT_CIPHER, "{cipher_a}");
    }};

    Map<String, String> ALGO_MAP = new HashMap<String, String>() {{
        // in Java, PKCS7Padding is the same as AES/CBC/PKCS5Padding (with same implementation which support larger block size)
        // see https://stackoverflow.com/questions/10193567/java-security-nosuchalgorithmexceptioncannot-find-any-provider-supporting-aes-e
        put(DEFAULT_ALGO, "AES/CBC/PKCS5Padding");
    }};

    /**
     * Encrypt password with composition of cipher, MD5 and AES encoded string
     *
     * @param guid            guid from core
     * @param seed            seed from core
     * @param password        password (core given or generated)
     * @param specifiedCipher user specified cipher or system's default cipher
     * @return encoded string with cipher + MD5 and AES encoded string
     * @throws CryptoException CryptoException
     */
    static String encryptPassword(String guid, String seed, String password, String specifiedCipher, String algo) throws CryptoException {
        String cipherKey = specifiedCipher;

        if (StringUtils.isEmpty(specifiedCipher)) {
            cipherKey = DEFAULT_CIPHER;
        }

        String md5EncodedStr = md5HexEncode(guid + seed).substring(0, 16);

        if (StringUtils.isEmpty(md5EncodedStr)) {
            throw new CryptoException("Error when encoding md5 string");
        }

        String encryptedPassword = aesEncrypt(md5EncodedStr, password, algo);

        return CIPHER_MAP.get(cipherKey) + encryptedPassword;

    }


    /**
     * Decrypt encryptedPassword with composition of cipher, MD5 and AES encoded string
     *
     * @param guid              guid from core
     * @param seed              seed from core
     * @param encryptedPassword encryptedPassword (core given or generated) with cipher prefix
     * @param specifiedCipher   user specified cipher or system's default cipher
     * @return encoded string with cipher + MD5 and AES encoded string
     * @throws CryptoException CryptoException
     */
    static String decryptPassword(String guid, String seed, String encryptedPassword, String specifiedCipher, String algo) throws CryptoException {
        String cipherKey = specifiedCipher;

        if (StringUtils.isEmpty(specifiedCipher)) {
            cipherKey = DEFAULT_CIPHER;
        }

        final String cipherPrefix = CIPHER_MAP.get(cipherKey);

        if (StringUtils.isEmpty(cipherPrefix)) {
            throw new CryptoException(String.format("Cannot find cipherPrefix from given cipher: [%s]", specifiedCipher));
        }

        final int prefixLength = cipherPrefix.length();
        if (encryptedPassword.length() <= prefixLength) {
            throw new CryptoException("The encryptedPassword's size is wrong.");
        }

        if (!StringUtils.equals(cipherPrefix, encryptedPassword.substring(0, prefixLength))) {
            throw new CryptoException("The given data doesn't have specified cipher prefix.");
        }

        final String encryptedPasswordWithoutCipherPrefix = encryptedPassword.substring(prefixLength);

        String md5EncodedStr = md5HexEncode(guid + seed).substring(0, 16);

        if (StringUtils.isEmpty(md5EncodedStr)) {
            throw new CryptoException("Error when encoding md5 string");
        }

        return aesDecrypt(md5EncodedStr, encryptedPasswordWithoutCipherPrefix, algo);

    }

    /**
     * Encrypt string use MD5 algorithm
     *
     * @param input input string
     * @return encoded string
     */
    static String md5HexEncode(String input) {
        return DigestUtils.md5Hex(input);
    }

    /**
     * Encrypt using AES algorithm
     *
     * @param key           key, which is md5 encoded substring ( string with range of 0 to 16 exclusive)
     * @param content       given password
     * @param specifiedAlgo specified algorithm
     * @return encoded string
     * @throws CryptoException CryptoException
     */
    static String aesEncrypt(String key, String content, String specifiedAlgo) throws CryptoException {

        String algo = ALGO_MAP.get(DEFAULT_ALGO);

        // support user specified algorithm, default is "AES/CBC/PKCS5Padding"
        if (StringUtils.isNotEmpty(specifiedAlgo)) {
            final String val = ALGO_MAP.get(specifiedAlgo);
            if (null == val) {
                throw new CryptoException("Cannot support such algorithm");
            } else {
                algo = val;
            }
        }

        // ensure to fill the key to a 16 bytes size long array
        byte[] keyBytes = Arrays.copyOf(key.getBytes(CHAR_SET), 16);

        // encode data
        byte[] result;
        try {
            final Cipher cipher = Cipher.getInstance(algo);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            // use keyBytes instead of a new empty byte array to be the initial vectors
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(content.getBytes(CHAR_SET));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new CryptoException(e.getMessage());
        }
        return HexUtils.toHexString(result);
    }

    /**
     * Decrypt using AES algorithm
     *
     * @param key           key, which is md5 encoded substring ( string with range of 0 to 16 exclusive)
     * @param content       given password
     * @param specifiedAlgo specified algorithm
     * @return encoded string
     * @throws CryptoException CryptoException
     */
    static String aesDecrypt(String key, String content, String specifiedAlgo) throws CryptoException {

        String algo = ALGO_MAP.get(DEFAULT_ALGO);

        // support user specified algorithm, default is "AES/CBC/PKCS5Padding"
        if (StringUtils.isNotEmpty(specifiedAlgo)) {
            final String val = ALGO_MAP.get(specifiedAlgo);
            if (null == val) {
                throw new CryptoException("Cannot support such algorithm");
            } else {
                algo = val;
            }
        }

        // ensure to fill the key to a 16 bytes size long array
        byte[] keyBytes = Arrays.copyOf(key.getBytes(CHAR_SET), 16);

        String result;
        final byte[] decodedByteArray = HexUtils.fromHexString(content);
        try {
            Cipher cipher = Cipher.getInstance(algo);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            // use keyBytes instead of a new empty byte array to be the initial vectors
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            result = new String(cipher.doFinal(decodedByteArray), CHAR_SET);
        } catch (NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            throw new CryptoException(e.getMessage());
        }
        return result;
    }


}
