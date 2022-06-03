namespace MyFramework.Services.Network.Tcp
{
    public class TcpRequest<T> : TcpProtocol where T : TcpResponse
    {
        public string message;
    }
}