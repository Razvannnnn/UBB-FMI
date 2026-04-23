using Microsoft.Data.SqlClient;
using System;
using System.Threading;

namespace Tema4
{
    internal class Program
    {
        static readonly string connStr =
            "Server=ROG_RAZVAN\\SQLEXPRESS;Database=BazaDateSGBDTema4_test;Integrated Security=true;TrustServerCertificate=true;";
        
        static readonly int maxIncercari = 2;

        static void Main(string[] args)
        {
            Thread t1 = new Thread(() => RuleazaTranzactie("deadlock_11", "Tranzactia 1"));
            Thread t2 = new Thread(() => RuleazaTranzactie("deadlock_22", "Tranzactia 2"));

            t1.Start();
            t2.Start();
            t1.Join();
            t2.Join();

            Console.WriteLine("Apasa <ENTER> pentru iesire...");
            Console.ReadKey();
        }

        static void RuleazaTranzactie(string procedura, string numeTranzactie)
        {
            int incercari = 0;
            bool reusit = false;

            while (!reusit && incercari < maxIncercari)
            {
                reusit = ExecutaProcedura(procedura, numeTranzactie);
                incercari++;
            }

            if (!reusit)
                Console.WriteLine($"{numeTranzactie} oprita.");
        }

        static bool ExecutaProcedura(string procedura, string numeTranzactie)
        {
            bool ok = false;
            Console.WriteLine($"{numeTranzactie} pornita...");

            using (SqlConnection conexiune = new SqlConnection(connStr))
            using (SqlCommand comanda = conexiune.CreateCommand())
            {
                try
                {
                    conexiune.Open();
                    comanda.CommandText = $"EXECUTE {procedura}";
                    comanda.ExecuteNonQuery();
                    Console.WriteLine($"{numeTranzactie} terminata!");
                    ok = true;
                }
                catch (SqlException ex) when (ex.Number == 1205)
                {
                    Console.WriteLine($"{numeTranzactie}: exceptie tip {ex.GetType()}");
                    Console.WriteLine($"Mesaj: {ex.Message}");
                }
            }

            return ok;
        }
    }
}
