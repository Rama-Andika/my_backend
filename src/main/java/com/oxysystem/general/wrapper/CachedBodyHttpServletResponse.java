package com.oxysystem.general.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedBody = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(new OutputStreamWriter(cachedBody, StandardCharsets.UTF_8), true);
    private final ServletOutputStream outputStream = new CachedBodyServletOutputStream(cachedBody);

    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public String getResponseBody() throws UnsupportedEncodingException {
        writer.flush(); // Ensure all data is written
        return cachedBody.toString(String.valueOf(StandardCharsets.UTF_8));
    }

    public void copyBodyToResponse() throws IOException {
        byte[] responseBodyBytes = cachedBody.toByteArray();
        getResponse().getOutputStream().write(responseBodyBytes);
        getResponse().getOutputStream().flush();
    }

    private static class CachedBodyServletOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream cachedBody;

        public CachedBodyServletOutputStream(ByteArrayOutputStream cachedBody) {
            this.cachedBody = cachedBody;
        }

        @Override
        public void write(int b) {
            cachedBody.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // No implementation needed
        }
    }
}
