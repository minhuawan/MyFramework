storage = {}

local DEBUG = false

local Storage = CS.MyFramework.Runtime.Utils.Storage
local function assert_params(scope, key)
    assert(type(scope) == 'string' and scope ~= '', 'scope should be a non-empty string value')
    assert(string.find(scope, '%W') == nil, 'invalid val: ' .. tostring(scope))

    assert(type(key) == 'string' and key ~= '', 'key should be a non-empty string value')
    assert(string.find(key, '%W') == nil, 'invalid val: ' .. tostring(key))
end

local function wrap_value(value)
    local vt = type(value)
    assert(vt == 'string'
            or vt == 'number'
            or vt == 'bool', 'only supported with string or number or bool value')
    return string.sub(vt, 1, 3) .. ':' .. tostring(value)
end

local function unwrap_value(value)
    assert(type(value) == 'string', 'unwrap value failed, target is not string')
    local vt = string.sub(value, 1, 3)
    local v = string.sub(value, 5, #value)
    if vt == 'str' then
        return v
    elseif vt == 'num' then
        return tonumber(v)
    elseif vt == 'boo' then
        return v == 'true'
    else
        assert_f(false, 'unwrap value failed, invalid value type: {}', vt)
    end
end

local function path(scope, key)
    assert_params(scope, key)
    return formatter.string("{}-{}", scope, key)
end

function storage.write(scope, key, value)
    local p = path(scope, key)
    local wv = wrap_value(value)
    Storage.Write(p, wv)
    if DEBUG then
        log.debug('[storage] write {}, with scope: "{}" and key: "{}"', value, scope, key)
    end
end

function storage.read(scope, key)
    local p = path(scope, key)
    if storage.has(scope, key) then
        local wv = Storage.Read(p)
        local v = unwrap_value(wv)
        if DEBUG then
            log.debug('[storage] read with scope: "{}" and key: "{}", got: {}', scope, key, v)
        end
        return v
    end
    if DEBUG then
        log.debug('[storage] read with scope: "{}" and key: "{}", not has', scope, key)
    end
    return nil
end

function storage.has(scope, key)
    local p = path(scope, key)
    return Storage.Has(p)
end

function storage.delete(scope, key)
    local p = path(scope, key)
    if DEBUG then
        log.debug('[storage], delete with scope: "{}" and key: "{}"', scope, key)
    end
    return Storage.Delete(p)
end


