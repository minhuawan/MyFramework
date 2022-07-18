local Presenter = require("core.ui.mvp.Presenter")
---@class MainPresenter : Presenter
local M = class("MainPresenter", Presenter)

function M:didAppeared()
    self._view:setTitle('hello world')
end


return M