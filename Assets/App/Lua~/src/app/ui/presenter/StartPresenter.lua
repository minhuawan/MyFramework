---@class StartPresenter : Presenter
---@field _view StartView
local M = class("StartPresenter", require("app.ui.base.mvp.Presenter"))


function M:didAppeared()
    self._view.closeEvent:subscribe(bind(self.onBackKey, self))
    self._view.modeEvent:subscribe(bind(self.onModeSelected, self))
end

function M:onModeSelected(model)
    self._context:moveNextState()

    local configuration = require("app.ui.configuration.context.CharacterSelectContext")
    require("app.ui.base.UIManager"):navigateTo(configuration)
end


return M