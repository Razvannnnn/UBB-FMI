using System;
using System.Configuration;
using System.Reflection;
using System.Windows.Forms;
using log4net;
using log4net.Config;
using Problema8SC_CSharp.Services;
using Networking.jsonprotocol;


namespace Client
{
    internal static class StartClient
    {
        private static int DEFAULT_PORT = 55556;
        private static string DEFAULT_IP = "127.0.0.1";
        private static readonly ILog log = LogManager.GetLogger(typeof(StartClient));

        [STAThread]
        static void Main(string[] args)
        {
            // Configure logging
            var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
            XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

            log.Debug("Reading properties from app.config ...");
            
            int port = DEFAULT_PORT;
            string ip = DEFAULT_IP;

            string portS = ConfigurationManager.AppSettings["port"];
            if (portS != null)
            {
                if (!int.TryParse(portS, out port))
                {
                    log.DebugFormat("Port property not a number. Using default value {0}", DEFAULT_PORT);
                    port = DEFAULT_PORT;
                }
            }
            else
            {
                log.DebugFormat("Port property not set. Using default value {0}", DEFAULT_PORT);
            }

            string ipS = ConfigurationManager.AppSettings["ip"];
            if (ipS != null)
            {
                ip = ipS;
            }
            else
            {
                log.DebugFormat("IP property not set. Using default value {0}", DEFAULT_IP);
            }

            log.InfoFormat("Using server on IP {0} and port {1}", ip, port);

            // Create service proxy
            IServices server = new ServicesJsonProxy(ip, port);

            Application.Run(new Login.Login(server));
        }
    }
}
