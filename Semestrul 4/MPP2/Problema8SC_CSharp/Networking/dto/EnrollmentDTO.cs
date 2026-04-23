namespace Networking.dto;

[Serializable]
public class EnrollmentDTO
{
    public long ChildId { get; set; }
    public long EventId { get; set; }

    public EnrollmentDTO(long childId, long eventId)
    {
        ChildId = childId;
        EventId = eventId;
    }

    public override string ToString()
    {
        return $"EnrollmentDTO[{ChildId} {EventId}]";
    }
}
