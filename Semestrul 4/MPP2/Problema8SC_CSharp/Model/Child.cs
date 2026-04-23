namespace Problema8SC_CSharp.Model;

public class Child : Entity<long>
{
    public long Id
    {
        get => GetId();
        set => SetId(value);
    }
    public String Name { get; set; }
    public String CNP { get; set; }
    
    public Child(long id, String name, String cnp)
    {
        Id = id;
        Name = name;
        CNP = cnp;
    }

    public int getAge()
    {
        return 0;
        //return AgeConverter.getAgeFromCNP(CNP);
    }
    
    public override String ToString()
    {
        return $"Name: {Name}, CNP: {CNP}";
    }
    
    public override bool Equals(object? obj)
    {
        if (obj == null || GetType() != obj.GetType())
        {
            return false;
        }
        
        Child child = (Child)obj;
        return Name.Equals(child.Name) && CNP.Equals(child.CNP);
    }

    protected bool Equals(Child other)
    {
        return Name == other.Name && CNP == other.CNP;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(Name, CNP);
    }
}