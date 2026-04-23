using System.ComponentModel.DataAnnotations.Schema;

namespace Problema8SC_CSharp.Model;

[Table("age_group")]
public class AgeGroup: Entity<long>
{
    [Column("age_group_id", TypeName = "INTEGER")]
    public long Id
    {
        get => GetId();
        set => SetId(value);
    }
    [Column("name", TypeName = "VARCHAR(50)")]
    public String Name { get; set; }
    [Column("min_age", TypeName = "INT")]
    public int MinAge { get; set; }
    [Column("max_age", TypeName = "INT")]
    public int MaxAge { get; set; }
    
    public AgeGroup(long id, String name, int minAge, int maxAge)
    {
        Id = id;
        Name = name;
        MinAge = minAge;
        MaxAge = maxAge;
    }
    
    public AgeGroup() {}
    
    public AgeGroup(String name, int minAge, int maxAge) : this(0, name, minAge, maxAge)
    {
    }

    public Tuple<int, int> GetAgeRange()
    {
        return new Tuple<int, int>(MinAge, MaxAge);
    }
    
    public override int GetHashCode()
    {
        return Name.GetHashCode();
    }

    public override bool Equals(object? obj)
    {
        if (this == obj) return true;
        if (obj == null || GetType() != obj.GetType()) return false;

        AgeGroup other = (AgeGroup)obj;
        return Name == other.Name && MinAge == other.MinAge && MaxAge == other.MaxAge;
    }

    public override String ToString()
    {
        return $"Name: {Name}, MinAge: {MinAge}, MaxAge: {MaxAge}";
    }
}