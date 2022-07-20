local View = require("core.ui.mvp.View")
---@class MainView: View
local M = class("MainView", View)

function M:initialize(model)
    self._vars = require("ui.configuration.vars.main").attach(self.binder)
    self._vars.buttonViews.buttonOk.OnClick:AddListener(function()
        if model and model.onButtonClick then
            model.onButtonClick()
        end
    end)
end

function M:setTitle(str)
end

return M