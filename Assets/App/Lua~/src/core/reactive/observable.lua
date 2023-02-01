---@class observable
local M = class("observable")

function M:ctor(...)
    local count = select('#', ...)
    assert(count >= 1, 'subjects are empty')
    if count == 1 then
        ---@type subject
        local subject = select(1, ...)
        assert(subject and subject.class.__cname == "subject", 'invalid subject')
        self._subject = subject
    else
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
---@reutrn observable
function M:merge(...)
    local subject = require("core.reactive.subject")
    local sj = subject()
    local new_ob = M(sj)
    local fn = function(...)
        sj:onNext(...)
    end
    self:subscribe(fn)
    ---@param ob observable
    for i, ob in ipairs(table.pack(...)) do
        assert(ob and ob.class.__cname == "observable", 'invalid observable at ' .. tostring(i))
        ob:subscribe(fn)
    end

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

function M:selectTo(...)
    local pack = table.pack(...)
    return self:select(function()
        return table.unpack(pack)
    end)
end

function M:selectFrom(tbl)
    return self:select(function(k)
        return tbl[k]
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
        elseif e == '~=' or e == '!=' then
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