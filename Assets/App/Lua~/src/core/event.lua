local event = {}

---@type map
local subjects = collections.map()

function event.subscribe(name, callable)
    assert(type(name) == 'string' and name ~= "", 'invalid parameter: name')
    assert(type(callable) == 'function', 'invalid parameter: callable')
    if not subjects:has(name) then
        subjects:set(name, collections.map())
    end
    local tokenMap = subjects:get(name)
    local token = {
        name = name,
        callable = callable
    }
    tokenMap:set(tostring(token), token)
    return token
end

function event.unsubscribe(token)
    assert(token and token.name, 'invalid token')
    local name = token.name
    if not subjects:has(name) then
        log.warn('event token not found in cache, name {}', name)
        return
    end
    local tokenMap = subjects:get(name)
    local key = tostring(token)
    if not tokenMap:has(key) then
        log.warn('event token not found in cache, name {}', name)
        return
    end
    tokenMap:remove(key)
end

function event.dispatch(name, ...)
    if subjects:has(name) then
        local map = subjects:get(name)
        for _, token in map:iter() do
            local ok, msg = pcall(token.callable, ...)
            if not ok then
                log.error('dispatch event error, name: {}, message: {}', name, msg)
            end
        end
    end
end

return event