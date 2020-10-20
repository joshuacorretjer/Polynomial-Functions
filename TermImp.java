public class TermImp implements Term{
    private double coefficient;
    private int exponent;
    public TermImp(double coefficient, int exponent){
        this.coefficient = coefficient;
        this.exponent = exponent;
    }
    @Override
    public double getCoefficient() {
        return this.coefficient;
    }

    @Override
    public int getExponent() {
        return this.exponent;
    }

    @Override
    public double evaluate(double x) {
        return this.coefficient*Math.pow(x,this.exponent);
    }
}
