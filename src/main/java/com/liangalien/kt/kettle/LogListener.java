package com.liangalien.kt.kettle;

import com.liangalien.kt.util.LogThread;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.logging.KettleLoggingEvent;
import org.pentaho.di.core.logging.KettleLoggingEventListener;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.Job;
import org.pentaho.di.trans.Trans;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class LogListener implements KettleLoggingEventListener {

    private Trans trans;
    private Job job;

    private String name;

    private static final String RUNNER_LOG_KEY = "runner";

    public LogListener(Trans trans) {
        this.trans = trans;
        this.job = null;
    }

    public LogListener(Job job) {
        this.trans = null;
        this.job = job;
    }

    public LogListener(String runnerKey) {
        this.name = runnerKey;
    }


    public static void setLogName(String name) {
        MDC.put(RUNNER_LOG_KEY, name);
    }

    public static void clearLog(String name) {
        MDC.remove(RUNNER_LOG_KEY);
    }

    public static void writeLog(String message, LogLevel level, long timeStamp) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = null;
        if (timeStamp == 0L) {
            time = df.format(new Date());
        } else {
            time = df.format(timeStamp);
        }

        log.trace(String.format("[%s] [%s] - %s", time, level.toString(), message));
    }


    @Override
    public void eventAdded(KettleLoggingEvent kettleLoggingEvent) {
        LogThread.create(() -> {
            String message = kettleLoggingEvent.getMessage() + "";
            if ("".equals(message)) {
                return;
            }

            setLogName(this.name);
            writeLog(message, kettleLoggingEvent.getLevel(), kettleLoggingEvent.getTimeStamp());
            clearLog(this.name);
        });
    }
}
