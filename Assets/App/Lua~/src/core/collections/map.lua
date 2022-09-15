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
    self._iterating = false
    return self
end

function map:set(key, value)
    assert(not self._iterating, 'do dot call `map.set` in iterating')
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
    assert(not self._iterating, 'do dot call `map.remove` in iterating')
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
    assert(not self._iterating, 'map already in iterating')
    self._iterating = true
    local k = nil
    return function()
        if not self._iterating then
            return
        end
        k = next(self._inner, k)
        if k == nil then
            self._iterating = false
            return
        end
        local v = self._inner[k]
        return k, v
    end
end

function map:remove_if(fn)
    assert(type(fn) == 'function', 'fn not a function')

    local to_remove = {}
    for k, v in self:iter() do
        local ok, r = pcall(fn, k, v)
        if not ok then
            log.error('call remove_if with error msg: {}', r)
            return
        end
        if r then
            table.insert(to_remove, k)
        end
    end
    for _, k in ipairs(to_remove) do
        self:remove(k)
    end
end

function map:kvs()
    local keys = collections.list()
    local values = collections.list()
    for k, v in self:iter() do
        keys:append(k)
        values:append(v)
    end
    return keys, values
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
    for k, v in pairs(self._inner) do
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