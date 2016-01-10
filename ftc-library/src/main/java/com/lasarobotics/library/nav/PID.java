package com.lasarobotics.library.nav;

/**
 * PID Targeting
 */
public class PID {
    protected double setpoint = 0;
    protected double processValue = 0;  // actual possition (Process Value)
    protected double error = 0;   // how much SP and PV are diff (SP - PV)
    protected double integral = 0; // curIntegral + (error * Delta Time)
    protected double derivative = 0;  //(error - prev error) / Delta time
    protected double previousError = 0; // error from last time (previous Error)
    protected double Kp = 0.2, Ki = 0.01, Kd = 1; // PID constant multipliers
    protected double dt = 100.0; // delta time
    protected double output = 0; // the drive amount that effects the PV.

    public PID(double p, double i, double d) {
        this.Kp = p;
        this.Ki = i;
        this.Kd = d;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    public void addMeasurement(double measuredValue, double timeDelta) {
        this.processValue = measuredValue;
        this.dt = timeDelta;
        update();
    }

    /**
     * Gets the most recent error - the difference between the setpoint and the measured value
     *
     * @return setpoint - measured value
     */
    public double getError() {
        return error;
    }

    /**
     * Get the integral value, or the sum of the recent error values
     *
     * @return Integral value I
     */
    public double getIntegralValue() {
        return integral;
    }

    /**
     * Get the derivative value, or how much the error is changing
     *
     * @return The slope of the error values
     */
    public double getDerivativeValue() {
        return derivative;
    }

    /**
     * Gets the error value from the previous measurement
     *
     * @return The previous error value
     */
    public double getPreviousError() {
        return previousError;
    }

    public double getCoefficientProportional() {
        return Kp;
    }

    public double getCoefficientIntegral() {
        return Ki;
    }

    public double getCoefficientDerivative() {
        return Kd;
    }

    public double getLastTimeDelta() {
        return dt;
    }

    public double getOutputValue() {
        return output;
    }

    private void update() {
        // calculate the difference between the desired value and the actual value
        error = setpoint - processValue;
        // track error over time, scaled to the timer interval
        integral = integral + (error * dt);
        // determin the amount of change from the last time checked
        derivative = (error - previousError) / dt;

        // calculate how much drive the output in order to get to the
        // desired setpoint.
        output = (Kp * error) + (Ki * integral) + (Kd * derivative);

        // remember the error for the next time around.
        previousError = error;
    }
}
