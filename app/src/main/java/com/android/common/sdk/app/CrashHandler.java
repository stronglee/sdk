package com.android.common.sdk.app;

import com.android.common.sdk.utils.FileUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by stronglee on 16/1/25.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String PATH_NAME = "crash.txt";

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        FileUtils.writeString(PATH_NAME, stringWriter.getBuffer().toString());
        printWriter.println();
        printWriter.close();
        ex.printStackTrace();
    }
}
