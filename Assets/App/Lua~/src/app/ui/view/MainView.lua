local View = require("app.ui.base.mvp.View")
---@class MainView: View
local M = class("MainView", View)

---@param model MainModel
function M:initialize(model)
    self._vars = require("app.ui.generated.MainViewVars").attach(self.binder)
    self.disposable:append(self._vars)

    self.editEvent = self._vars.ButtonViews.btnEdit.clickEvent
    self.startEvent = self._vars.ButtonViews.btnStart.clickEvent
    self.wikiEvent = self._vars.ButtonViews.btnWiki.clickEvent
    self.AnalysisEvent = self._vars.ButtonViews.btnAnalysis.clickEvent
    self.settingEvent = self._vars.ButtonViews.btnSetting.clickEvent
    self.patchEvent = self._vars.ButtonViews.btnPatch.clickEvent
    self.exitEvent = self._vars.ButtonViews.btnExit.clickEvent

    self._vars.Texts.nickname.text = model:getName()
end

return M
