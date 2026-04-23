using TestCSharp;

class Program
{
    static async Task Main(string[] args)
    {
        var client = new EventClient();

        var newEvent = new Event
        {
            Name = "Marathon",
            Distance = 42,
            AgeGroupId = 1
        };

        try
        {
            Console.WriteLine("Creating event...");
            var created = await client.CreateAsync(newEvent);
            Console.WriteLine($"Created event: {created.Id} - {created.Name}");

            Console.WriteLine("\nGetting all events:");
            var events = await client.GetAllAsync();
            foreach (var ev in events)
                Console.WriteLine($"{ev.Id}: {ev.Name} ({ev.Distance} km)");

            Console.WriteLine($"\nGetting event by ID: {created.Id}");
            var found = await client.GetByIdAsync(created.Id.Value);
            Console.WriteLine($"Found: {found.Name}");

            Console.WriteLine("\nUpdating event distance to 21...");
            created.Distance = 21;
            await client.UpdateAsync(created);
            Console.WriteLine("Update successful");
            
            var updated = await client.GetByIdAsync(created.Id.Value);
            Console.WriteLine($"Updated event: {updated.Id} - {updated.Name} ({updated.Distance} km)");
            
            Console.WriteLine($"\nDeleting event {created.Id}...");
            await client.DeleteAsync(created.Id.Value);
            Console.WriteLine("Delete successful");
        }
        catch (HttpRequestException ex)
        {
            Console.WriteLine($"HTTP error: {ex.Message}");
        }
    }
}