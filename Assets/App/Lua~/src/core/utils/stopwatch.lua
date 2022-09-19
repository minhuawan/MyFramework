local function get_time()
    if CS then
        return CS.UnityEngine.Time.realtimeSinceStartupAsDouble
    end
    if calendar then
        return calendar.timestamp()
    end
    return os.time()
end

---@class stopwatch
local M = class("stopwatch")

function M:ctor()
end

function M:reset()
    self._begin = nil
    self._end = nil
end

function M:restart()
    self:reset()
    self._begin = get_time()
end

function M:elapsed()
    assert(self._begin, 'you should call `begin` before elapsed')
    local diff = get_time() - self._begin
    return diff
end

function M:dispose()
    self:reset()
end

return M