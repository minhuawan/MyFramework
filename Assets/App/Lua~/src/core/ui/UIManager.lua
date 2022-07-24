local SwitchableContextManager = require("core.ui.SwitchableContextManager")
local SingleContextManager = require("core.ui.SingleContextManager")
---@class UIManager : Singleton
local M = singleton("UIManager")

function M:ctor()
    ---@type SwitchableContextManager
    self._switchable = SwitchableContextManager.getInstance()
    ---@type SingleContextManager
    self._single = SingleContextManager.getInstance()
end

function M:back()

end

function M:single(configuration)
    self._single:show(configuration)
end

function M:switch(configuration)
    self._switchable:switch(configuration)
end

function M:dispose()
    self._switchable:dispose()
    self._single:dispose()
end

return M