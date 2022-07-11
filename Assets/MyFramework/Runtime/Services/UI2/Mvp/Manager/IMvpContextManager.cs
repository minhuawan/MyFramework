namespace MyFramework.Runtime.Services.UI2
{
    public interface IMvpContextManager
    {
        public void Abort(MvpContext context, string message);
        public void Back();
    }
}