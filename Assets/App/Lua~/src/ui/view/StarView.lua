local ButtonGroupEvent = require("core.ui.ButtonGroupEvent")
local ButtonEvent = require("core.ui.ButtonEvent")
---@class StartView: View
local M = class("StartView", require("core.ui.mvp.View"))

function M:initialize(model)
    self._vars = require("ui.configuration.vars.StartViewVars").attach(self.binder)
    self.closeEvent = ButtonEvent(self._vars.ButtonViews.btnBack):addTo(self.disposable)

    local bts = { self._vars.ButtonViews.menu1 }
    local peer = nil
    self.modeEvent = ButtonGroupEvent(bts, peer):addTo(self.disposable)
end
return M
