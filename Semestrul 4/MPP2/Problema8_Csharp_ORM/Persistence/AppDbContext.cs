using Problema8SC_CSharp.Model;
using Microsoft.EntityFrameworkCore;

namespace Persistence;

public class AppDbContext : DbContext
{
    public DbSet<AgeGroup> AgeGroups { get; set; }
    public DbSet<User> Users { get; set; }
    
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }
    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<AgeGroup>()
            .HasKey(a => a.Id);
        
        modelBuilder.Entity<User>()
            .HasKey(u => u.Id);
        
        base.OnModelCreating(modelBuilder);
    }
}