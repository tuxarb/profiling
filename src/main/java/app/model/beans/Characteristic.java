package app.model.beans;

import app.utils.Utils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "results")
public class Characteristic implements Serializable {
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

    @Column(name = "test_date", nullable = false, precision = 6)
    private String testxDate = Utils.getCurrentDate();   // it's named as testxDate because Hibernate saves in alphabetical order

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

    public String getTestxDate() {
        return testxDate;
    }

    public void setTestxDate(String testxDate) {
        this.testxDate = testxDate;
    }

    @Override
    public String toString() {
        return "Characteristic{" +
                "id=" + id +
                ", runtime='" + runtime + '\'' +
                ", capacity='" + capacity + '\'' +
                ", speed='" + speed + '\'' +
                ", testsNumber=" + testsNumber +
                ", taskName='" + taskName + '\'' +
                ", testxDate='" + testxDate + '\'' +
                '}';
    }
}
