local MvpContext = require("core.ui.MvpContext")
---@class SingleContextManager : Singleton
local M = singleton("SingleContextManager")

function M:ctor()
    ---@type MvpContext
    self._current = nil
end

function M:singleShow(configuration)
    if self._current then
        log.warn("show warning, have a dialog show type {}", self._current.presenter.class.__cname)
        return
    end
    if not configuration then
        log.error("show error, configuration is nil value")
        return
    end
    local context = MvpContext.new(configuration)
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

function M:dispose()
    if self._current then
        self._current:dispose()
        self._current = nil
    end
end

return M