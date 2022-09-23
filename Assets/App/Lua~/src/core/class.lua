function class(name, super)
    local clazz = { __cname = name, super = super }
    clazz.class = clazz
    setmetatable(clazz, {
        __index = super,
        __call = function(t, ...)
            return t.new(...)
        end,
        __tostring = function(t)
            return formatter.string("class@{}", t.__cname)
        end
    })
    clazz.new = function(...)
        local instance = {}
        local addr = tostring(instance)
        setmetatable(instance, { __index = clazz, __tostring = function(t)
            return formatter.string("{}@{}", t.class.__cname, addr)
        end })
        if clazz.ctor then
            clazz.ctor(instance, ...)
        end
        return instance
    end
    return clazz
end

---@generic T
---@class Singleton
---@return fun(T):T
function singleton(name, super)
    local clazz = class(name, super)
    clazz.__instance__ = nil
    clazz.getInstance = function()
        log.debug("call instance")
        if clazz.__instance__ == nil then
            clazz.__instance__ = clazz.new()
        end
        assert(clazz.__instance__, "singleton is nil")
        log.debug("return instance {}, {}", clazz, clazz.__instance__)
        return clazz.__instance__
    end
    return clazz
end
