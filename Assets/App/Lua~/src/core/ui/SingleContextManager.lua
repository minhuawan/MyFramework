local MvpContext = require("core.ui.MvpContext")
---@class SingleContextManager
local M = singleton("SingleContextManager")

function M:ctor()
    ---@type MvpContext
    self._current = nil
end

function M:show(context)
    if self._current then
        log.warn("show warning, have a dialog show type {}", self._current.presenter.class.__cname)
        return
    end
    if not context then
        log.error("show error, context is nil value")
        return
    end
    self._current = context
    self._current:moveNextState()
end

function M:hide(context)
    if not context then
        log.error("hide error, context is nil value")
        return
    end
    if not self._current then
        log.error("hide error, current context is nil, no context to hide")
        return
    end
    if self._current ~= context then
        log.error("hide error, self._current ~= context")
        return
    end
    self._current:dispose()
    self._current = nil
end

function M:abort(message)
    log.error("abort message {}", message)
    if not self._current then
        log.error("abort error current context is nil")
        return
    end
    self._current:dispose()
    self._current = nil
end

function M:back()
    if self._current then
        self._current:handleBackKey()
    end
end

return M