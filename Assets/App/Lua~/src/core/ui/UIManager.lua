local SwitchableContextManager = require("core.ui.SwitchableContextManager")
local SingleContextManager = require("core.ui.SingleContextManager")
---@class UIManager
local M = class("UIManager")
---@type UIManager
local __instance__

function M:initialize()
    ---@type SwitchableContextManager
    self._switchable = SwitchableContextManager.getInstance()
    ---@type SingleContextManager
    self._single = SingleContextManager.getInstance()
end

function M:back()

end

function M:singleShow(configuration)
    assert(type(configuration) == 'table', 'invalid configuration')
    assert(configuration.type == 'single', 'invalid configuration type')
    self._single:singleShow(configuration)
end

function M:switchTo(configuration)
    assert(type(configuration) == 'table', 'invalid configuration')
    assert(configuration.type == 'switchable', 'invalid configuration type')
    self._switchable:switchTo(configuration)
end

function M:dispose()
    if self._switchable then
        self._switchable:dispose()
    end
    if self._single then
        self._single:dispose()
    end
end

__instance__ = M.new()

return __instance__
