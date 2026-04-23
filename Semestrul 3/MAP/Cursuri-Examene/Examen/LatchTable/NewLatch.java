package Model.LatchTable;

import Model.Exception.DivByZeroException;
import Model.Exception.EvaluationException;
import Model.Exception.InexistentSymbolException;
import Model.Expression.Expression;
import Model.Statement.PrgState;
import Model.Statement.Statement;

public class NewLatch implements Statement{
    private static int newFreeLocation = -1;
    private String var;
    private Expression exp;

    public NewLatch(String var,Expression exp) {
        this.var=var;
        this.exp=exp;
    }

    @Override
    public PrgState execute(PrgState state) {
        try {
            int number = this.exp.Eval(state.getSymbolT(), state.getHeap());
            synchronized (state.getLatchTable()) {
                ++newFreeLocation;
                state.getLatchTable().put(newFreeLocation,number);
                state.getSymbolT().add(this.var,newFreeLocation);
                return null;
            }

        }
        catch(DivByZeroException | EvaluationException | InexistentSymbolException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "newLatch(" + this.var + ", " + this.exp.toString() + ")";
    }
}
