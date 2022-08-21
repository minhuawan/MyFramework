local ButtonEvent = require("core.ui.ButtonEvent")
local View = require("core.ui.mvp.View")
---@class MainView: View
local M = class("MainView", View)

---@param model MainModel
function M:initialize(model)
    self._vars = require("ui.configuration.vars.main").attach(self.binder)

    self.editEvent = ButtonEvent:create(self._vars.ButtonViews.btnEdit)
    self.startEvent = ButtonEvent:create(self._vars.ButtonViews.btnStart)
    self.wikiEvent = ButtonEvent:create(self._vars.ButtonViews.btnWiki)
    self.analysisEvent = ButtonEvent:create(self._vars.ButtonViews.btnAnalysis)
    self.settingEvent = ButtonEvent:create(self._vars.ButtonViews.btnSetting)
    self.patchEvent = ButtonEvent:create(self._vars.ButtonViews.btnPatch)
    self.exitEvent = ButtonEvent:create(self._vars.ButtonViews.btnExit)

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