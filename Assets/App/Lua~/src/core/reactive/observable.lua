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

function M:select(selectFn, ...)
    local outer_p = table.pack(...)
    local subject = require("core.reactive.subject")
    local sj = subject()
    local new_ob = M(sj)
    local fn = function(...)
        sj:onNext(selectFn(..., table.unpack(outer_p)))
    end
    self:subscribe(fn)
    return new_ob
end

function M:selectField(key)
    return self:select(function(i)
        return i[key]
    end)
end

function M:selectTo(v)
    return self:select(function()
        return v
    end)
end

function M:where(whereFn, ...)
    local outer_p = table.pack(...)
    local subject = require("core.reactive.subject")
    local sj = subject()
    local new_ob = M(sj)
    local fn = function(...)
        if whereFn(..., table.unpack(outer_p)) == true then
            sj:onNext(...)
        end
    end
    self:subscribe(fn)
    return new_ob
end

function M:whereEquation(e, v)
    return self:where(function(i)
        if e == '==' then
            return i == v
        elseif e == '>' then
            return i > v
        elseif e == '<' then
            return i < v
        elseif e == '>=' then
            return i >= v
        elseif e == '<=' then
            return i <= v
        elseif e == '~=' then
            return i ~= v
        else
            assert(false, 'invalid eType: ' .. tostring(e))
        end
    end)
end

function M:whereIn(tbl)
    assert(type(tbl) == 'table', 'invalid argument: tbl')
    return self:where(function(i)
        for k, v in pairs(tbl) do
            if v == i then
                return true
            end
        end
        return false
    end)
end

return M