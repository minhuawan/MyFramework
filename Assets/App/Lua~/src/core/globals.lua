local p = print
print = function(...)
    log.warn('consider use log instant of print')
    p(...)
end


-- function handler
function bind(func, obj)
    assert(obj, "handler obj is nil")
    assert(type(func) == "function", "method not a function")
    local closure = function(...)
        return func(obj, ...)
    end
    return closure
end

function bind_p1(func, obj, p1)
    assert(obj, "handler obj is nil")
    assert(type(func) == "function", "method not a function")
    local closure = function(...)
        return func(obj, p1, ...)
    end
    return closure
end

function bind_p2(func, obj, p1, p2)
    assert(obj, "handler obj is nil")
    assert(type(func) == "function", "method not a function")
    local closure = function(...)
        return func(obj, p1, p2, ...)
    end
    return closure
end

function bind_p2(func, obj, p1, p2, p3)
    assert(obj, "handler obj is nil")
    assert(type(func) == "function", "method not a function")
    local closure = function(...)
        return func(obj, p1, p2, p3, ...)
    end
    return closure
end

function assert_f(condition, format, ...)
    if condition then
        return
    else
        local msg = formatter.string(format, ...)
        error(msg, 2)
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
