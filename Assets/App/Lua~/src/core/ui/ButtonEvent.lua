---@class ButtonEvent
local M = class("ButtonEvent")

function M:ctor(buttonView)
    ---@type map
    self._map = collections.map()
    self._bv = buttonView
end

function M:subscribe(fn)
    log.assert(type(fn) == 'function', '[ButtonEvent] subscribe `fn` not a function', self._name)
    local k = tostring(fn)
    if self._map:has(fn) then
        log.warn('[ButtonEvent] subscribe duplicated, traceback: {}', log.traceback())
        return
    end
    self._map:set(k, fn)
    self._bv.onClick:AddListener(fn)
end

function M:unsubscribe(fn)
    log.assert(type(fn) == 'function')
    local k = tostring(fn)
    if self._map:has(k) then
        self._map:remove(k)
        self._bv.onClick:RemoveListener(fn)
    end
end

function M:dispose()
    -- https://blog.csdn.net/weixin_42205596/article/details/90608478
    self._bv.onClick:RemoveAllListeners()
    self._bv.onClick:Invoke()
    self._map:clear()
    self._map = nil
    self._bv = nil
end

---@param list list
function M:addTo(list)
    list:append(self)
    return self
end

return M
