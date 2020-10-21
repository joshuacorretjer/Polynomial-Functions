package Polynomial;

import Polynomial.Term;

public class TermImp implements Term {

    private double coefficient;
    private int exponent;

    public TermImp(double coefficient, int exponent){
        this.coefficient = coefficient;
        this.exponent = exponent;
    }
    @Override
    //Method that gets the coefficient of a term.
    public double getCoefficient() {
        return this.coefficient;
    }

    @Override
    //Method that gets the exponent of a term.
    public int getExponent() {
        return this.exponent;
    }

    @Override
    //Method that evaluates the term at value x.
    public double evaluate(double x) {
        return this.coefficient*Math.pow(x,this.exponent);
    }
}
