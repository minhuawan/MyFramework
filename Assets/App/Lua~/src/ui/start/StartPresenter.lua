---@class StartPresenter : Presenter
---@field _view StartView
local M = class("StartPresenter", require("core.ui.mvp.Presenter"))


function M:didAppeared()
    self._view.closeEvent:subscribe(bind(self.onBack, self))
end

function M:onBack()
    self._context:moveNextState()
    require("core.ui.UIManager"):back()
end

return M