package com.deep.pyrun.bean;

import android.accessibilityservice.GestureDescription.StrokeDescription;
import android.graphics.Path;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Class -
 * <p>
 * Created by Deepblue on 2019/7/1 0001.
 */

public abstract class Event {
    private long duration = 10;
    public Path path;
    private long startTime = 10;

    public abstract void movePath();

    public final long getStartTime() {
        return this.startTime;
    }

    public final void setStartTime(long j) {
        this.startTime = j;
    }

    public final long getDuration() {
        return this.duration;
    }

    public final void setDuration(long j) {
        this.duration = j;
    }

    public final Path getPath() {
        Path path = this.path;
        return path;
    }

    public final void setPath(Path path) {
        this.path = path;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public final StrokeDescription onEvent() {
        this.path = new Path();
        movePath();
        Path path = this.path;

        return new StrokeDescription(path, this.startTime, this.duration);
    }
}