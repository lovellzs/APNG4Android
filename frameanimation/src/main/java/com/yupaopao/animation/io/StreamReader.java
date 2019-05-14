package com.yupaopao.animation.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: pengfei.zhou
 * @CreateDate: 2019-05-11
 */
public class StreamReader extends FilterInputStream implements Reader {
    private static ThreadLocal<byte[]> __intBytes = new ThreadLocal<>();
    private int position;

    public StreamReader(InputStream in) {
        super(in);
    }

    @Override
    public byte peek() throws IOException {
        byte ret = (byte) read();
        position++;
        return ret;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int ret = super.read(b, off, len);
        position += Math.max(0, ret);
        return ret;
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        position = 0;
    }

    @Override
    public long skip(long n) throws IOException {
        long ret = super.skip(n);
        position += ret;
        return ret;
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public InputStream toInputStream() throws IOException {
        reset();
        return this;
    }
}