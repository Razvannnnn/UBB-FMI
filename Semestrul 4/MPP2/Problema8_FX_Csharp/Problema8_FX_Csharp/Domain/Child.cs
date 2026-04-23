using Problema8_FX_Csharp.Utils;

namespace Problema8_FX_Csharp.Domain;

public class Child : Entity<long>
{
    public String Name { get; set; }
    public String CNP { get; set; }
    
    public Child(long id, String name, String cnp)
    {
        SetId(id);
        Name = name;
        CNP = cnp;
    }

    public int getAge()
    {
        return AgeConverter.getAgeFromCNP(CNP);
    }
    
    public override String ToString()
    {
        return $"Name: {Name}, CNP: {CNP}";
    }
    
    public override bool Equals(Object obj)
    {
        if (obj == null || GetType() != obj.GetType())
        {
            return false;
        }
        
        Child child = (Child)obj;
        return Name.Equals(child.Name) && CNP.Equals(child.CNP);
    }
}