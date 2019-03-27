package com.yupaopao.animation.apng.chunk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yupaopao.animation.apng.ChainInputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 作用描述
 * @Author: pengfei.zhou
 * @CreateDate: 2019/3/27
 */
class Frame {
    FCTLChunk fctlChunk;
    IDATChunk idatChunk;
    int sequence_number;
    List<Chunk> otherChunks = new ArrayList<>();
    private static final byte[] sPNGSignatures = {(byte) 137, 80, 78, 71, 13, 10, 26, 10};
    private static final byte[] sPNGEndChunk = {0, 0, 0, 0, 0x49, 0x45, 0x4E, 0x44, (byte) 0xAE, 0x42, 0x60, (byte) 0x82};
    private Bitmap bitmap;


    byte[] toByteArray() {
        int size = sPNGSignatures.length;
        for (Chunk chunk : otherChunks) {
            size += chunk.getRawDataLength();
        }
        size += idatChunk.getRawDataLength();
        size += sPNGEndChunk.length;
        byte[] dst = new byte[size];
        int offset = 0;
        System.arraycopy(sPNGSignatures, 0, dst, offset, sPNGSignatures.length);
        offset += sPNGSignatures.length;
        for (Chunk chunk : otherChunks) {
            chunk.copy(dst, offset);
            offset += chunk.getRawDataLength();
        }
        idatChunk.copy(dst, offset);
        offset += idatChunk.getRawDataLength();

        System.arraycopy(sPNGEndChunk, 0, dst, offset, sPNGEndChunk.length);
        return dst;
    }

    InputStream toInputStream() {
        List<InputStream> inputStreams = new ArrayList<>();
        InputStream signatureStream = new ByteArrayInputStream(sPNGSignatures);
        inputStreams.add(signatureStream);
        for (Chunk chunk : otherChunks) {
            inputStreams.add(chunk.toInputStream());
        }
        inputStreams.add(idatChunk.toInputStream());
        inputStreams.add(new ByteArrayInputStream(sPNGEndChunk));
        return new ChainInputStream(inputStreams.toArray(new InputStream[0]));
    }

    public Bitmap toBitmap() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeStream(toInputStream());
        }
        return bitmap;
    }
}