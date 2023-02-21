return function()
    local root = "app.metadata"
    return {
        load = function(module, ...)
            local t = { root, module, ... }
            local real_path = table.concat(t, '.')
            return require(real_path)
        end
    }
end