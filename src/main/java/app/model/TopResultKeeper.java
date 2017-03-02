package app.model;


public class TopResultKeeper {
    private long runtime;
    private long capacity;
    private long speed;

    TopResultKeeper() {
        setValuesByDefault();
    }

    boolean isTopResult(long newCapacity, long newSpeed, long newRuntime) {
        if (this.runtime == Long.MAX_VALUE) {
            update(newCapacity, newSpeed, newRuntime);
            return true;
        }
        if (newRuntime < 0.5 * this.runtime) {              //new value runtime is too small
            return false;
        }
        if (newCapacity >= this.capacity * 0.75 && (       //new value capacity is not smaller than prev not more than a quarter
                (newRuntime < this.runtime &&               //new value runtime is less than prev
                        (newCapacity <= this.capacity ||     //new value capacity is not more than prev
                                //new value capacity is not more than prev not more than a quarter and and the runtime difference is not less than 20 ms
                                (newCapacity <= this.capacity * 1.25 && this.runtime - newRuntime >= 20))) ||
                        (newRuntime == this.runtime && newCapacity < this.capacity) ||
                        (newRuntime > this.runtime && newRuntime - this.runtime <= 15 &&
                                newCapacity <= 0.95 * this.capacity))) {
            update(newCapacity, newSpeed, newRuntime);
            return true;
        }
        return false;
    }

    private void update(long newCapacity, long newSpeed, long newRuntime) {
        this.runtime = newRuntime;
        this.capacity = newCapacity;
        this.speed = newSpeed;
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
