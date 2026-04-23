namespace Networking.dto;

[Serializable]
public class AgeGroupDTO
{
    public string Name { get; set; }
    public int MinAge { get; set; }
    public int MaxAge { get; set; }

    public AgeGroupDTO(string name, int minAge, int maxAge)
    {
        Name = name;
        MinAge = minAge;
        MaxAge = maxAge;
    }

    public override string ToString()
    {
        return $"AgeGroupDTO[{Name} {MinAge} {MaxAge}]";
    }
}
