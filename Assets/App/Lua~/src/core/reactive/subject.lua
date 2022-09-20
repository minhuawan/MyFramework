local subscription = require("core.reactive.subscription")
---@class subject
local M = class("subject")

function M:ctor()
    self._disposed = nil
    self._packs = {}
end

function M:onNext(...)
    assert(not self._disposed, 'subject disposed')
    for location, pack in pairs(self._packs) do
        if pack == false then
            -- removed
        else
            assert(type(pack) == 'table' and pack.fn, 'invalid pack')
            if pack.parameters then
                pack.fn(table.unpack(pack.parameters), ...)
            else
                pack.fn(...)
            end
        end
    end
end

function M:subscribe(fn, ...)
    assert(not self._disposed, 'subject disposed')
    local pack = {
        fn = fn,
    }
    if select("#", ...) > 0 then
        pack.parameters = table.pack(...)
    end
    local location = tostring(pack)
    self._packs[location] = pack
    return subscription(self, location)
end

function M:unsubscribe(location)
    assert(not self._disposed, 'subject disposed')
    self._packs[location] = false
end

function M:dispose()
    if self._disposed then
        return
    end
    self._disposed = true
    if self._packs then
        self._packs = nil
    end
end

return M