local View = require("core.ui.mvp.View")
---@class MainView: View
local M = class("MainView", View)

function M:initialize(model)
    self._times = 0
    self._vars = require("ui.configuration.vars.main").attach(self.binder)
    self._vars.ButtonViews.buttonOk.onClick:AddListener(bind(self.onClick, self))
end


function M:onClick()
    self._times = self._times + 1
    log.debug("main view button clicked {}", self._times)
    if self._times > 5 then
        self._vars.ButtonViews.buttonOk.onClick:RemoveAllListeners()
    end
end

function M:setTitle(str)
    self._vars.Texts.title.text = str
end

function M:dispose()
    self._vars.ButtonViews.buttonOk.onClick:RemoveAllListeners()
    self._vars.ButtonViews.buttonOk.onClick:Invoke()
end

return M