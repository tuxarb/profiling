package app.model;


public class TopResultKeeper {
    private long runtime;
    private long capacity;
    private long speed;

    TopResultKeeper() {
        setValuesByDefault();
    }

    boolean isTopResult(long capacity, long speed, long runtime) {
        if (this.runtime != Long.MAX_VALUE && this.runtime > 1.85 * runtime) {
            return false;
        }
        if (this.runtime >= runtime &&
                (this.capacity > capacity || this.capacity < 0.9 * capacity)) {
            this.runtime = runtime;
            this.capacity = capacity;
            this.speed = speed;
            return true;
        }
        return false;
    }

    public void reset() {
        setValuesByDefault();
    }

    private void setValuesByDefault() {
        this.runtime = Long.MAX_VALUE;
        this.capacity = Long.MAX_VALUE;
        this.speed = Long.MAX_VALUE;
    }

    public long getRuntime() {
        return runtime;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getSpeed() {
        return speed;
    }
}
