---@class Presenter
local M = class("Presenter")

---@param mvpContext MvpContext
function M:initialize(mvpContext)
    self._context = mvpContext
    mvpContext:createViewAsync(function(view)
        ---@type View
        self._view = view
        self._view:initialize(mvpContext.model)
        mvpContext:moveNextState("createViewCallback")
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
    self._context:moveNextState("didDisappeared")
end

function M:dispose()
end

function M:onBackKey()
end

return M