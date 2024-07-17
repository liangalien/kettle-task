package com.liangalien.kt.util;

import org.pentaho.di.core.gui.*;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.JobPainter;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.TransPainter;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class KettleUtil {
    public static RunnerStatus getStatus(String message) {
        if ("Waiting".equals(message)) {
            return RunnerStatus.WAITING;
        } else if ("Halting".equals(message) || "Stopped".equals(message)) {
            return RunnerStatus.STOPPED;
        } else if ("Finished".equals(message)) {
            return RunnerStatus.SUCCESS;
        } else if ("Finished (with errors)".equals(message) || "Stopped (with errors)".equals(message)) {
            return RunnerStatus.ERROR;
        } else if ("Running".equals(message)) {
            return RunnerStatus.RUNNING;
        }
        return RunnerStatus.OTHER;
    }

    public static BufferedImage generateTransImage(TransMeta transMeta) throws Exception {
        float magnification = 1.0F;
        Point maximum = transMeta.getMaximum();
        maximum.multiply(magnification);
        SwingGC gc = new SwingGC((ImageObserver)null, maximum, 32, 0, 0);
        TransPainter transPainter = new TransPainter(gc, transMeta, maximum, (ScrollBarInterface)null, (ScrollBarInterface)null, (TransHopMeta)null, (Point)null, (Rectangle)null, new ArrayList(), new ArrayList(), 32, 1, 0, 0, true, "Arial", 10);
        transPainter.setMagnification(magnification);
        transPainter.buildTransformationImage();
        return (BufferedImage)gc.getImage();
    }

    public static BufferedImage generateJobImage( JobMeta jobMeta ) throws Exception {
        float magnification = 1.0f;
        Point maximum = jobMeta.getMaximum();
        maximum.multiply( magnification );

        SwingGC gc = new SwingGC( null, maximum, 32, 0, 0 );
        JobPainter jobPainter =
                new JobPainter(
                        gc, jobMeta, maximum, null, null, null, null, null, new ArrayList<AreaOwner>(),
                        new ArrayList<JobEntryCopy>(), 32, 1, 0, 0, true, "Arial", 10 );
        jobPainter.setMagnification( magnification );
        jobPainter.drawJob();

        BufferedImage image = (BufferedImage) gc.getImage();

        return image;
    }
}
