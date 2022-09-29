local subject = require("core.reactive.subject")
local observable = require("core.reactive.observable")
local ButtonGroupEvent = require("app.ui.base.ButtonGroupEvent")
local ButtonViewWrap = require("app.ui.base.ButtonViewWrap")
---@class StartView: View
local M = class("StartView", require("app.ui.base.mvp.View"))

function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.StartViewVars").attach(self.binder)
    self.btnBack = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnBack))

    self.event = ButtonViewWrap(self._vars.ButtonViews.menu1).clickEvent:selectTo(1)
            :merge(ButtonViewWrap(self._vars.ButtonViews.menu2).clickEvent:selectTo(2))
            :merge(ButtonViewWrap(self._vars.ButtonViews.menu2).clickEvent:selectTo(2))

    self.disposable:append(self.event)
end
return M
