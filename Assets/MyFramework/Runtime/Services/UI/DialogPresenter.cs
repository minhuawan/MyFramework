using System;
using System.Threading.Tasks;
using UniRx;

namespace MyFramework.Services.UI
{
    public abstract class DialogPresenter : Presenter
    {
        public abstract IObservable<Unit> CloseEvent { get; }
    }
}