using System.Net.Http.Json;

namespace TestCSharp;

public class EventClient
{
    private readonly HttpClient _client;
    private const string BaseUrl = "http://localhost:8080/problema8/events";

    public EventClient()
    {
        _client = new HttpClient();
    }

    public async Task<Event> CreateAsync(Event ev)
    {
        var response = await _client.PostAsJsonAsync(BaseUrl, ev);
        response.EnsureSuccessStatusCode();
        return await response.Content.ReadFromJsonAsync<Event>();
    }

    public async Task<List<Event>> GetAllAsync()
    {
        return await _client.GetFromJsonAsync<List<Event>>(BaseUrl);
    }

    public async Task<Event> GetByIdAsync(long id)
    {
        return await _client.GetFromJsonAsync<Event>($"{BaseUrl}/{id}");
    }

    public async Task UpdateAsync(Event ev)
    {
        var response = await _client.PutAsJsonAsync($"{BaseUrl}/{ev.Id}", ev);
        response.EnsureSuccessStatusCode();
    }

    public async Task DeleteAsync(long id)
    {
        var response = await _client.DeleteAsync($"{BaseUrl}/{id}");
        response.EnsureSuccessStatusCode();
    }
}