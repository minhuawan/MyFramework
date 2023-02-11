local assert = assert
if log and log.assert then
    assert = log.assert
end


---@class observable_list
local M = class("observable_list")

function M:ctor(...)
    local pack = table.pack(...)
    self._subject = require("core.reactive.subject")()
    local function onNext(...)
        self._subject:onNext(...)
    end
    ---@param subject subject
    for i, subject in ipairs(pack) do
        assert(subject and subject.class.__cname == "subject", 'invalid subject at ' .. tostring(i))
        subject:subscribe(onNext)
    end
end

---@return subscription
function M:subscribe(fn, ...)
    assert(not self._disposed, 'observable disposed')
    return self._subject:subscribe(fn, ...)
end

function M:dispose()
    if self._subject then
        self._disposed = true
        self._subject:dispose()
        self._subject = nil
    end
end

return M