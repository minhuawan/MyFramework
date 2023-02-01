---@class Presenter
local M = class("Presenter")

function M:ctor()
    self.disposable = collections.list()
end

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
    if not self.disposable or self.disposable:count() == 0 then
        return
    end
    for i, v in self.disposable:iter() do
        if v.dispose then
            local ok, msg = pcall(v.dispose, v)
            if not ok then
                log.error('disposed view with error, index: {}, self: {}, msg: {}', i, self, msg)
                return
            end
        else
            log.warn('disposable object does not contain a `dispose` field, index: {}, self: {}', i, self)
        end
    end
    self.disposable:clear()
    self.disposable = nil
end

function M:onBackKey()
    App.ui.UIManager:onBackKey()
end

return M
