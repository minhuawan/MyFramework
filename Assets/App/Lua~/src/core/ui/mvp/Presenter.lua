---@class Presenter
local M = class("Presenter")

---@param mvpContext MvpContext
function M:initialize(mvpContext)
    self._context = mvpContext
    mvpContext:createViewAsync(function(view)
        ---@type View
        self._view = view
        self._view:initialize(mvpContext.model)
        mvpContext:moveNextState()
    end)
end

function M:appear()
    self._view:appearAsync(handler(self, self.didAppeared))
end

function M:didAppeared()
end

function M:disappear()
    self._view:disappearAsync(handler(self, self.didDisappeared))
end

function M:didDisappeared()
    self._context:moveNextState()
end

function M:dispose()
end

function M:onBackKey()
end

return M