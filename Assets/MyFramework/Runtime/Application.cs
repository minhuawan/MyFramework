using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading;
using System.Threading.Tasks;
using App.StateMachine;
using MyFramework.Runtime.Services;
using MyFramework.Runtime.Services.StateMachine;
using UnityEngine;
using UnityEngine.SceneManagement;

namespace MyFramework
{
    public static class Application
    {
        private static Dictionary<Type, AbstractService> services;

        [RuntimeInitializeOnLoadMethod(RuntimeInitializeLoadType.BeforeSceneLoad)]
        private static void startup()
        {
            var scene = SceneManager.GetActiveScene();
            if (scene == null || !scene.name.ToLower().Equals("main"))
                return;
            Initialize();
            UnityEngine.Application.quitting += OnUnityAppQuit;
            TaskScheduler.UnobservedTaskException += UnobservedTaskExceptionHandler;
        }

        private static void OnUnityAppQuit()
        {
#if UNITY_EDITOR
            var constructor = SynchronizationContext.Current.GetType()
                .GetConstructor(BindingFlags.NonPublic | BindingFlags.Instance, null, new Type[] {typeof(int)}, null);
            var newContext = constructor.Invoke(new object[] {Thread.CurrentThread.ManagedThreadId});
            SynchronizationContext.SetSynchronizationContext(newContext as SynchronizationContext);
#endif

            foreach (var service in services.Values)
            {
                service.OnDestroy();
            }

            services.Clear();
        }

        private static void UnobservedTaskExceptionHandler(object sender, UnobservedTaskExceptionEventArgs args)
        {
            Debug.LogError("UnobservedTaskException " + args.Exception);
        }


        private static void Initialize()
        {
            RegisterServices();
            GetService<StateMachineService>().ChangeState<SplashStateMachine>();
        }

        private static void RegisterServices()
        {
            services = new Dictionary<Type, AbstractService>();
            var baseType = typeof(AbstractService);
            var serviceTypes = Assembly.GetExecutingAssembly().GetTypes()
                .Where(type => type.BaseType == baseType && type.Namespace.StartsWith("MyFramework.Runtime.Services."));
            foreach (var serviceType in serviceTypes)
            {
                var service = Activator.CreateInstance(serviceType) as AbstractService;
                services[serviceType] = service;
            }

            var ordered = services.Values.OrderBy(service => service.CreatePriority);
            foreach (var serviceManager in ordered)
            {
                serviceManager.OnCreated();
            }

            foreach (var service in ordered)
            {
                service.Initialize();
            }
        }

        public static T GetService<T>() where T : AbstractService
        {
            if (services.TryGetValue(typeof(T), out var manager))
            {
                return manager as T;
            }

            throw new Exception($"service not registration, type: {typeof(T)}");
        }
    }
}