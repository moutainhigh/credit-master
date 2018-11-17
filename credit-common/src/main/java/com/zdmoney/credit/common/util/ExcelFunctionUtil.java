package com.zdmoney.credit.common.util;

public class ExcelFunctionUtil {
	
	public static double rate(Double nper,Double pmt,Double pv,Double fv,Double type,Double guess) {
        if (guess == null){
        	guess = 0.01;
        }
        if (fv == null) {
        	fv = 0d;
        }
        if (type == null) {
        	type = 0d;
        }
        double FINANCIAL_MAX_ITERATIONS = 128;//Bet accuracy with 128
        double FINANCIAL_PRECISION = 0.0000001;//1.0e-8
        double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
        double rate = guess;
        if (Math.abs(rate) < FINANCIAL_PRECISION) {
            y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
        } else {
            f = Math.exp(nper * Math.log(1 + rate));
            y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
        }
        y0 = pv + pmt * nper + fv;
        y1 = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
        // find root by Newton secant method
        i = x0 = 0.0;
        x1 = rate;
        while ((Math.abs(y0 - y1) > FINANCIAL_PRECISION) && (i < FINANCIAL_MAX_ITERATIONS)) {
            rate = (y1 * x0 - y0 * x1) / (y1 - y0);
            x0 = x1;
            x1 = rate;
            if (Math.abs(rate) < FINANCIAL_PRECISION) {
                y = pv * (1 + nper * rate) + pmt * (1 + rate * type) * nper + fv;
            } else {
                f = Math.exp(nper * Math.log(1 + rate));
                y = pv * f + pmt * (1 / rate + type) * (f - 1) + fv;
            }
            y0 = y1;
            y1 = y;
            ++i;
        }
        return rate;
    }


    public static double irr(double[] values) {
        double guess=0.001;
        int maxIterationCount = 20;
        double absoluteAccuracy = 1E-7;
        double x0 = guess;
        double x1;
        int i = 0;
        while (i < maxIterationCount) {
            // the value of the function (NPV) and its derivate can be calculated in the same loop
            double fValue = 0;
            double fDerivative = 0;
            for (int k = 0; k < values.length; k++) {
                fValue += values[k] / Math.pow(1.0 + x0, k);
                fDerivative += -k * values[k] / Math.pow(1.0 + x0, k + 1);
            }
            // the essense of the Newton-Raphson Method
            x1 = x0 - fValue/fDerivative;
            if (Math.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }
            x0 = x1;
            ++i;
        }
        return Double.NaN;
    }

    public static double pmt(double rate_per_period,double number_of_payments,double present_value,double future_value){
        double type=0;
        if(rate_per_period != 0.0){
            // Interest rate exists
            double q = Math.pow(1 + rate_per_period, number_of_payments);
            return -(rate_per_period * (future_value + (q * present_value))) / ((-1 + q) * (1 + rate_per_period * (type)));

        } else if(number_of_payments != 0.0){
            // No interest rate, but number of payments exists
            return -(future_value + present_value) / number_of_payments;
        }
        return 0;
    }

    public static double pv(double rate,double nper,double pmt){
        return (-pmt) / rate * (1 - Math.pow(1 + rate, -nper));
    }

    ////from src / java / org / apache / poi / ss / formula / functions / Finance.java
    /**
     * Emulates Excel/Calc's FV(interest_rate, number_payments, payment, PV,
     * Type) function, which calculates future value or principal at period N.
     *
     * @param r
     *            - periodic interest rate represented as a decimal.
     * @param nper
     *            - number of total payments / periods.
     * @param pmt
     *            - periodic payment amount.
     * @param pv
     *            - present value -- borrowed or invested principal.
     * @param type
     *            - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing future principal value.
     */
    //http://en.wikipedia.org/wiki/Future_value
    static public double fv(double r, int nper, double pmt, double pv, int type) {
        double fv = -(pv * Math.pow(1 + r, nper) + pmt * (1+r*type) * (Math.pow(1 + r, nper) - 1) / r);
        return fv;
    }
}
