-- function handler
function handler(obj, method)
    assert(obj, "handler obj is nil")
    assert(type(method == "function"), "method not a function")
    return function(...)
        return method(obj, ...)
    end
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

