package com.liangalien.kt.kettle;

import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;

public class KettleJob extends Job {

    public KettleJob(Repository repository, JobMeta jobMeta) {
        super((Repository)repository, (JobMeta)jobMeta, (LoggingObjectInterface)null);
    }


    // 主要是为了重写这个方法，避免${Internal.Entry.Current.Directory}被重置
    public void setInternalKettleVariables(VariableSpace var) {

    }
}