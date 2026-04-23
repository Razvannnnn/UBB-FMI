namespace Networking.dto;

[Serializable]
public class EventDTO
{
    public string Name { get; set; }
    public int Distance { get; set; }
    public long AgeGroupId { get; set; }

    public EventDTO(string name, int distance, long ageGroupId)
    {
        Name = name;
        Distance = distance;
        AgeGroupId = ageGroupId;
    }

    public override string ToString()
    {
        return $"EventDTO[{Name} {Distance} {AgeGroupId}]";
    }
}
