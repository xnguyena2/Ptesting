package com.example.heroku.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Util {
    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u',};

    private static Util instance;

    private Util(){
    }

    synchronized public static Util getInstance() {
        if (instance == null)
        {
            instance = new Util();
        }
        return instance;
    }

    public Map<String, String> synchronizedHashMap = Collections.synchronizedMap(new HashMap<String, String>());//WeakHashMap

    public Map<String, Integer> counterSession = Collections.synchronizedMap(new HashMap<String, Integer>());//new ConcurrentHashMap<String, String>();

    public Map<String, Integer> concurrentVoucherReuse = Collections.synchronizedMap(new HashMap<String, Integer>());//new ConcurrentHashMap<String, Integer>();

    public int getVoucher(String id){
        Integer value = concurrentVoucherReuse.get(id);
        if(value == null){
            return -1;
        }
        return value;
    }

    public Integer obserVoucher(String id) {
        //increase only when have same device and new Voucher ID
        return counterSession.compute(id, (k, v) -> (v == null) ? 1 : v + 1);
    }

    public void setVoucher(String id, Integer value) {
        concurrentVoucherReuse.put(id, value);
    }

    public boolean CleanMap(String id) {
        boolean exist = concurrentVoucherReuse.containsKey(id);
        if (exist) {
            Integer currentSession = counterSession.get(id);
            if (currentSession != null && currentSession > 1) {
                exist = false;
                counterSession.put(id, currentSession - 1);
            } else {
                exist = true;
            }
        } else {
            counterSession.remove(id);
        }
        synchronizedHashMap.remove(id);
        return exist;
    }

    public int CleanReuse(String id) {
        counterSession.remove(id);
        Integer reuse = concurrentVoucherReuse.remove(id);
        if (reuse == null)
            reuse = 0;
        return reuse;
    }

    public String GetMap(String id) {
        return synchronizedHashMap.compute(id, (k, v) -> (v == null) ? GenerateID() : v);
    }

    public String GenerateID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public String HashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public static char RemoveAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public String RemoveAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, RemoveAccent(sb.charAt(i)));
        }
        return sb.toString().toLowerCase();
    }

    public int DiffirentDays(Timestamp t1, Timestamp current) {
        if(t1 == null)
            return 9999;
        if(current == null)
            return -9999;
        return (int)((t1.getTime() - current.getTime()) / (1000 * 60 * 60 * 24));
    }
}