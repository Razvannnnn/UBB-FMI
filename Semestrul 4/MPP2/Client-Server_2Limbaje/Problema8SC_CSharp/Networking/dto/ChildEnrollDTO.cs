namespace Networking.dto;

using Problema8SC_CSharp.Model;

public class ChildEnrollDTO
{
    public Child Child { get; set; }
    public Enrollment Enrollment { get; set; }

    public ChildEnrollDTO(Child child, Enrollment enrollment)
    {
        Child = child;
        Enrollment = enrollment;
    }
}
