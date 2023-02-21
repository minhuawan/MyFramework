local M = class("counter")

function M:ctor(begin, step)
    begin = begin or 1
    step = step or 1
    assert(type(begin) == 'number', 'invalid parameter: begin')
    assert(type(step) == 'number', 'invalid parameter: step')
    self._begin = begin
    self._step = step
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