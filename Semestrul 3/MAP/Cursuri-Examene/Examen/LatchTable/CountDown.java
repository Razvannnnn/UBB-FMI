
package Model.LatchTable;

import Model.Exception.DivByZeroException;
import Model.Exception.EvaluationException;
import Model.Exception.InexistentSymbolException;
import Model.Statement.*;


public class CountDown implements Statement{
    private String var;
    public CountDown(String var) {
        this.var = var;
    }
    @Override
    public PrgState execute(PrgState state) {
        if (state.getSymbolT().get(this.var) == null);
        int index = state.getSymbolT().get(this.var);
        synchronized (state.getLatchTable()) {
            if (state.getLatchTable().get(index) == null)
                return null; // do nothing
            int count = state.getLatchTable().get(index);
            if (count > 0) {
                state.getLatchTable().put(index, count - 1);
                state.getList().add(state.getID());
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "countDown(" + this.var + ")";
    }
}
