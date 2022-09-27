local subject = require("core.reactive.subject")
local observable = require("core.reactive.observable")
local ButtonGroupEvent = require("app.ui.base.ButtonGroupEvent")
local ButtonViewWrap = require("app.ui.base.ButtonViewWrap")
---@class StartView: View
local M = class("StartView", require("app.ui.base.mvp.View"))

function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.StartViewVars").attach(self.binder)
    self.btnBack = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnBack))

    -- todo 这里要想办法包一层...
    local menu1 = ButtonViewWrap(self._vars.ButtonViews.menu1)
    local t1 = {
        menu1
    }
    local t2 = {

    }
    local sj = subject()
    self.modeEvent = observable(sj)
    self.disposable:append(self.modeEvent)
    self.disposable:append(sj)
    menu1.clickEvent:subscribe(function()

    end)


    local bts = { self._vars.ButtonViews.menu1 }
    local peer = nil
    self.modeEvent = self.disposable:append(ButtonGroupEvent(bts, peer))
end
return M
