local MvpContext = require("core.ui.MvpContext")
local MvpContextState = require("core.ui.MvpContextState")
---@class SwitchableContextManager
local M = singleton("SwitchableContextManager")

function M:ctor()
    ---@private
    ---@type table[]
    self._history = {}
    ---@private
    ---@type MvpContext
    self._current = nil
    ---@private
    ---@type MvpContext
    self._processing = nil
    --self._topCanvasOrder = 0
end

function M:switchTo(configuration)
    if self._processing then
        log.warn("[SwitchableContextManager] can not switch context, because have a processing context. name: {}", self._processing:getName())
        return
    end
    if self._current and not self._current:canSwitch() then
        log.warn("[SwitchableContextManager] current context can not switch context. name: {}", self._processing:getName())
        return
    end
    local context = MvpContext.new(configuration)
    --self._topCanvasOrder = self._topCanvasOrder + 1
    --context:setViewCanvasOrder(self._topCanvasOrder)
    self._processingListener = bind(self.onProcessingStateChanged, self)
    context:addStateChangeListener(self._processingListener)
    table.insert(self._history, configuration)
    self._processing = context
    if not self._current then
        self._current = context
        log.debug("[SwitchableContextManager] now current is {}", context:getName())
    end
    context:moveNextState("switchable")
    log.debug("[SwitchableContextManager] switch to {}", configuration.prefab)
end

---@private
---@param context MvpContext
function M:onProcessingStateChanged(context, state)
    --log.verbose("onProcessingStateChanged context {} state {} @{}", context:getName(), state, tostring(self))
    --log.verbose("onProcessingStateChanged current {} ", self._current:getName())
    --log.verbose("onProcessingStateChanged processing {} ", self._processing:getName())
    if not context or context ~= self._processing then
        return
    end
    if not self._processing:canSwitch() then
        return
    end
    -- remove event listener
    self._processing:removeStateChangeListener(self._processingListener)
    self._processingListener = nil
    -- move stage next
    if self._current == self._processing then
        self._processing = nil
    else
        --self._current:moveNextState() -- to dispose?
        --self._current = self._processing
        --self._current = nil
    end
end

function M:dispose()
    if self._current then
        self._current:dispose()
    end
    if self._processing then
        self._processing:dispose()
    end
    self._processing = nil
    self._current = nil
    self._history = nil
end

return M
