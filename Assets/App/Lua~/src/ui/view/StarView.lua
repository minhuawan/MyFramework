local ButtonEvent = require("core.ui.ButtonEvent")
---@class StartView: View
local M = class("StartView", require("core.ui.mvp.View"))

function M:initialize(model)
    self._vars = require("ui.configuration.vars.start").attach(self.binder)
    self.closeEvent = ButtonEvent:create(self._vars.ButtonViews.btnBack)
end

function M:dispose()
    self.closeEvent:dispose()
end

return M
