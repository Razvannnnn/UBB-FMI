using Networking.dto;
using Problema8SC_CSharp.Model;

namespace Networking.jsonprotocol;

public class Request
{
    public long Type { get; set; }
    public UserDTO User { get; set; }
    public AgeGroupDTO AgeGroup { get; set; }
    public long AgeGroupId { get; set; }
    public string Nume { get; set; }
    public string Cnp { get; set; }
    public Event EventName1 { get; set; }
    public long Id { get; set; }

    public Request() { }
}
