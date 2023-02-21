---@class StartView: View
local M = class("StartView", require("app.ui.base.mvp.View"))

function M:initialize(model)
    self._vars = require("app.ui.generated.StartViewVars").attach(self.binder)
    self.disposable:append(self._vars)
    self.backEvent = self._vars.ButtonViews.btnBack.clickEvent

    self.modeEvent = self._vars.ButtonViews.menuNormal.clickEvent:selectTo('normal')
end
return M
