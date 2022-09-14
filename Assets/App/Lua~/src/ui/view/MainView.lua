local ButtonEvent = require("core.ui.ButtonEvent")
local View = require("core.ui.mvp.View")
---@class MainView: View
local M = class("MainView", View)

---@param model MainModel
function M:initialize(model)
    self._vars = require("ui.configuration.vars.MainViewVars").attach(self.binder)

    self.editEvent = ButtonEvent(self._vars.ButtonViews.btnEdit)
    self.startEvent = ButtonEvent(self._vars.ButtonViews.btnStart)
    self.wikiEvent = ButtonEvent(self._vars.ButtonViews.btnWiki)
    self.analysisEvent = ButtonEvent(self._vars.ButtonViews.btnAnalysis)
    self.settingEvent = ButtonEvent(self._vars.ButtonViews.btnSetting)
    self.patchEvent = ButtonEvent(self._vars.ButtonViews.btnPatch)
    self.exitEvent = ButtonEvent(self._vars.ButtonViews.btnExit)

    self._vars.Texts.nickname.text = model:getName()
end

function M:dispose()
    self.editEvent:dispose()
    self.startEvent:dispose()
    self.wikiEvent:dispose()
    self.analysisEvent:dispose()
    self.settingEvent:dispose()
    self.patchEvent:dispose()
    self.exitEvent:dispose()
end

return M
