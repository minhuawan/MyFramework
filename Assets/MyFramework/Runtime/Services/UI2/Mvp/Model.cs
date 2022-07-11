namespace MyFramework.Runtime.Services.UI2
{
    public abstract class Model
    {
        public T As<T>() where T : Model
        {
            return this as T;
        }
    }
}