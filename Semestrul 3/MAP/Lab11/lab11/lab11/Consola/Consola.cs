using lab11.Domain;

namespace lab11.Console;

using lab11.Service;

public class Consola
{
    private Service service;
    public Consola(Service service)
    {
        this.service = service;
    }

    public void run()
    {
        while (true)
        {
            System.Console.WriteLine("\n");
            System.Console.WriteLine("1. Afiseaza toti jucatorii unei echipe");
            System.Console.WriteLine("2. Afiseaza toti jucatorii activi ai unei echipe la un meci");
            System.Console.WriteLine("3. Afiseaza toate meciurile dintr-o anumita perioda");
            System.Console.WriteLine("4. Scorul unui meci");
            System.Console.WriteLine("0. Exit\n");
            System.Console.WriteLine("Optiune: ");
            try
            {
                int cmd = int.Parse(System.Console.ReadLine());
                Echipa echipa;
                Meci meci;
                switch (cmd)
                {
                    case 0:
                        return;
                    case 1:
                        System.Console.WriteLine("ID Echipa: ");
                        echipa = service.findEchipa(Guid.Parse(System.Console.ReadLine()));
                        System.Console.WriteLine($"Jucatorii echipei {echipa.Nume} sunt: ");
                        foreach (var j in service.JucatoriAiUneiEchipe(echipa))
                            System.Console.WriteLine(j.Nume + "," + j.Scoala);
                        break;
                    case 2:
                        System.Console.WriteLine("ID Echipa: ");
                        echipa = service.findEchipa(Guid.Parse(System.Console.ReadLine()));
                        System.Console.WriteLine("ID Meci: ");
                        meci = service.findMeci(Guid.Parse(System.Console.ReadLine()));
                        foreach (var j in service.JucatoriActiviLaUnMeciAiUneiEchipe(echipa, meci))
                            System.Console.WriteLine(j.Nume + "," + j.Scoala);
                        break;
                    case 3:
                        DateTime d1;
                        DateTime d2;
                        System.Console.WriteLine("Data de inceput: ");
                        d1 = DateTime.Parse(System.Console.ReadLine());
                        System.Console.WriteLine("Data de sfarsit: ");
                        d2 = DateTime.Parse(System.Console.ReadLine());
                        foreach (var m in service.MeciuriDintrOPerioada(d1, d2))
                            System.Console.WriteLine(m.echipa1.Nume + "-" + m.echipa2.Nume + ", " + m.data);
                        break;
                    case 4:
                        System.Console.WriteLine("ID Meci: ");
                        meci = service.findMeci(Guid.Parse(System.Console.ReadLine()));
                        string scor = service.ScorLaUnMeci(meci);
                        System.Console.WriteLine(meci.echipa1.Nume + " - " + meci.echipa2.Nume + " a avut scorul: " + scor);
                        break;
                }
            }
            catch
            {
                System.Console.WriteLine("Introduceti o comanda valida!");
            }
        }
    }
}