---@class StartPresenter : Presenter
---@field _view StartView
local M = class("StartPresenter", require("app.ui.base.mvp.Presenter"))

function M:didAppeared()
    self._view.backEvent:subscribe(self.onBackKey, self)
    self._view.modeEvent:subscribe(self.onModeSelected, self)
end

function M:onModeSelected(model)
    if model == 'normal'  then
        self._context:moveNextState()
        local configuration = require("app.ui.configuration.context.CharacterSelectContext")
        App.ui.UIManager:navigateTo(configuration)
    else
        log.assert(false, 'unsupported model type `{}`', model)
    end
end

return M