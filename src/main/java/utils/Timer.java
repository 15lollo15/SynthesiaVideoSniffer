package utils;

public class Timer {

    private long start;
    private long end;
    private boolean isRunning = false;

    public void start() {
        if (!isRunning) {
            start = System.currentTimeMillis();
            isRunning = true;
        }
    }

    public long stop() {
        if (isRunning) {
            end = System.currentTimeMillis();
            isRunning = false;
            return end - start;
        }
        return 0;
    }


}
