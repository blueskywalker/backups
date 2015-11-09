package com.sysomos.grid.tools.backup.twitter;

import com.sysomos.grid.tools.backup.BackupConsumer;
import com.sysomos.grid.tools.backup.BackupTool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kkim on 9/1/15.
 */
public class TwitterComplianceConsumer extends BackupConsumer {
    public TwitterComplianceConsumer(BackupTool tool) throws IOException {
        super(tool);
    }


    @Override
    protected String getRootdir() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long currentTime = System.currentTimeMillis();
        String day = format.format(new Date(currentTime));
        return String.format("%s/compliance/%s", properties.getProperty(BACKUP_DIR_KEY, "/backup"), day);
    }
}
