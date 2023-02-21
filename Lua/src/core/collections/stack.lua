---@class stack
local stack = setmetatable({}, { __call = function(t)
    local object = setmetatable({}, {
        __index = t,
        __tostring = function(self)
            return self:tostring()
        end
    })
    object:clear()
    return object
end })

function stack:clear()
    self._inner = {}
    self._count = 0
    return self
end

function stack:push(v)
    self._count = self._count + 1
    self._inner[self._count] = v
    return self
end

function stack:pop()
    if self._count == 0 then
        return
    end
    local c = self._count
    self._count = self._count - 1
    return table.remove(self._inner, c)
end

function stack:peek()
    return self._inner[self._count]
end

function stack:has(value)
    for _, v in self:iter() do
        if v == value then
            return true
        end
    end
    return false
end

function stack:count()
    return self._count
end

function stack:iter()
    local top = self._count
    return function()
        if top == 0 then
            return
        end
        top = top - 1
        return top + 1, self._inner[top + 1]
    end
end

function stack:reverse()
    local c = stack()
    for i = self._count, 1, -1 do
        c:push(self._inner[i])
    end
    return c
end

function stack:raw()
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
function stack:tostring()
    if json then
        return json.encode(self._inner)
    end
    if not self._inner or self._count == 0 then
        return '[]'
    end
    local t = {}
    for i = self._count, 1, -1 do
        local v = self._inner[i]
        assert(v ~= self, 'stack tostring failed, can not contains self')
        table.insert(t, tostring(v))
    end
    return '[' .. table.concat(t, ', ') .. ']'
end

function stack:tojson()
    return self:tostring()
end

return stack
