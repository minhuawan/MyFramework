local ButtonGroupEvent = require("app.ui.base.ButtonGroupEvent")
local ButtonEvent = require("app.ui.base.ButtonEvent")
---@class StartView: View
local M = class("StartView", require("app.ui.base.mvp.View"))

function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.StartViewVars").attach(self.binder)
    self.closeEvent = ButtonEvent(self._vars.ButtonViews.btnBack):addTo(self.disposable)

    local bts = { self._vars.ButtonViews.menu1 }
    local peer = nil
    self.modeEvent = ButtonGroupEvent(bts, peer):addTo(self.disposable)
end
return M
