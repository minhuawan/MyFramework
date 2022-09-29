---@class StartView: View
local M = class("StartView", require("app.ui.base.mvp.View"))

function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.StartViewVars").attach(self.binder)
    self.disposable:append(self._vars)
    self.backEvent = self._vars.ButtonViews.btnBack.clickEvent

    local e1 = self._vars.ButtonViews.menuNormal.clickEvent:selectTo('normal')
    local e2 = self._vars.ButtonViews.menuNormal2.clickEvent:selectTo('normal2')
    local e3 = self._vars.ButtonViews.menuNormal3.clickEvent:selectTo('normal3')
    self.modeEvent = e1:merge(e2, e3)
end
return M
