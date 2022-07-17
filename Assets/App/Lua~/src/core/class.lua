function class(name, super)
    local clazz = { __cname = name, super = super }
    if super then
        setmetatable(clazz, { __index = super })
    end
    clazz.new = function(...)
        local instance = setmetatable({}, { __index = clazz })
        if clazz.ctor then
            clazz.ctor(instance, ...)
        end
        return instance
    end
    return clazz
end

function singleton(name, super)
    local clazz = class(name, super)
    clazz.__instance__ = nil
    clazz.getInstance = function()
        if clazz.__instance__ == nil then
            clazz.__instance__ = clazz.new()
        end
        return clazz.__instance__
    end
end