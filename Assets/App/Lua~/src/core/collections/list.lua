---@class list
local list = setmetatable({}, { __call = function(t)
    local object = setmetatable({}, {
        __index = t,
        __tostring = function(self)
            return self:tostring()
        end,
        __add = function(t1, t2)
            return t1:concat(t2)
        end
    })
    object:clear()
    return object
end })

function list:clear()
    self._inner = {}
    self._count = 0
    return self
end

function list:append(value)
    local idx = self._count
    idx = idx + 1
    self._inner[idx] = value
    self._count = idx
    return self
end

function list:remove(index)
    assert(type(index) == 'number', 'index should be a number')
    assert(index >= 1 and index <= self._count, 'index out of range')
    table.remove(self._inner, index)
    self._count = self._count - 1
    return self
end

function list:find(value)
    for i, v in self:iter() do
        if v == value then
            return i
        end
    end
    return nil
end

function list:get(index)
    assert(type(index) == 'number', 'index should be a number')
    return self._inner[index]
end

function list:set(index, value)
    assert(type(index) == 'number', 'index should be a number')
    assert(index >= 1 and self._count and index <= self._count, 'set index out of range')
    self._inner[index] = value
    return self
end

function list:insert(index, value)
    assert(type(index) == 'number', 'index should be a number')
    assert(index >= 1 and self._count and index <= self._count, 'insert index out of range')
    table.insert(self._inner, index, value)
    self._count = self._count + 1
    return self
end

function list:count()
    return self._count
end

function list:reverse()
    local c = list()
    for i = self._count, 1, -1 do
        c:append(self._inner[i])
    end
    return c
end

function list:iter(b, e, s)
    if self._count == 0 then
        return function()
            return nil
        end
    end
    local begin = b or 1
    local end_ = e or self._count
    local step = s or 1
    log.assert(type(begin) == 'number'
            and type(end_) == 'number'
            and type(step) == 'number',
            'invalid b: {}, e: {}, s: {}', b, e, s
    )
    if step > 0 then
        log.assert(begin <= end_, 'invalid parameters with step > 0, b: {}, e: {}, s: {}', b, e, s)
    elseif step < 0 then
        log.assert(begin >= end_, 'invalid parameters with step < 0, b: {}, e: {}, s: {}', b, e, s)
    elseif step == 0 then
        log.assert(false, 'invalid parameters with step = 0, b: {}, e: {}, s: {}', b, e, s)
    end
    local cursor = begin
    return function()
        if step > 0 and cursor > end_ then
            return
        elseif step < 0 and cursor < end_ then
            return
        end
        local v = self._inner[cursor]
        cursor = cursor + step
        return cursor - step, v
    end
end

function list:sort(fn)
    table.sort(self._inner, fn)
    return self
end

---@param t1 list
function list:concat(t1)
    local new = self:clone()
    for _, v in t1:iter() do
        new:append(v)
    end
    return new
end

function list:clone()
    local new = list()
    for _, v in self:iter() do
        new:append(v)
    end
    return new
end

function list:raw()
    local t = {}
    for k, v in self:iter() do
        t[k] = v
    end
    return t
end

---
--- 如果 collection 相互包含, 在 tostring 的时候可能会出现栈溢出问题
--- 尽量避免在 相互引用的 collection 中使用 tostring 方法
---
function list:tostring()
    if not self._inner or self._count == 0 then
        return '[]'
    end
    local t = {}
    for _, v in self:iter() do
        assert(v ~= self, 'list tostring failed, can not contains self')
        table.insert(t, tostring(v))
    end
    return '[' .. table.concat(t, ', ') .. ']'
end

return list