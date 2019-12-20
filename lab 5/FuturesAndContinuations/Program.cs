using System;
using System.Collections.Generic;
using FuturesAndContinuations;


namespace FuturesAndContinuations
{
    class Program
    {
        // adding 3 hosts, each returning a response in different format:
        private static readonly List<string> HOSTS = new List<string> {
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp",
            "www.digisport.ro",
        };


        static void Main(string[] args)
        {
            Console.WriteLine("-------------------------");
            DirectCallbacks.run(HOSTS);
            TasksMechanism.run(HOSTS);
            AsyncTasksMechanism.run(HOSTS);
            Console.WriteLine("-------------------------");
        }
    }
}