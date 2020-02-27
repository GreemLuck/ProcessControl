public class Controller {

    private double P;
    private double I;
    private double D;

    private double lowerBound;
    private double upperBound;
    private boolean isBang;

    private double errorMax;
    private double errorMin;

    long priorTime;
    double accumulatedError;
    double priorError;

    private double setpoint = 0;


    //PID
    public Controller(double p, double i, double d){
        new Controller(p, i, d, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public Controller(double p, double i, double d, double errorMin, double errorMax){
        P=p; I=i; D=d;
        this.errorMax = errorMax;
        this.errorMin = errorMin;

        priorTime = System.currentTimeMillis();
        accumulatedError = 0;
        priorError = 0;
        isBang = false;
    }


    //Bang Bang
    public Controller(){
        isBang = true;
    }

    public double getOutput(double actual){
        return getOutput(actual, setpoint);
    }

    public double getOutput(double actual, double setpoint) {
        double output;
        this.setpoint = setpoint;

        if(isBang){
            return bangBang(actual);
        }

        double error = setpoint - actual;

        long dt = System.currentTimeMillis() - priorTime;
        priorTime = System.currentTimeMillis();

        accumulatedError += error*dt;
        if(accumulatedError > errorMax) accumulatedError = errorMax;
        if(accumulatedError < errorMin) accumulatedError = errorMin;

        double derror = (error - priorError)/dt;
        priorError = error;

        output = P*error + I*accumulatedError + D*derror;

        if(output > Main.MAX_OUTPUT) return Main.MAX_OUTPUT;
        if(output < Main.MIN_OUTPUT) return Main.MIN_OUTPUT;
        return output;
    }

    private double bangBang(double actual){
        if(actual > setpoint){
            return Main.MIN_OUTPUT*0.6;
        }
        else {
            return Main.MAX_OUTPUT*0.6;
        }
    }

    public void reset() {
        setpoint = 0;
        priorError = 0;
        priorTime = System.currentTimeMillis();
        accumulatedError = 0;
    }

    public double getP() {
        return P;
    }

    public void setP(double p) {
        P = p;
    }

    public double getI() {
        return I;
    }

    public void setI(double i) {
        I = i;
    }

    public double getD() {
        return D;
    }

    public void setD(double d) {
        D = d;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

}
