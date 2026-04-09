package com.oxysystem.general.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CachedServletOutputStream extends ServletOutputStream {
    private final ByteArrayOutputStream cachedOutputStream;
    private final OutputStream originalOutputStream;

    public CachedServletOutputStream(ByteArrayOutputStream cachedOutputStream, OutputStream originalOutputStream) {
        this.cachedOutputStream = cachedOutputStream;
        this.originalOutputStream = originalOutputStream;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        // No-op
    }

    @Override
    public void write(int b) throws IOException {
        cachedOutputStream.write(b);
        originalOutputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        cachedOutputStream.flush();
        originalOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        cachedOutputStream.close();
        originalOutputStream.close();
    }
}
