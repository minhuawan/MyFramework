using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using App.StateMachine;
using MyFramework.Services;
using MyFramework.Services.Resource;
using MyFramework.Services.StateMachine;
using UnityEngine;
using UnityEngine.LowLevel;

namespace MyFramework
{
    public static class Application
    {
        [RuntimeInitializeOnLoadMethod(RuntimeInitializeLoadType.BeforeSceneLoad)]
        private static void startup()
        {
            Initialize();
            UnityEngine.Application.quitting += OnUnityAppQuit;
        }

        private static Dictionary<Type, AbstractService> services;

        private static void Initialize()
        {
            RegisterServices();
            GetService<StateMachineService>().ChangeState<LaunchStateMachine>();
        }

        private static void RegisterServices()
        {
            services = new Dictionary<Type, AbstractService>();
            var baseType = typeof(AbstractService);
            var serviceTypes = Assembly.GetExecutingAssembly().GetTypes()
                .Where(type => type.BaseType == baseType && type.Namespace.StartsWith("MyFramework.Services."));
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
        }

        private static void OnUnityAppQuit()
        {
            foreach (var service in services.Values)
            {
                service.OnDestroy();
            }
            services.Clear();
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