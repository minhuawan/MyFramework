local SwitchableContextManager = require("core.ui.SwitchableContextManager")
local SingleContextManager = require("core.ui.SingleContextManager")
local M = singleton("UIManager")

function M:ctor()
    self._switchable = SwitchableContextManager.getInstance()
    self._single = SingleContextManager.getInstance()
end

function M:back()

end

return M