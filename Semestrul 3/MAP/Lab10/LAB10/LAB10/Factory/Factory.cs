namespace LAB10.Factory;

public interface Factory
{
    Container.Container createContainer(Enum.ContainerStrategy strategy);
}