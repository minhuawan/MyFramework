---@class ButtonEvent
local M = class("ButtonEvent")

function M:ctor(buttonView)
    self._bv = buttonView
end

function M:subscribe(fn)
    log.assert(type(fn) == 'function')
    if not self._clicks then
        self._clicks = {}
    end
    table.insert(self._clicks, fn)
    self._bv.onClick:AddListener(fn)
end

function M:unsubscribe(fn)
    log.assert(type(fn) == 'function')
    if not self._clicks then
        log.warn('no function subscribed in this object')
        return
    end
    local index
    for i = 1, self._clicks do
        if self._clicks[i] == fn then
            index = i
            break
        end
    end
    if not index then
        log.warn('unsubscribe target not found in cache')
        return
    end
    table.remove(self._clicks, index)
    self._bv.onClick:RemoveListener(fn)
end

function M:dispose()
    if self._clicks then
        -- https://blog.csdn.net/weixin_42205596/article/details/90608478
        self._bv.onClick:RemoveAllListeners()
        self._bv.onClick:Invoke()
        self._clicks = nil
        self._bv = nil
    end
end

return M