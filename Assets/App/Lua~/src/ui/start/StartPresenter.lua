---@class StartPresenter : Presenter
---@field _view StartView
local M = class("StartPresenter", require("core.ui.mvp.Presenter"))


function M:didAppeared()
    self._view.closeEvent:subscribe(bind(self.onCloseEvent, self))
end

function M:onCloseEvent()
    self._context:moveNextState()
end

return M