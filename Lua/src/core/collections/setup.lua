local list = require("core.collections.list")
local map = require("core.collections.map")
local stack = require("core.collections.stack")

collections = {
    list = list,
    map = map,
    stack = stack,
    range = function(p1, p2, p3)
        local begin, end_, step
        if p1 and p2 and p3 then
            begin, end_, step = p1, p2, p3
        elseif p1 and p2 then
            begin, end_, step = p1, p2, p2 > p1 and 1 or -1
        elseif p1 then
            begin, end_, step = 1, p1, p1 > 1 and 1 or -1
        end
        local l = list()
        for i = begin, end_, step do
            l:append(i)
        end
        return l
    end
}