package app.model.beans;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "results")
public class Characteristic implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "runtime", length = 15, nullable = false)
    private String runtime;

    @Column(name = "capacity", length = 18, nullable = false)
    private String capacity;

    @Column(name = "speed", length = 18, nullable = false)
    private String speed;

    @Column(name = "tests_number", nullable = false)
    private int testsNumber;

    @Column(name = "program_name", length = 50, nullable = false)
    private String taskName = "";

    public Characteristic() {
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String programName) {
        this.taskName = programName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTestsNumber() {
        return testsNumber;
    }

    public void setTestsNumber(int iterationsNumber) {
        this.testsNumber = iterationsNumber;
    }

    @Override
    public String toString() {
        return "Characteristic{" +
                "id=" + id +
                ", runtime='" + runtime + '\'' +
                ", capacity='" + capacity + '\'' +
                ", speed='" + speed + '\'' +
                ", iterationsNumber='" + testsNumber + '\'' +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
