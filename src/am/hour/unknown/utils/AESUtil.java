package am.hour.unknown.utils;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
/**
 * AES�ԳƼ����㷨��java6ʵ�֣�bouncycastleҲ֧��AES�ԳƼ����㷨
 * ���ǿ�����AES�㷨ʵ��Ϊ�ο������RC2��RC4��Blowfish�㷨��ʵ��
 * @author kongqz
 * */
public class AESUtil {
	
	
	private void test() throws InvalidKeyException{
//      String content = "test";
//      String password = "12345678";
//      //����
//      System.out.println("����ǰ��" + content);
//      byte[] encryptResult = encrypt(content, password);
//      System.out.println(encryptResult);
//      //����
//      byte[] decryptResult = decrypt(encryptResult,password);
//      System.out.println("���ܺ�" + new String(decryptResult));
      
      
      
      
      String content_2 = "test";
      String password_2 = "12345678";
      //����
      System.out.println("����ǰ��" + content_2);
      byte[] encryptResult_2 = encrypt(content_2, password_2);
      String encryptResultStr_2 = parseByte2HexStr(encryptResult_2);
      System.out.println("���ܺ�" + encryptResultStr_2);
      //����
      byte[] decryptFrom_2 = parseHexStr2Byte(encryptResultStr_2);
      byte[] decryptResult_2 = decrypt(decryptFrom_2,password_2);
      System.out.println("���ܺ�" + new String(decryptResult_2));
	}

    /**
     * ����
     * 
     * @param content ��Ҫ���ܵ�����
     * @param password  ��������
     * @return
     * @throws java.security.InvalidKeyException 
     */
    public static byte[] encrypt(String content, String password) throws java.security.InvalidKeyException {
            try {           
                    KeyGenerator kgen = KeyGenerator.getInstance("AES");
                    kgen.init(128, new SecureRandom(password.getBytes()));
                    SecretKey secretKey = kgen.generateKey();
                    byte[] enCodeFormat = secretKey.getEncoded();
                    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                    Cipher cipher = Cipher.getInstance("AES");// ����������
                    byte[] byteContent = content.getBytes("utf-8");
                    cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��
                    byte[] result = cipher.doFinal(byteContent);
                    return result; // ����
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
            } catch (InvalidKeyException e) {
                    e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
            } catch (BadPaddingException e) {
                    e.printStackTrace();
            }
            return null;
    }
    /**����
     * @param content  ����������
     * @param password ������Կ
     * @return
     * @throws java.security.InvalidKeyException 
     */
    public static byte[] decrypt(byte[] content, String password) throws java.security.InvalidKeyException {
            try {
                     KeyGenerator kgen = KeyGenerator.getInstance("AES");
                     kgen.init(128, new SecureRandom(password.getBytes()));
                     SecretKey secretKey = kgen.generateKey();
                     byte[] enCodeFormat = secretKey.getEncoded();
                     SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");            
                     Cipher cipher = Cipher.getInstance("AES");// ����������
                    cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��
                    byte[] result = cipher.doFinal(content);
                    return result; // ����
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
            } catch (InvalidKeyException e) {
                    e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
            } catch (BadPaddingException e) {
                    e.printStackTrace();
            }
            return null;
    }
    /**��������ת����16����
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
                    String hex = Integer.toHexString(buf[i] & 0xFF);
                    if (hex.length() == 1) {
                            hex = '0' + hex;
                    }
                    sb.append(hex.toUpperCase());
            }
            return sb.toString();
    }
    /**��16����ת��Ϊ������
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1)
                    return null;
            byte[] result = new byte[hexStr.length()/2];
            for (int i = 0;i< hexStr.length()/2; i++) {
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
                    result[i] = (byte) (high * 16 + low);
            }
            return result;
    }

}
//����̨��������
//ԭ�ģ�AES
//��Կ��PZ2cqR0GgybDSHtJqlrM59A5f2wLBapFTjidt1AADJg=
//���ܺ�UVtlIlbQIufti69QYGyYMw==
//���ܺ�AES
