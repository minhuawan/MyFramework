--
--task:bind():delay():interval():times():start()
--
--task:bind():delay():start()
--



---@class TimeTask
local M = class("TimeTask")

function M:ctor()
    self._createAt = os.time()
    self._targetTime = os.time()
    self._maxTimes = 1
    self._curTimes = 0
    self._interval = 0
    self._invokeTime = 0
end

function M:bind(callable)
    self._callable = callable
    return self
end

function M:delay(seconds)
    assert(type(seconds) == 'number' and seconds > 0, 'delay second should great than zero.')
    self._targetTime = self._createAt + seconds
    return self
end

function M:interval(second)
    assert(type(second) == 'number' and second > 0, 'interval second should great than zero.')
    self._interval = second
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

function M:tick()
    if not self._started then
        return
    end
    -- 1. check target time
    if self._targetTime then
        if os.time() < self._targetTime then
            return
        end
    end
    -- 2. check times
    if self._maxTimes then
        if self._curTimes >= self._maxTimes then
            return
        end
    end
    -- 3. check interval
    if self._interval > 0 then
        if self._invokeTime + self._interval < os.time() then
            return
        end
    end

    self._invokeTime = os.time()
    self._curTimes = self._curTimes + 1
    self._callable()
end

function M:release()
    self._callable = nil
end

return M
