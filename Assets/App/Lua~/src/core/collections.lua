---@class list
local list = {}
---@class map
local map = {}
---@class stack
local stack = {}

collections = {
    ---@return list
    list = function()
        local object = setmetatable({}, { __index = list })
        object:clear()
        return object
    end,
    ---@return map
    map = function()
        local object = setmetatable({}, { __index = map })
        object:clear()
        return object
    end,
    ---@return stack
    stack = function()
        local object = setmetatable({}, { __index = stack })
        object:clear()
        return object
    end,
}

---
--- list
---
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
    assert(index > 0 and index < self._count, 'index out of range')
    table.remove(self._inner, index)
    self._count = self._count - 1
    return self
end

function list:get(index)
    assert(type(index) == 'number', 'index should be a number')
    return self._inner[index]
end

function list:set(index, value)
    assert(type(index) == 'number', 'index should be a number')
    assert(index >= 1 and self._count and index <= self._count, 'index out of range')
    self._inner[index] = value
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
    local cursor = b or 1
    local final = e or self._count
    local step = s or 1
    -- print("cursor", cursor, "final", final, "step", step)
    return function()
        -- print("cursor", cursor)
        if cursor < 1 or cursor > final then
            return
        end
        local v = self._inner[cursor]
        cursor = cursor + step
        return cursor - step, v
    end
end

---
--- map
---
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

---
--- stack
---

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

---
--- test cases
---
RUN_COLLECTIONS_TEST_CASE = false
if not RUN_COLLECTIONS_TEST_CASE then
    return
end

function assert_test(v, message)
    assert(v, 'collection test case assert: ' .. tostring(message))
end

-- list test case
l1 = list()
assert_test(l1:count() == 0, 'list case 01')

l1:append(1)
l1:append(nil)
assert_test(l1:count() == 2, 'list case 02')

l1:remove(1)
assert_test(l1:count() == 1, 'list case 03')
assert_test(l1:get(1) == nil, 'list case 04')

for k, v in l1:iter() do
    assert_test(k == 1 and v == nil, 'list case 05')
end

l1:clear()
assert_test(l1:count() == 0, 'list case 06')

for i = 1, 10 do
    l1:append(i)
end
assert_test(l1:count() == 10, 'list case 07')
