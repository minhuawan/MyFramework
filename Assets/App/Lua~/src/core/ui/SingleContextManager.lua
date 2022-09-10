local MvpContextState = require("core.ui.MvpContextState")
local MvpContext = require("core.ui.MvpContext")
---@class SingleContextManager : Singleton
local M = singleton("SingleContextManager")

function M:ctor()
    ---@type MvpContext
    self._current = nil
end

function M:singleShow(configuration)
    if self._current then
        log.warn("[SingleContextManager] show warning, have a dialog show type: {}", self._current.presenter)
        return
    end
    if not configuration then
        log.error("[SingleContextManager] show error, configuration is nil value")
        return
    end
    local context = MvpContext.new(configuration)
    self._current = context
    self._current:addStateChangeListener(bind(self.onStateChanged, self))
    self._current:moveNextState()
end

--function M:hide(context)
--    if not context then
--        log.error("[SingleContextManager] hide error, context is nil value")
--        return
--    end
--    if not self._current then
--        log.error("[SingleContextManager] hide error, current context is nil, no context to hide")
--        return
--    end
--    if self._current ~= context then
--        log.error("[SingleContextManager] hide error, self._current ~= context")
--        return
--    end
--    self._current:dispose()
--    self._current = nil
--end

function M:onStateChanged(context, state)
    if self._current and self._current == context then
        if state == MvpContextState.Dispose then
            self._current = nil
        end
    end
end

function M:abort(message)
    log.warn("[SingleContextManager] abort message {}", message)
    if not self._current then
        log.warn("[SingleContextManager] abort error current context is nil")
        return
    end
    self._current:dispose()
    self._current = nil
end

function M:back()
    self._current:moveNextState()
end

function M:canHandleBack()
    return self._current ~= nil
end

function M:dispose()
    if self._current then
        self._current:dispose()
        self._current = nil
    end
end

return M