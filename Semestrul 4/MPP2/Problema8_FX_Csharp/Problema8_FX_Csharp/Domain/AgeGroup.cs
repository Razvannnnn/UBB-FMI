namespace Problema8_FX_Csharp.Domain;

public class AgeGroup: Entity<long>
{
    public String Name { get; set; }
    public int MinAge { get; set; }
    public int MaxAge { get; set; }
    
    public AgeGroup(long id, String name, int minAge, int maxAge)
    {
        SetId(id);
        Name = name;
        MinAge = minAge;
        MaxAge = maxAge;
    }
    
    public Tuple<int, int> GetAgeRange()
    {   
        return new Tuple<int, int>(MinAge, MaxAge);
    }
    
    public override String ToString()
    {
        return $"Name: {Name}, MinAge: {MinAge}, MaxAge: {MaxAge}";
    }
    
    public override bool Equals(Object obj)
    {
        if (obj == null || GetType() != obj.GetType())
        {
            return false;
        }
        
        AgeGroup ageGroup = (AgeGroup)obj;
        return Name.Equals(ageGroup.Name) && MinAge == ageGroup.MinAge && MaxAge == ageGroup.MaxAge;
    }
    
    
}