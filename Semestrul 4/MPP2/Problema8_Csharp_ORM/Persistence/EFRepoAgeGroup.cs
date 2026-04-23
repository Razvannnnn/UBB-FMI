using Microsoft.EntityFrameworkCore;
using Problema8SC_CSharp.Model;

namespace Persistence;

public class EFRepoAgeGroup : IRepoAgeGroup
{
    private readonly AppDbContext _context;

    public EFRepoAgeGroup(AppDbContext contextFactory)
    {
        _context = contextFactory;
    }

    public AgeGroup FindOne(long id) => _context.AgeGroups.Find(id);

    public IEnumerable<AgeGroup> FindAll() => _context.AgeGroups.ToList();

    public void Save(AgeGroup entity)
    {
        _context.AgeGroups.Add(entity);
        _context.SaveChanges();
    }

    public void Delete(long id)
    {
        var entity = _context.AgeGroups.Find(id);
        if (entity != null)
        {
            _context.AgeGroups.Remove(entity);
            _context.SaveChanges();
        }
    }

    public void Update(AgeGroup entity)
    {
        _context.AgeGroups.Update(entity);
        _context.SaveChanges();
    }
}