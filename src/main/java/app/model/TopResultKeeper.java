package app.model;


class TopResultKeeper {
    private long runtime;
    private long capacity;
    private long speed;

    TopResultKeeper() {
        this.runtime = Long.MAX_VALUE;
        this.capacity = Long.MAX_VALUE;
        this.speed = Long.MAX_VALUE;
    }

    boolean isTopResult(long capacity, long speed, long runtime) {
        if (this.runtime != Long.MAX_VALUE &&
                (this.runtime > 1.75 * runtime || this.capacity > 2 * capacity)) {
            return false;
        }
        if ((this.runtime > runtime && (this.capacity > 1.25 * capacity || this.capacity < 1.25 * capacity)) ||
                (this.runtime == runtime && this.capacity > capacity)) {
            this.runtime = runtime;
            this.capacity = capacity;
            this.speed = speed;
            return true;
        }
        return false;
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
