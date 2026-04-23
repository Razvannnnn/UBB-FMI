/**************************************************************************************************
 *       Copyright(c):                                                                            *
 *                         Program made by @Moldovan Alexandru-Vasile.                            *
 *                                                                                                *
 **************************************************************************************************/

package Model.CyclicBarrier;

import Model.Statement.PrgState;
import Model.Statement.Statement;

public class AwaitStmt implements Statement
{
    private String var;

    public AwaitStmt(String v)
    {
        var = v;
    }

    @Override
    public PrgState execute(PrgState p)
    {
        boolean isFound = p.getSymbolT().contains(var);
        if (!isFound) return null;

        int index = p.getSymbolT().get(var);
        if (!p.getCyclicBarrier().contains(index)) return null;

        synchronized (p.getCyclicBarrier())
        {
            if (p.getCyclicBarrier().get(index).getFirst() == p.getCyclicBarrier().get(index).getSecond().size())
                return null;
            else
            {
                p.getExecStack().push(new AwaitStmt(var));
                p.getCyclicBarrier().get(index).getSecond().add(p.getID());
                return null;
            }
        }
    }

    @Override
    public String toString()
    {
        return "Await("+var+")";
    }
}
