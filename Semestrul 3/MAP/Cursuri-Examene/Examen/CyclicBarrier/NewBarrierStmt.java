/**************************************************************************************************
 *       Copyright(c):                                                                            *
 *                         Program made by @Moldovan Alexandru-Vasile.                            *
 *                                                                                                *
 **************************************************************************************************/

package Model.CyclicBarrier;

import Model.Exception.DivByZeroException;
import Model.Exception.EvaluationException;
import Model.Exception.InexistentSymbolException;
import Model.Expression.Expression;
import Model.Statement.PrgState;
import Model.Statement.Statement;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class NewBarrierStmt implements Statement
{
    private String var;
    private Expression expr;
    private Integer nextFree = 0;

    public NewBarrierStmt(String v, Expression e)
    {
        var = v;
        expr = e;
    }

    @Override
    public PrgState execute(PrgState p)
    {
        try
        {
            if (expr.Eval(p.getSymbolT(), p.getHeap()) != 0)
            {
                synchronized (p.getCyclicBarrier())
                {
                    p.getCyclicBarrier().add(nextFree,new Pair(expr.Eval(p.getSymbolT(), p.getHeap()), new ArrayList<Integer>()));
                }
                if (p.getSymbolT().contains(var))
                    p.getSymbolT().update(var,nextFree);
                else
                    p.getSymbolT().add(var,nextFree);
            }
            nextFree++;
            return null;
        }
        catch(InexistentSymbolException | DivByZeroException | EvaluationException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "NewBarrierStmt(" + var + ", "+ expr + ")";
    }
}
