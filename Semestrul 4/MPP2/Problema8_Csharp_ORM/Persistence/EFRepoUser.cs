using Microsoft.EntityFrameworkCore;
using Problema8SC_CSharp.Model;
using Problema8SC_CSharp.Persistence;

namespace Persistence;

public class EFRepoUser : IRepoUser
{
    private readonly AppDbContext _context;
    public EFRepoUser(AppDbContext context)
    {
        _context = context;
    }

    public User FindOne(long id) => _context.Users.Find(id);

    public IEnumerable<User> FindAll() => _context.Users.ToList();

    public void Save(User entity)
    {
        _context.Users.Add(entity);
        _context.SaveChanges();
    }

    public void Delete(long id)
    {
        var user = _context.Users.Find(id);
        if (user != null)
        {
            _context.Users.Remove(user);
            _context.SaveChanges();
        }
    }

    public void Update(User entity)
    {
        _context.Users.Update(entity);
        _context.SaveChanges();
    }

    public User Login(string username, string password) => _context.Users.FirstOrDefault(u => u.Username == username && u.Password == password);
}