using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Sockets;
using System.Threading;
using System.Net;


namespace FuturesAndContinuations
{
    class StateOfObject
    {

        public string hostname;

        public string endpoint;

        public IPEndPoint remoteEndpoint;

        public ManualResetEvent connectDone = new ManualResetEvent(false);

        public ManualResetEvent sendDone = new ManualResetEvent(false);

        public ManualResetEvent receiveDone = new ManualResetEvent(false);

        public Socket socket = null;

        public const int BUFFER_SIZE = 512;

        public byte[] buffer = new byte[BUFFER_SIZE];

        public StringBuilder responseContent = new StringBuilder();

        public int id;


    }
}