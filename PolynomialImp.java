import java.text.DecimalFormat;
import java.util.Iterator;

public class PolynomialImp implements Polynomial{
    private double coefficient;
    private int exponent;
    private String polynomial;
    private List<Term> listTerms;
    private static final double EPSILON = 0.0001;

    public PolynomialImp(String polynomial){
        if(polynomial==null){
            throw new IllegalArgumentException("Must input a valid polynomial");
        }
        this.polynomial = polynomial;
        this.listTerms = this.polynomialToTerm();
    }

    @Override
    public Polynomial add(Polynomial P2) {
        List<Term> p1 = this.listTerms;
        List<Term> p2 = P2.polynomialToTerm();
        List<Term> p3 = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i<p1.size() && j<p2.size()){
            if(p1.get(i).getExponent() == p2.get(j).getExponent()){
                double coeff = p1.get(i).getCoefficient() + p2.get(j).getCoefficient();
                Term term = new TermImp(coeff, p1.get(i).getExponent());
                p3.add(term);
                i++;
                j++;
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
        Polynomial newPolynomial = termsToPolynomial(p3);
        return newPolynomial;
    }

    @Override
    public Polynomial subtract(Polynomial P2) {
        return this.add(P2.multiply(-1));
    }

    @Override
    public Polynomial multiply(Polynomial P2) {
        List<Term> p1 = this.listTerms;
        List<Term> p2 = P2.polynomialToTerm();
        Polynomial Pol1 = new PolynomialImp("");

        for (int i = 0; i < p1.size(); i++) {
            List<Term> p3 = new ArrayList<Term>();
            for (int j = 0; j < p2.size(); j++) {
                double coeff = p1.get(i).getCoefficient() * p2.get(j).getCoefficient();
                int expo = p1.get(i).getExponent() + p2.get(j).getExponent();
                Term term = new TermImp(coeff, expo);
                p3.add(term);
            }
            Polynomial Pol2 = termsToPolynomial(p3);
            Pol1 = Pol1.add(Pol2);
        }
        return Pol1;
    }

    @Override
    public Polynomial multiply(double c) {
        List<Term> newTerms = new ArrayList<Term>();
        for(Term coeff: this.polynomialToTerm()){
            double coefficient = coeff.getCoefficient()*c;
            Term term = new TermImp(coefficient, coeff.getExponent());
            newTerms.add(term);
        }
        Polynomial newPolynomial = termsToPolynomial(newTerms);
        return newPolynomial;
    }

    @Override
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
        Polynomial newPolynomial = termsToPolynomial(newTerms);
        return newPolynomial;
    }

    @Override
    public Polynomial indefiniteIntegral() {
        List<Term> termList = this.listTerms;
        List<Term> newTerms = new ArrayList<Term>();
        for (int i = 0; i < termList.size(); i++) {
            double coeff = termList.get(i).getCoefficient();
            int expo = termList.get(i).getExponent();
            int newExpo = expo+1;
            double newCoeff = coeff/newExpo;
            Term term = new TermImp(newCoeff, newExpo);
            newTerms.add(term);
        }
        Term constant = new TermImp(1,0);
        newTerms.add(constant);
        Polynomial newPolynomial = termsToPolynomial(newTerms);
        return newPolynomial;
    }

    @Override
    public double definiteIntegral(double a, double b) {
        Polynomial P1 = this.indefiniteIntegral();
        double number1 = 0.0;
        double number2 = 0.0;
        number1=P1.evaluate(b);
        number2=P1.evaluate(a);
//        return this.indefiniteIntegral().evaluate(b)-this.indefiniteIntegral().evaluate(a);
        return number1-number2;
    }

    @Override
    public int degree() {
//        List<Term> p1 = this.stringToTerm();
//        int max = p1.get(0).getExponent();
//        for (int i = 0; i < term.size(); i++) {
//            if(p1.get(i).getExponent()>=max){
//                max = p1.get(i).getExponent();
//            }
//        }
//        return max;
        return this.listTerms.get(0).getExponent();
    }

    @Override
    public double evaluate(double x) {
        double total = 0.00;
        List<Term> termList = this.listTerms;
        for (int i = 0; i < termList.size(); i++) {
            total += termList.get(i).evaluate(x);
        }
        return total;
    }

    @Override
    public String toString(){//turns polynomial to string
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
                            stringBuild.append(df.format(coeff)+ "x^" + expo+"+");
                        }
                        else {
                            double coeff = 1.00;
                            int expo = Integer.parseInt(arr[i].substring(arr[i].indexOf("^") + 1));
                            stringBuild.append(df.format(coeff)+ "x^" + expo+"+");
                        }
                    } else {
                        if(!coefficient.equals("")) {
                            double coeff = Double.parseDouble(coefficient);
                            stringBuild.append(df.format(coeff) + "x+");
                        }
                        else{
                            double coeff = 1.00;
                            stringBuild.append(df.format(coeff) + "x+");
                        }
                    }
//                    if(!arr[i+1].contains("")){
//                        stringBuild.append("+");
//                    }
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
    public boolean equals(Object obj) {
        if(!(obj instanceof PolynomialImp)){
            throw new IllegalArgumentException("Obj must be a Polynomial");
        }
        Polynomial temp = (PolynomialImp)obj;
        List<Term> p1 = this.listTerms;
        List<Term> p2 = temp.polynomialToTerm();
        if(p1.size()==p2.size()) {
            for (int i = 0; i < p1.size(); i++) {
//                if ((p1.get(i).getCoefficient() - p2.get(i).getCoefficient()) != 0.00 && (p1.get(i).getExponent() - p1.get(i).getExponent()) != 0) {
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
    public List<Term> polynomialToTerm(){//turns polynomials into a list of terms
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
        return this.listTerms.iterator();
    }

    public Polynomial termsToPolynomial(List<Term> termList){//turns list of terms into polynomials
        StringBuffer stringBuild = new StringBuffer();
        for (int i = 0; i < termList.size(); i++) {
            double coeff = termList.get(i).getCoefficient();
            int expo = termList.get(i).getExponent();
            if(termList.get(i).getExponent()>1) {
                if(coeff!=0) {
                    stringBuild.append(termList.get(i).getCoefficient());
                    stringBuild.append("x^");
                    stringBuild.append(expo);
                    stringBuild.append("+");
                }
            }
            else if(termList.get(i).getExponent()==1){
                if(coeff!=0) {
                    stringBuild.append(termList.get(i).getCoefficient());
                    stringBuild.append("x");
                    stringBuild.append("+");
                }
            }
            else{
                stringBuild.append(termList.get(i).getCoefficient());
            }
        }
        String polyString = stringBuild.toString();
        Polynomial newPolynomial = new PolynomialImp(polyString);

        return newPolynomial;
    }

}
