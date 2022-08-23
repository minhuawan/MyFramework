-- function handler
function bind(func, obj)
    assert(obj, "handler obj is nil")
    assert(type(func == "function"), "method not a function")
    local closure = function(...)
        return func(obj, ...)
    end
    return closure
end

function global_set(k, v)
    rawset(_G, k, v)
end

function global_get(k)
    return _G[k]
end

-- disable global set implicit
setmetatable(_G, { __newindex = function(g, k, v)
    log.error("set global ({}) error, cannot set value implicit or with _G, use global_set", k)
end })
