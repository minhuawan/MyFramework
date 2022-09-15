---@class Presenter
local M = class("Presenter")

---@param mvpContext MvpContext
function M:initialize(mvpContext)
    self._context = mvpContext
    mvpContext:createViewAsync(function(view)
        ---@type View
        self._view = view
        -- Control the mvpContext flow manually
        local continueManually = self._view:initialize(mvpContext.model)
        if not continueManually then
            mvpContext:moveNextState('create-view-async')
        end
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
