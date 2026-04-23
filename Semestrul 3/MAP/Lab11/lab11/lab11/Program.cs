using lab11.Console;
using lab11.Domain;
using lab11.Service;
using lab11.Repository.File;


namespace lab11;

public class Program
{
    public static void Main(string[] args)
    {
        EchipaFile echipaFile = new EchipaFile("C:\\Users\\razva\\Desktop\\MAP\\Lab11\\lab11\\lab11\\echipe.txt");
        ElevFile elevFile = new ElevFile("C:\\Users\\razva\\Desktop\\MAP\\Lab11\\lab11\\lab11\\elevi.txt");
        MeciFile meciFile = new MeciFile(echipaFile, "C:\\Users\\razva\\Desktop\\MAP\\Lab11\\lab11\\lab11\\meciuri.txt");
        JucatorFile jucatorFile = new JucatorFile(echipaFile, "C:\\Users\\razva\\Desktop\\MAP\\Lab11\\lab11\\lab11\\jucatori.txt");
        JucatorActiveFile jucatorActivFile = new JucatorActiveFile("C:\\Users\\razva\\Desktop\\MAP\\Lab11\\lab11\\lab11\\jucatori_activi.txt");
        
        Service.Service service = new Service.Service(echipaFile, jucatorFile, meciFile, elevFile, jucatorActivFile);
        
        Consola consola = new Consola(service);
        consola.run();
        
        //1. ID Echipa:
        //a0313f86-3368-4613-b9c3-dec5e2b05d64
        
        //2.ID Echipa:
        //
        //ID Meci:
        //117324cb-0870-4212-8ed1-aae0e5b18972

        //3. Data de inceput:
        //01/01/2024 
        //Data de sfarsit:
        //10/10/2025
        
        //4. ID Meci:
        //117324cb-0870-4212-8ed1-aae0e5b18972


    }
}

