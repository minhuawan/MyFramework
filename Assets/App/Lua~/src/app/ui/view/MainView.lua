local ButtonViewWrap = require("app.ui.base.ButtonViewWrap")
local View = require("app.ui.base.mvp.View")
---@class MainView: View
local M = class("MainView", View)

---@param model MainModel
function M:initialize(model)
    self._vars = require("app.ui.configuration.vars.MainViewVars").attach(self.binder)

    self.btnEdit = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnEdit))
    self.btnStart = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnStart))
    self.btnWiki = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnWiki))
    self.btnAnalysis = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnAnalysis))
    self.btnSetting = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnSetting))
    self.btnPatch = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnPatch))
    self.btnExit = self.disposable:append(ButtonViewWrap(self._vars.ButtonViews.btnExit))

    self._vars.Texts.nickname.text = model:getName()
end

return M
