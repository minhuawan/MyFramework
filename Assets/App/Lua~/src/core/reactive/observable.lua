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

--
-- extension
--
---@param ob observable
---@reutrn observable
function M:merge(ob)
    assert(ob and ob.class.__cname == "observable", 'invalid observable')
    local subject = require("core.reactive.subject")
    local sj = subject()
    local new_ob = M(sj)
    local fn = function(...)
        sj:onNext(...)
    end
    self:subscribe(fn)
    ob:subscribe(fn)
    return new_ob
end

function M:select(selectFn)
    local subject = require("core.reactive.subject")
    local sj = subject()
    local new_ob = M(sj)
    local fn = function(...)
        sj:onNext(selectFn(...))
    end
    self:subscribe(fn)
    return new_ob
end

function M:where(whereFn)
    local subject = require("core.reactive.subject")
    local sj = subject()
    local new_ob = M(sj)
    local fn = function(...)
        if whereFn(...) then
            sj:onNext(...)
        end
    end
    self:subscribe(fn)
    return new_ob
end

return M