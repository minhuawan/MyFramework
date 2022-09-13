local ButtonGroupEvent = require("core.ui.ButtonGroupEvent")
local ButtonEvent = require("core.ui.ButtonEvent")
---@class StartView: View
local M = class("StartView", require("core.ui.mvp.View"))

function M:initialize(model)
    self._vars = require("ui.configuration.vars.StartViewVars").attach(self.binder)
    self.closeEvent = ButtonEvent:create(self._vars.ButtonViews.btnBack)
    self.modeEvent = ButtonGroupEvent:create({
        self._vars.ButtonViews.menu1
    })
end

function M:dispose()
    self.closeEvent:dispose()
    self.modeEvent:dispose()
end

return M
