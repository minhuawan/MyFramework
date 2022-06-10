using System.Threading.Tasks;
using App.UI.Views;
using MyFramework.Services.UI;

namespace App.UI.Presenters
{
    public class TestDialogPresenter : DialogPresenter
    {
        private TestDialogView _view;
        public override View View => _view;

        public override async Task<TransitionResult> LoadAsync(PresenterLocatorParameters parameters)
        {
            _view = await TestDialogView.LoadAsync();
            _view.SetTitle(parameters.Get("title", "no title"));
            return TransitionResult.Ok;
        }
    }
}