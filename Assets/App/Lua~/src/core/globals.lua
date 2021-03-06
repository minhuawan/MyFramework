-- function handler
function bind(func, obj)
    assert(obj, "handler obj is nil")
    assert(type(func == "function"), "method not a function")
    return function(...)
        return func(obj, ...)
    end
end

OnLuaEnvDisposeBefore = function()
    --require("xlua.util").print_func_ref_by_csharp()
    require("core.ui.UIManager").getInstance():dispose()
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

