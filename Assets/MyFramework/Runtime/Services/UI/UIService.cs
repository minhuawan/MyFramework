﻿using System.Collections.Generic;
using App.UI.View.Launch;
using MyFramework.Services.Resource;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public class UIService : AbstractService
    {
        private Stack<UIPresenter> stack;

        public override void OnCreated()
        {
            stack = new Stack<UIPresenter>();
        }

        public override void OnDestroy()
        {
            foreach (var uiPresenter in stack)
            {
                uiPresenter.OnDestroy();
            }

            stack.Clear();
        }

        public T Open<T>() where T : UIPresenter, new()
        {
            var presenter = new T();
            var resourceService = Application.GetService<ResourceService>();
            var schemaPath = UIPresenter.GetPrefabSchemaPath<T>();
            var resourceObject = resourceService.GetResourceObject(schemaPath);
            var uiView = resourceObject.CreateObject<UIView>();
            presenter.OnCreated(uiView);
            presenter.OnOpened();
            stack.Push(presenter);
            return presenter;
        }

        public void Back()
        {
            if (stack.Count > 0)
            {
                var top = stack.Pop();
                top.OnClose();
                top.OnDestroy();
            }
        }
    }
}