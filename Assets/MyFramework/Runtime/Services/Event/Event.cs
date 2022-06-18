namespace MyFramework.Runtime.Services.Event
{
    public abstract class Event
    {
        public void Dispatch()
        {
            Application.GetService<EventService>().Dispatch(this);
        }

        public T As<T>() where T : Event
        {
            return this as T;
        }
    }
}