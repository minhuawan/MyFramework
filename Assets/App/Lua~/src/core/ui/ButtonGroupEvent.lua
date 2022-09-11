local ButtonEvent = require("core.ui.ButtonEvent")
---@class ButtonGroupEvent
local M = class("ButtonGroupEvent")

function M:createGroup(bvs, peer)
    return M.new(bvs, peer)
end

function M:ctor(bvs, peer)
    self._bvs = bvs
    self._peer = peer
    ---@type map
    self._map = collections.map()

    self._buttonEvents = {}
    for i, bv in ipairs(bvs) do
        local event = ButtonEvent:create(bv)
        event:subscribe(function()
            self:onButtonTrigger(i)
        end)
        self._buttonEvents[i] = event
    end
end

---@private
function M:onButtonTrigger(index)
    local ev = self._buttonEvents[index]
    if not ev then
        return
    end
    for _, fn in self._map:iter() do
        local param = index
        if self._peer then
            param = self._peer[index]
        end
        local ok, msg = pcall(fn, param)
        if not ok then
            log.error('[ButtonGroupEvent] perform with error msg: {}', msg)
        end
    end
end

function M:subscribe(fn)
    log.assert(type(fn) == 'function', '[ButtonGroupEvent] subscribe `fn` not a function, event name: {}', self._name)
    local k = tostring(fn)
    if self._map:has(fn) then
        log.warn('[ButtonGroupEvent] subscribe duplicated, event name: {}, traceback: {}', self._name, log.traceback())
        return
    end
    self._map:set(k, fn)
end

function M:unsubscribe(fn)
    local k = tostring(fn)
    if self._map:has(k) then
        self._map:remove(k)
    end
end

function M:dispose()
    self._map:clear()
    for _, be in pairs(self._buttonEvents) do
        be:dispose()
    end
    self._buttonEvents = nil
    self._map = nil
end

return M