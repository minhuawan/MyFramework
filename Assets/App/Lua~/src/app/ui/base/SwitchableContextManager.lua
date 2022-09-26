local MvpContext = require("app.ui.base.MvpContext")
local MvpContextState = require("app.ui.base.MvpContextState")
---@class SwitchableContextManager
local M = class("SwitchableContextManager")

function M:ctor()
    ---@private
    ---@type stack
    self._histories = collections.stack()
    ---@private
    ---@type MvpContext
    self._current = nil
    ---@private
    ---@type MvpContext
    self._processing = nil
    ---@private
    ---@type MvpContext
    self._backTriggerContext = nil
end

function M:switchTo(configuration)
    if self._processing then
        log.warn("[SwitchableContextManager] can not switch context, because have a processing context. path: {}", self._processing:getPrefabPath())
        return
    end
    if self._current and not self._current:canSwitch() then
        log.warn("[SwitchableContextManager] current context can not switch context. path: {}", self._processing:getPrefabPath())
        return
    end
    local context = MvpContext.new(configuration)
    self._processingListener = bind(self.onProcessingStateChanged, self)
    context:addStateChangeListener(self._processingListener)

    self._processing = context
    if not self._current then
        self._current = context
        -- log.debug("[SwitchableContextManager] now current path: {}", context:getPrefabPath())
    end
    context:moveNextState()
    -- log.debug("[SwitchableContextManager] switch to {}", configuration.prefab)
end

---@private
---@param context MvpContext
function M:onProcessingStateChanged(context, state)
    if state == MvpContextState.Appear then
        if self._processing and self._processing == context and self._processing:canSwitch() then
            if self._current == self._processing then
                -- first
                self._processing = nil
            else
                -- after first
                self._current:moveNextState() -- should move to dispose
                self._current = self._processing
                self._processing = nil
            end
        end
    elseif state == MvpContextState.Dispose then
        --log.debug('onProcessingStateChanged withoutHistory {}, path: {}', withoutHistory, context:getPrefabPath())
        if self._backTriggerContext ~= nil and self._backTriggerContext == context then
            self._backTriggerContext = nil
        else
            self._histories:push(context.configuration)
        end
    end
end

function M:canHandleBack()
    return self._histories and self._histories:count() > 0
end

function M:back()
    log.debug('switchable back')
    if self._processing then
        log.warn('[SwitchableContextManager] back canceled, because have a processing target')
        return
    elseif self._histories:count() == 0 then
        log.warn('[SwitchableContextManager] back canceled, because history stack are empty')
        return
    end
    local last = self._histories:pop()
    self._backTriggerContext = self._current
    self:switchTo(last, true)
end

function M:dispose()
    if self._current then
        self._current:dispose()
    end
    if self._processing then
        self._processing:dispose()
        self._processingListener = nil
    end
    if self._histories then
        self._histories:clear()
    end
    self._processing = nil
    self._current = nil
    self._histories = nil
    self._backTriggerContext = nil
end

return M
