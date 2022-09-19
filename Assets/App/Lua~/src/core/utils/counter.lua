local M = class("counter")

function M:ctor(begin, step)
    assert(type(begin) == 'number', 'invalid parameter: begin')
    assert(type(step) == 'number', 'invalid parameter: step')
    self._begin = begin or 1
    self._step = step or 1
    self._value = self._begin
end

function M:plus(v)
    self._value = self._value + (self._step or v)
    return self._value
end

function M:value()
    return self._value
end

return M