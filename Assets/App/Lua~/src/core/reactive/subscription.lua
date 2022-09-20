---@class subscription
local M = class("subscription")

---@param subject subject
function M:ctor(subject, location)
    self._subject = subject
    self._location = location
end

function M:dispose()
    if self._subject and self._location then
        self._subject:unsubscribe(self._location)
        self._subject = nil
        self._pack = nil
    end
end

return M