
abstract class Expr {  
    abstract public String toString();
}

abstract class BinaryExpr extends Expr {
    private Expr e1;
    private Expr e2;

    protected BinaryExpr(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expr getE1() {
        return e1;
    }

    public Expr getE2() {
        return e2;
    }
}

class PlusExpr extends BinaryExpr {
    public PlusExpr(Expr e1, Expr e2) {
        super(e1, e2);
    }

    public String toString() {
        return "(" + getE1().toString() + " + " + getE2().toString() + ")";
    }
}

class MinusExpr extends BinaryExpr {
    public MinusExpr(Expr e1, Expr e2) {
        super(e1, e2);
    }

    public String toString() {
        return "(" + getE1().toString() + " - " + getE2().toString() + ")";
    }
}

class TimesExpr extends BinaryExpr {
    public TimesExpr(Expr e1, Expr e2) {
        super(e1, e2);
    }

    public String toString() {
        return "(" + getE1().toString() + " * " + getE2().toString() + ")";
    }
}

class DivExpr extends BinaryExpr {
    public DivExpr(Expr e1, Expr e2) {
        super(e1, e2);
    }

    public String toString() {
        return "(" + getE1().toString() + " / " + getE2().toString() + ")";
    }
}

class FloatExpr extends Expr {
    private float literal;

    public FloatExpr(float f) {
        this.literal = f;
    }

    public String toString() {
        return Float.toString(literal);
    }
}