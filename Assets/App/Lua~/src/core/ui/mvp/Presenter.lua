---@class Presenter
local M = class("Presenter")

---@param mvpContext MvpContext
function M:initialize(mvpContext)
    self._context = mvpContext
    mvpContext:createViewAsync(function(view)
        ---@type View
        self._view = view
        self._view:initialize(mvpContext.model)
        mvpContext:moveNextState('create-view-async')
    end)
end

function M:appear()
    self._view:appearAsync(bind(self.didAppeared, self))
end

function M:didAppeared()
end

function M:disappear()
    self._view:disappearAsync(bind(self.didDisappeared, self))
end

function M:didDisappeared()
    self._context:moveNextState()
end

function M:dispose()
end

function M:onBackKey()
    require("core.ui.UIManager"):onBackKey()
end

return M
