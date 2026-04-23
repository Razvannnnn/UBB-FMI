
package Model.LatchTable;

import Model.Statement.PrgState;
import Model.Statement.Statement;

public class AwaitStmt implements Statement {
    private String var;

    public AwaitStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState p) {
        if (!p.getSymbolT().contains(var))
            return null;
        int foundIndex=p.getSymbolT().get(var);
        if(!p.getLatchTable().contains(foundIndex))
            return null;
        else
            p.getExecStack().push(this);
        return null;
    }

    @Override
    public String toString(){
        return "await(" + var + ")";
    }
}
