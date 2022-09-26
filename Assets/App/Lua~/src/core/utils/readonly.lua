return function(t)
    assert(type(t) == 'table', 'invalid type')
    return setmetatable({}, {
        __index = t,
        __newindex = function()
            error('error, the table is readonly')
        end,
    })
end