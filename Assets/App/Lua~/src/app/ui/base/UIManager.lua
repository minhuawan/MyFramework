local SwitchableContextManager = require("app.ui.base.SwitchableContextManager")
local SingleContextManager = require("app.ui.base.SingleContextManager")
---@class UIManager
local M = class("UIManager")

function M:ctor()
    self:initialize()
end

function M:initialize()
    self._switchable = SwitchableContextManager()
    self._single = SingleContextManager()

    self._escapeKeyListener = CS.MyFramework.Runtime.Utils.EscapeKeyListener.Create('Lua-UIManager', bind(self.onBackKeyFromKeyboard, self))
end

function M:navigateTo(configuration)
    local tye = type(configuration)
    if tye == 'table' then

    end
    assert(type(configuration) == 'table', 'invalid configuration')
    if configuration.type == 'single' then
        self._single:singleShow(configuration)
    elseif configuration.type == 'switchable' then
        self._switchable:switchTo(configuration)
    else
        log.assert(false, 'invalid configuration type: {}', configuration.type)
    end
end

function M:dispose()
    if self._switchable then
        self._switchable:dispose()
    end
    if self._single then
        self._single:dispose()
    end

    if self._escapeKeyListener then
        self._escapeKeyListener:Dispose()
        self._escapeKeyListener = nil
    end
end
CharacterSelectView
function M:onBackKeyFromKeyboard()
    self:onBackKey()
end

function M:onBackKey()
    if self._single:canHandleBack() then
        self._single:back()
    elseif self._switchable:canHandleBack() then
        self._switchable:back()
    else
        log.debug('unhandled back key')
    end
end

return M
