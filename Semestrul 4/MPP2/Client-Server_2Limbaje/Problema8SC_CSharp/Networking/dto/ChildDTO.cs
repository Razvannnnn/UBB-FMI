namespace Networking.dto;

[Serializable]
public class ChildDTO
{
    public string Name { get; set; }
    public string CNP { get; set; }

    public ChildDTO(string name, string cnp)
    {
        Name = name;
        CNP = cnp;
    }

    public override string ToString()
    {
        return $"ChildDTO[{Name} {CNP}]";
    }
}
