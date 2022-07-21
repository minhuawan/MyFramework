local View = require("core.ui.mvp.View")
---@class MainView: View
local M = class("MainView", View)

function M:initialize(model)
    local times = 0
    self._vars = require("ui.configuration.vars.main").attach(self.binder)
    self._vars.ButtonViews.buttonOk.onClick:AddListener(function()
        times = times + 1
        log.debug("main view button clicked {}", times)
        if times > 5 then
            self._vars.ButtonViews.buttonOk.onClick:RemoveAllListeners()
        end
    end)
end

function M:setTitle(str)
    self._vars.Texts.title.text = str
end

return M