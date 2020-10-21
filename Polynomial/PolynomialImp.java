package Polynomial;

import edu.uprm.cse.list.ArrayList;
import edu.uprm.cse.list.List;

import java.text.DecimalFormat;
import java.util.Iterator;

public class PolynomialImp implements Polynomial{

    private String polynomial;
    private List<Term> listTerms;
    private static final double EPSILON = 0.0001;

    //Constructor that receives a string and converts it into a polynomial.
    public PolynomialImp(String polynomial){
        if(polynomial==null){
            throw new IllegalArgumentException("Must input a valid polynomial");
        }
        this.polynomial = polynomial;
        this.listTerms = this.polynomialToTerm();
    }

    //Constructor that receives a string and converts it into a polynomial.
    public PolynomialImp(List<Term> list){
        this.listTerms = list;
        this.polynomial = termsToPolynomial(list).toString();
    }

    @Override
    //Method that gets the terms of a specific polynomial.
    public List<Term> getListTerms(){
        return this.listTerms;
    }

    @Override
    //Method that sums 2 polynomials and returns a new polynomial.
    public Polynomial add(Polynomial P2) {
        List<Term> p1 = this.listTerms;
        List<Term> p2 = P2.getListTerms();
        List<Term> p3 = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i<p1.size() && j<p2.size()){
            if(p1.get(i).getExponent() == p2.get(j).getExponent()){
                double coeff = p1.get(i).getCoefficient() + p2.get(j).getCoefficient();
                if(coeff!=0) {
                    Term term = new TermImp(coeff, p1.get(i).getExponent());
                    p3.add(term);
                    i++;
                    j++;
                }
                else{
                    if(p1.get(i).getExponent()==0){
                        Term term = new TermImp(coeff, p1.get(i).getExponent());
                        p3.add(term);
                        i++;
                        j++;
                    }
                    else{
                        i++;
                        j++;
                    }

                }
            }
            else if(p1.get(i).getExponent() > p2.get(j).getExponent()){
                Term term = new TermImp(p1.get(i).getCoefficient(), p1.get(i).getExponent());
                p3.add(term);
                i++;
            }
            else{
                Term term = new TermImp(p2.get(j).getCoefficient(), p2.get(j).getExponent());
                p3.add(term);
                j++;
            }
        }
        while(i<p1.size()){
            Term term = new TermImp(p1.get(i).getCoefficient(), p1.get(i).getExponent());
            p3.add(term);
            i++;
        }
        while(j<p2.size()){
            Term term = new TermImp(p2.get(j).getCoefficient(), p2.get(j).getExponent());
            p3.add(term);
            j++;
        }
        Polynomial newPolynomial = new PolynomialImp(p3);
        return newPolynomial;
    }

    @Override
    //Method that subtracts 2 polynomials and returns a new polynomial.
    public Polynomial subtract(Polynomial P2) {
        return this.add(P2.multiply(-1));
    }

    @Override
    //Method that multiplies 2 polynomials and returns a new polynomial.
    public Polynomial multiply(Polynomial P2) {
        List<Term> p1 = this.listTerms;
        List<Term> p2 = P2.getListTerms();
        Polynomial Pol1 = new PolynomialImp("");

        for (int i = 0; i < p1.size(); i++) {
            List<Term> p3 = new ArrayList<Term>();
            for (int j = 0; j < p2.size(); j++) {
                double coeff = p1.get(i).getCoefficient() * p2.get(j).getCoefficient();
                int expo = p1.get(i).getExponent() + p2.get(j).getExponent();
                if(coeff!=0) {
                    Term term = new TermImp(coeff, expo);
                    p3.add(term);
                }
                else{
                    if(expo==0){
                        Term term = new TermImp(0.0, 0);
                        p3.add(term);
                    }
                }
            }
            Polynomial Pol2 = new PolynomialImp(p3);
            Pol1 = Pol1.add(Pol2);
        }
        return Pol1;
    }

    @Override
    //Method that multiplies a polynomial by a constant and returns a new polynomial.
    public Polynomial multiply(double c) {
        List<Term> newTerms = new ArrayList<Term>();
        if(c==0){
            Term term = new TermImp(0.0, 0);
            newTerms.add(term);
            Polynomial newPolynomial = new PolynomialImp(newTerms);
            return newPolynomial;
        }
        else {
            for (Term coeff : this.listTerms) {
                double coefficient = coeff.getCoefficient() * c;
                Term term = new TermImp(coefficient, coeff.getExponent());
                newTerms.add(term);
            }
            Polynomial newPolynomial = new PolynomialImp(newTerms);
            return newPolynomial;
        }
    }

    @Override
    //Method that returns the derivative of a polynomial.
    public Polynomial derivative() {
        List<Term> termList = this.listTerms;
        List<Term> newTerms = new ArrayList<Term>();
        for (int i = 0; i < termList.size(); i++) {
            double coeff = termList.get(i).getCoefficient();
            int expo = termList.get(i).getExponent();
            if(expo>1) {
                double newCoeff = coeff*expo;
                int newExpo = expo-1;
                Term term = new TermImp(newCoeff, newExpo);
                newTerms.add(term);
            }
            else if(expo==1){
                Term term = new TermImp(coeff, 0);
                newTerms.add(term);
            }
        }
        Polynomial newPolynomial = new PolynomialImp(newTerms);
        return newPolynomial;
    }

    @Override
    //Method that finds the indefinite integral (anti-derivative) of a polynomial.
    public Polynomial indefiniteIntegral() {
        List<Term> termList = this.listTerms;
        List<Term> newTerms = new ArrayList<Term>();
        for (int i = 0; i < termList.size(); i++) {
            double coeff = termList.get(i).getCoefficient();
            int expo = termList.get(i).getExponent();
            int newExpo = expo+1;
            double newCoeff = coeff/newExpo;
            if(coeff!=0) {
                Term term = new TermImp(newCoeff, newExpo);
                newTerms.add(term);
            }
        }
        Term constant = new TermImp(1,0);
        newTerms.add(constant);
        Polynomial newPolynomial = new PolynomialImp(newTerms);
        return newPolynomial;
    }

    @Override
    //Method that evaluates a polynomial's definite integral over an interval [a,b].
    public double definiteIntegral(double a, double b) {
        return this.indefiniteIntegral().evaluate(b)-this.indefiniteIntegral().evaluate(a);
    }

    @Override
    //Method that finds it's degree (the largest exponent in any term).
    public int degree() {
//        List<Term> p1 = this.listTerms;
//        int max = p1.get(0).getExponent();
//        for (int i = 0; i < p1.size(); i++) {
//            if(p1.get(i).getExponent()>=max){
//                max = p1.get(i).getExponent();
//            }
//        }
//        return max;
        return this.listTerms.get(0).getExponent();
    }

    @Override
    //Method that evaluates the polynomial at value x.
    public double evaluate(double x) {
        double total = 0.00;
        List<Term> termList = this.listTerms;
        for (int i = 0; i < termList.size(); i++) {
            total += termList.get(i).evaluate(x);
        }
        return total;
    }

    @Override
    //Method that turns polynomials into strings.
    public String toString(){
        StringBuilder stringBuild = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        String[] arr = this.polynomial.split("\\+");
        if(arr!=null) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].contains("x")) {
                    String coefficient = arr[i].substring(0, arr[i].indexOf("x"));
                    if (arr[i].contains("^")) {
                        if(!coefficient.equals("")) {
                            double coeff = Double.parseDouble(coefficient);
                            int expo = Integer.parseInt(arr[i].substring(arr[i].indexOf("^") + 1));
                            stringBuild.append(df.format(coeff)+ "x^" + expo);
                        }
                        else {
                            double coeff = 1.00;
                            int expo = Integer.parseInt(arr[i].substring(arr[i].indexOf("^") + 1));
                            stringBuild.append(df.format(coeff)+ "x^" + expo);
                        }
                    } else {
                        if(!coefficient.equals("")) {
                            double coeff = Double.parseDouble(coefficient);
                            stringBuild.append(df.format(coeff) + "x");
                        }
                        else{
                            double coeff = 1.00;
                            stringBuild.append(df.format(coeff) + "x");
                        }
                    }
                    if(i<arr.length-1){
                        stringBuild.append("+");
                    }
                } else if(arr[i].equals("")) {
                    double coeff = 0.00;
                    stringBuild.append(df.format(coeff));
                }
                else{
                    double coeff = Double.parseDouble(arr[i]);
                    stringBuild.append(df.format(coeff));
                }
            }
        }
        String polyString = stringBuild.toString();
        return polyString;
    }

    @Override
    //Method that verifies if 2 polynomials are equal to each other using their terms.
    public boolean equals(Object obj) {
        if(!(obj instanceof PolynomialImp)){
            throw new IllegalArgumentException("Obj must be a PolynomialImp");
        }
        Polynomial temp = (PolynomialImp)obj;
        List<Term> p1 = this.listTerms;
        List<Term> p2 = temp.getListTerms();
        if(p1.size()==p2.size()) {
            for (int i = 0; i < p1.size(); i++) {
                if(Math.abs(p1.get(i).getCoefficient()-p2.get(i).getCoefficient())>EPSILON && p1.get(i).getExponent()!=p2.get(i).getExponent()){
                    return false;
                }
            }
        }
        else{
            return false;
        }
        return true;
    }

    @Override
    //Method that converts a polynomial into a list of terms and returns a new ArrayList of terms.
    public List<Term> polynomialToTerm(){
        List<Term> termsPoly = new ArrayList<Term>();
        String polyString = this.toString();
        String[] arr = polyString.split("\\+");
        if(arr!=null) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].contains("x")) {
                    if (arr[i].contains("^")) {
                        double coeff = Double.parseDouble(arr[i].substring(0, arr[i].indexOf("x")));
                        int expo = Integer.parseInt(arr[i].substring(arr[i].indexOf("^") + 1));
                        Term terms = new TermImp(coeff, expo);
                        termsPoly.add(terms);
                    } else {
                        double coeff = Double.parseDouble(arr[i].substring(0, arr[i].indexOf("x")));
                        int expo = 1;
                        Term terms = new TermImp(coeff, expo);
                        termsPoly.add(terms);
                    }
                } else {
                    double coeff = Double.parseDouble(arr[i]);
                    int expo = 0;
                    Term terms = new TermImp(coeff, expo);
                    termsPoly.add(terms);
                }
            }
        }
        return termsPoly;
    }

    @Override
    public Iterator<Term> iterator() {
        return null;
    }

    //Method that converts a list of terms into a polynomial and returns a new polynomial.
    public Polynomial termsToPolynomial(List<Term> termList){//turns list of terms into polynomials
        StringBuffer stringBuild = new StringBuffer();
        if(termList.isEmpty()){
            Polynomial newPolynomial = new PolynomialImp("0");
            return newPolynomial;
        }
        else {
            for (int i = 0; i < termList.size(); i++) {
                double coeff = termList.get(i).getCoefficient();
                int expo = termList.get(i).getExponent();
                if (termList.get(i).getExponent() > 1) {
                    if (coeff != 0) {
                        stringBuild.append(termList.get(i).getCoefficient());
                        stringBuild.append("x^");
                        stringBuild.append(expo);
                        stringBuild.append("+");
                    }
                } else if (termList.get(i).getExponent() == 1) {
                    if (coeff != 0) {
                        stringBuild.append(termList.get(i).getCoefficient());
                        stringBuild.append("x");
                        stringBuild.append("+");
                    }
                } else {
                    stringBuild.append(termList.get(i).getCoefficient());
                }
            }
            String polyString = stringBuild.toString();
            Polynomial newPolynomial = new PolynomialImp(polyString);

            return newPolynomial;
        }
    }

}
