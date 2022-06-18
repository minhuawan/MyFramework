using TMPro;

namespace MyFramework.Runtime.Services.Network.Tcp
{
    public class TcpLengthBasedFrame
    {
        public byte[] data { get; private set; } // the data does not contain length
        public int length { get; private set; }

        public TcpLengthBasedFrame(byte[] data)
        {
            this.data = data;
            length = data == null ? 0 : data.Length;
        }
    }
}