package app.model;


import app.utils.Utils;

public class ResultsCalculator {
    private double capacity;
    private double speed;
    private long runtime;
    private Model model;

    public ResultsCalculator(Model model) {
        this.model = model;
    }

    public void addResultsForTestToSum() {
        capacity += Utils.getNumberFromString(model.getCharacteristic().getCapacity());
        runtime += Utils.getNumberFromString(model.getCharacteristic().getRuntime());
        speed += Utils.getNumberFromString(model.getCharacteristic().getSpeed());
    }

    public void computeAverageResultsForAllTests() {
        capacity /= model.getNumberTests();
        runtime /= model.getNumberTests();
        speed /= model.getNumberTests();
    }

    public void setResultingData() {
        model.setResultingData((long) capacity, (long) speed, runtime);
    }
}
