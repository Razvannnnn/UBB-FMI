namespace Problema8SC_CSharp.Model;

public class Entity<ID>
{
    private ID Id = default!;
    
    public ID GetId()
    {
        return Id;
    }
    public void SetId(ID id)
    {
        Id = id;
    }
}