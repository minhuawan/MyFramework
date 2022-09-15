local ButtonEvent = require("core.ui.ButtonEvent")
local View = require("core.ui.mvp.View")
---@class MainView: View
local M = class("MainView", View)

---@param model MainModel
function M:initialize(model)
    self._vars = require("ui.configuration.vars.MainViewVars").attach(self.binder)

    self.editEvent = ButtonEvent(self._vars.ButtonViews.btnEdit):addTo(self.disposable)
    self.startEvent = ButtonEvent(self._vars.ButtonViews.btnStart):addTo(self.disposable)
    self.wikiEvent = ButtonEvent(self._vars.ButtonViews.btnWiki):addTo(self.disposable)
    self.analysisEvent = ButtonEvent(self._vars.ButtonViews.btnAnalysis):addTo(self.disposable)
    self.settingEvent = ButtonEvent(self._vars.ButtonViews.btnSetting):addTo(self.disposable)
    self.patchEvent = ButtonEvent(self._vars.ButtonViews.btnPatch):addTo(self.disposable)
    self.exitEvent = ButtonEvent(self._vars.ButtonViews.btnExit):addTo(self.disposable)

    self._vars.Texts.nickname.text = model:getName()
end

return M
