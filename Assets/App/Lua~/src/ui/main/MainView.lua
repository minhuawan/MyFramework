local View = require("core.ui.mvp.View")
---@class MainView: View
local M = class("MainView", View)

function M:initialize(model)
    self._button = xxx
    self._button.OnClick:AddListener(function()
        if model and model.onButtonClick then
            model.onButtonClick()
        end
    end)
end

function M:setTitle(str)
end

return M