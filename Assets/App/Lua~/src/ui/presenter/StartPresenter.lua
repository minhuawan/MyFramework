---@class StartPresenter : Presenter
---@field _view StartView
local M = class("StartPresenter", require("core.ui.mvp.Presenter"))


function M:didAppeared()
    self._view.closeEvent:subscribe(bind(self.onBackKey, self))
    self._view.modeEvent:subscribe(bind(self.onModeSelected, self))
end

function M:onModeSelected(model)
    self._context:moveNextState()

    local configuration = require("ui.configuration.context.CharacterSelectContext")
    require("core.ui.UIManager"):navigateTo(configuration)
end


return M