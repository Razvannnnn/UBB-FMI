using Problema8_FX_Csharp.Repository;

namespace Problema8_FX_Csharp;

static class Program
{
    /// <summary>
    ///  The main entry point for the application.
    /// </summary>
    [STAThread]
    static void Main()
    {
        // To customize application configuration such as set high DPI settings or default font,
        // see https://aka.ms/applicationconfiguration.
        ApplicationConfiguration.Initialize();
        
        IRepoUser repoUser = new RepoUser();
        IRepoAgeGroup repoAgeGroup = new RepoAgeGroup();
        IRepoEvent repoEvent = new RepoEvent();
        IRepoEnrollment repoEnrollment = new RepoEnrollment();
        IRepoChild repoChild = new RepoChild();
        Service.Service service = new Service.Service(repoAgeGroup, repoChild, repoEnrollment, repoEvent, repoUser);
        
        Application.Run(new Login(service));
    }
}