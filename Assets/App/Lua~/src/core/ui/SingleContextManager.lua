---@class SingleContextManager
local M = singleton("SingleContextManager")

function M:ctor()
    ---@type MvpContext
    self._current = nil
end

function M:show(context)
    if self._current then
        return
    end
end

function M:back()
    if self._current then
    end
end

return M