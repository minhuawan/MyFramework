local subject = require("core.reactive.subject")
local observable = require("core.reactive.observable")
---@class ButtonViewWrap
local M = class("ButtonViewWrap")

---@private
function M:ctor(buttonView)
    ---@private
    self.clickSubject = subject()
    ---@private
    self._buttonView = buttonView
    ---@private
    self._buttonView.onClick:AddListener(function()
        self.clickSubject:onNext()
    end)

    ---@public
    self.clickEvent = observable(self.clickSubject)
end

function M:dispose()
    self._buttonView.onClick:RemoveAllListeners()
    self._buttonView.onClick:Invoke()
    self._buttonView = nil
end

return M