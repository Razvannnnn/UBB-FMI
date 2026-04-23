using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Problema8SC_CSharp.Model;

[Table("child")]
public class Child : Entity<long>
{
    [Column("child_id", TypeName = "INTEGER")]
    public long Id
    {
        get => GetId();
        set => SetId(value);
    }
    [Column("name", TypeName = "VARCHAR(255)")]
    public String Name { get; set; }
    
    [Column("CNP", TypeName = "VARCHAR(13)")]
    public String CNP { get; set; }
    
    public Child(long id, String name, String cnp)
    {
        Id = id;
        Name = name;
        CNP = cnp;
    }
    
    public Child() { }

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