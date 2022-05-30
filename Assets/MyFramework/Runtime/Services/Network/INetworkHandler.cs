using System;
using System.Threading.Tasks;

namespace MyFramework.Services.Network
{
    public interface INetworkHandler
    {
        public Task<T> SendAsync<T>(INetworkRequest request) where T : INetworkResponse;
    }
}