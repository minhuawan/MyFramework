--
--task:bind():delay():interval():times():start()
--
--task:bind():delay():start()
--


---@class TimeTask
local M = class("TimeTask")

function M:ctor(now)
    self._createAt = now
    self._targetTime = self._createAt
    self._maxTimes = 1
    self._curTimes = 0
    self._interval = 0
    self._invokeTime = 1
end

function M:bind(callable)
    self._callable = callable
    return self
end

function M:delay(seconds)
    assert(type(seconds) == 'number' and seconds >= 0, 'delay seconds should great or equals zero.')
    self._targetTime = self._createAt + seconds

    return self
end

function M:interval(seconds)
    assert(type(seconds) == 'number' and seconds > 0, 'interval seconds should great than zero.')
    self._interval = seconds
    return self
end

function M:times(times)
    assert(type(times) == 'number' and times > 0, 'times should great than zero.')
    self._maxTimes = times
    self._curTimes = 0
    return self
end

function M:start()
    self._started = true
    return self
end

function M:tick(realtimeSinceStartup, deltaTime)
    if not self._started then
        return true
    end

    -- 1. check target time
    if self._targetTime then
        if realtimeSinceStartup < self._targetTime then
            return true
        end
    end
    -- 2. check times
    if self._maxTimes then
        if self._curTimes >= self._maxTimes then
            return false
        end
    end
    -- 3. check interval
    if self._interval > 0 then
        if self._invokeTime + self._interval > realtimeSinceStartup then
            return true
        end
    end

    self._invokeTime = realtimeSinceStartup
    self._curTimes = self._curTimes + 1
    local ok, msg = pcall(self._callable)
    if not ok then
        log.error('TimeTask perform tick with error msg: {}', msg)
    end
    return ok
end

function M:release()
    self._callable = nil
end

return M
