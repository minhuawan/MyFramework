


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
            // Initialize();
            // UnityEngine.Application.quitting += OnUnityAppQuit;

            // var playerLoop = PlayerLoop.GetCurrentPlayerLoop();
            // var target = typeof(UnityEngine.PlayerLoop.Update);
            // var updateLoopIndex = Array.FindIndex(playerLoop.subSystemList, s => s.type == target);
            // var updateLoop = playerLoop.subSystemList[updateLoopIndex];
            // var subSystemList = updateLoop.subSystemList.ToList();
            // subSystemList.Add(new PlayerLoopSystem()
            // {
            //     type = typeof(Application),
            //     updateDelegate = () =>
            //     {
            //         Debug.LogError("MySelf subsystem update");   
            //     }
            // });
            // var array = subSystemList.ToArray();
            // updateLoop.subSystemList = array;
            // playerLoop.subSystemList[updateLoopIndex] = updateLoop;
            PlayerLoop.SetPlayerLoop(new PlayerLoopSystem()
            {
                subSystemList = new PlayerLoopSystem[]{},
                type = typeof(Application),
                updateDelegate = () =>
                {
                    Debug.LogError("MySelf subsystem update");   
                }
            });
            
            // var playerloop = PlayerLoop.GetCurrentPlayerLoop();
            // var loop = new PlayerLoopSystem
            // {
            //     type = typeof(Application),
            //     updateDelegate = ()=>
            //     {
            //         Debug.LogError("MySelf subsystem update");   
            //     }
            // };
            // //1. 找到 Update Loop System
            // int index = Array.FindIndex(playerloop.subSystemList, v => v.type == typeof(UnityEngine.PlayerLoop.Update));
            // //2.  将咱们的 loop 插入到 Update loop 中
            // var updateloop = playerloop.subSystemList[index];
            // var temp = updateloop.subSystemList.ToList();
            // temp.Add(loop);
            // updateloop.subSystemList = temp.ToArray();
            // playerloop.subSystemList[index] = updateloop;
            // //3. 设置自定义的 Loop 到 Unity 引擎
            // PlayerLoop.SetPlayerLoop(playerloop);
            
            
            // int ident = 0;
            // void ShowSystem(PlayerLoopSystem system)
            // {
            //     ident++;
            //     foreach (var item in system.subSystemList)
            //     {
            //         Debug.Log($"{new string('\t',ident)}{item .type}");
            //         if (item.subSystemList?.Length>0)
            //         {
            //             ShowSystem(item);
            //         }
            //     }
            //     ident--;
            // }
            // var system = PlayerLoop.GetCurrentPlayerLoop();
            // ShowSystem(system);
        }

        private static Dictionary<Type, AService> services;

        private static void Initialize()
        {
            RegisterServices();
            GetService<StateMachineService>().ChangeState<LaunchStateMachine>();
        }

        private static void RegisterServices()
        {
            services = new Dictionary<Type, AService>();
            var baseType = typeof(AService);
            var serviceTypes = Assembly.GetExecutingAssembly().GetTypes()
                .Where(type => type.BaseType == baseType && type.Namespace.StartsWith("MyFramework.Services."));
            foreach (var serviceType in serviceTypes)
            {
                var service = Activator.CreateInstance(serviceType) as AService;
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

        public static T GetService<T>() where T : AService
        {
            if (services.TryGetValue(typeof(T), out var manager))
            {
                return manager as T;
            }

            throw new Exception($"service not registration, type: {typeof(T)}");
        }
    }
}