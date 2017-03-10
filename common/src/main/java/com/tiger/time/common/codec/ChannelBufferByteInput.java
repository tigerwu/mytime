package com.tiger.time.common.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import java.io.IOException;

/**
 * Created by wuyihao on 17-3-2.
 */
public class ChannelBufferByteInput implements ByteInput {
    private final ByteBuf buffer;

    public ChannelBufferByteInput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (buffer.isReadable()) {
            return buffer.readByte() & 0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    @Override
    public int read(byte[] bytes, int i, int length) throws IOException {
        int available = available();
        int readlength = length;

        if(available == 0) {
            return -1;
        }

        readlength = Math.min(available, length);
        buffer.readBytes(bytes, i, readlength);
        return readlength;
    }


    @Override
    public int available() throws IOException {
        return this.buffer.readableBytes();
    }

    @Override
    public long skip(long length) throws IOException {
        int readable = buffer.readableBytes();
        long skiplength = length;
        if (readable < length) {
            skiplength = readable;
        }
        buffer.readerIndex((int) (buffer.readerIndex() + skiplength));
        return skiplength;

    }

    @Override
    public void close() throws IOException {

    }
}
