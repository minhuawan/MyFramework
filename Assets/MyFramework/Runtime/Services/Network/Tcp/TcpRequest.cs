namespace MyFramework.Services.Network.Tcp
{
    public class TcpRequest<T> where T : TcpResponse
    {
        public virtual int MessageId { get; }
    }
}