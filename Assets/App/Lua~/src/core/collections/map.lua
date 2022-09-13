---@class map
local map = setmetatable({}, { __call = function(t)
    local object = setmetatable({}, {
        __index = t,
        __tostring = function(self)
            return self:tostring()
        end
    })
    object:clear()
    return object
end })

function map:clear()
    self._inner = {}
    self._count = 0
    return self
end

function map:set(key, value)
    assert(key ~= nil, 'key should not be a nil value')
    assert(value ~= nil, 'value should not be a nil value, if you want to remove, use remove')
    if not self:has(key) then
        self._count = self._count + 1
    end
    self._inner[key] = value
    return self
end

function map:get(key, default)
    assert(key ~= nil, 'key should not be a nil value')
    if self:has(key) then
        return self._inner[key]
    end
    return default
end

function map:has(key)
    assert(key ~= nil, 'key should not be a nil value')
    return self._inner[key] ~= nil
end

function map:remove(key)
    assert(key ~= nil, 'key should not be a nil value')
    if self:has(key) then
        self._inner[key] = nil
        self._count = self._count - 1
    end
    return self
end

function map:count()
    return self._count
end

function map:iter()
    local k = nil
    return function()
        k = next(self._inner, k)
        if k == nil then
            return
        end
        local v = self._inner[k]
        return k, v
    end
end

function map:raw()
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
function map:tostring()
    if not self._inner or self._count == 0 then
        return '{}'
    end
    local t = {}
    for k, v in self:iter() do
        assert(k ~= self and v ~= self, 'map tostring failed, can not contains self')
        -- {"key": value}
        local key = tostring(k)
        local value
        local vt = type(v)
        if vt == 'string' then
            value = '"' .. tostring(v) .. '"'
        else
            value = tostring(v)
        end

        local kvp = '"' .. key .. '":' .. value
        table.insert(t, kvp)
    end
    return '{' .. table.concat(t, ', ') .. '}'
end

return map