---@class StartPresenter : Presenter
---@field _view StartView
local M = class("StartPresenter", require("app.ui.base.mvp.Presenter"))


function M:didAppeared()
    self.disposable:append(self._view.btnBack.clickEvent:subscribe(bind(self.onBackKey, self)))
    self.disposable:append(self._view.modeEvent:subscribe(bind(self.onModeSelected, self)))
end

function M:onModeSelected(model)
    self._context:moveNextState()

    local configuration = require("app.ui.configuration.context.CharacterSelectContext")
    App.ui.UIManager:navigateTo(configuration)
end


return M