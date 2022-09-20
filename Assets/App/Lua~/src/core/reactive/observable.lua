---@class observable
local M = class("observable")

---@param subject subject
function M:ctor(subject)
    assert(subject and subject.class.__cname == "subject", 'invalid subject')
    self._subject = subject
    return subject
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