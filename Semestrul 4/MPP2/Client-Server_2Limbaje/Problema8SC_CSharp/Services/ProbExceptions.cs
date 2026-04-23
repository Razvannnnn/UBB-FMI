namespace Problema8SC_CSharp.Services;

public class ProbExceptions : Exception
{
    public ProbExceptions(): base() { }
    public ProbExceptions(string message) : base(message) { }
    public ProbExceptions(string message, Exception innerException) : base(message, innerException) { }
}