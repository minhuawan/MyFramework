namespace MyFramework.Services.Network.Tcp
{
    public interface ITcpProtocolHandler<T> where T : TcpProtocol
    {
        public void OnTcpProtocol(T protocol);
    }
}