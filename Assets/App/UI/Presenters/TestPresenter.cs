using System.Threading.Tasks;
using App.UI.Views;
using MyFramework.Services.UI;

namespace App.UI.Presenters
{
    public class TestPresenter : Presenter
    {
        private TestView view;
        public override View View => view;

        public override async Task<TransitionResult> LoadAsync(PresenterLocatorParameters parameters)
        {
            view = await TestView.LoadAsync();
            view.Initialize(parameters.Get<string>("title"));
            return TransitionResult.Ok;
        }
    }
}