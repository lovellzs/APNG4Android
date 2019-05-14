package com.yupaopao.animation.apng.io;

import android.text.TextUtils;

import com.yupaopao.animation.io.StreamReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: APNG4Android
 * @Author: pengfei.zhou
 * @CreateDate: 2019-05-13
 */
public class APNGReader extends StreamReader {
    public APNGReader(InputStream in) {
        super(in);
    }

    public int readInt() throws IOException {
        byte[] buf = ensureBytes();
        read(buf, 0, 4);
        return buf[3] & 0xFF |
                (buf[2] & 0xFF) << 8 |
                (buf[1] & 0xFF) << 16 |
                (buf[0] & 0xFF) << 24;
    }

    public short readShort() throws IOException {
        byte[] buf = ensureBytes();
        read(buf, 0, 2);
        return (short) (buf[1] & 0xFF |
                (buf[0] & 0xFF) << 8);
    }

    /**
     * @return read FourCC and match chars
     */
    public boolean matchFourCC(String chars) throws IOException {
        if (TextUtils.isEmpty(chars) || chars.length() != 4) {
            return false;
        }
        int fourCC = readFourCC();
        for (int i = 0; i < 4; i++) {
            if (((fourCC >> (i * 8)) & 0xff) != chars.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public int readFourCC() throws IOException {
        byte[] buf = ensureBytes();
        read(buf, 0, 4);
        return buf[0] & 0xff | (buf[1] & 0xff) << 8 | (buf[2] & 0xff) << 16 | (buf[3] & 0xff) << 24;
    }

}
