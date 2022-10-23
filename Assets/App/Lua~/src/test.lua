local unpack = unpack or table.unpack
local pack = pack or table.pack or function(...)
    return { ... }
end
table.pack = pack
table.unpack = unpack

require("core.class")
local subject = require("core.reactive.subject")
local observable = require("core.reactive.observable")

--local subjects = {}
--local observables = {}
--
--for i = 1, 4 do
--    local sj = subject()
--    subjects[i] = sj
--    observables[i] = observable(sj)
--end
--
--
--local outer_subject = subject()
--local outer_observable = observable(outer_subject)
--
--for i, v in ipairs(observables) do
--    v:subscribe(function()
--        outer_subject:onNext(i)
--    end)
--end
--
--outer_observable:subscribe(function(i)
--    print(i)
--end)
--
--
--subjects[2]:onNext()
--subjects[4]:onNext()


-- test merge

--local sj1 = subject()
--local sj2 = subject()
--local ob1 = observable(sj1)
--local ob2 = observable(sj2)
--
--local ob3 = ob1:merge(ob2)

--ob3:subscribe(print)

--local function select(s)
--    return string.sub(s, 1, 3)
--end

--local ob4 = ob1:merge(ob2):select(select)

--local ob5 = ob1:merge(ob2):whereEquation('==', 'hello'):select(string.sub, 1, 3)
--local ob6 = ob1:merge(ob2):whereIn({ 'hello', 'world', 'china' }):select(string.sub, 1, 3)
--local ob7 = ob1:merge(ob2):whereEquation('>=', 5)
--
----ob5:subscribe(_G.print)
----ob6:subscribe(_G.print)
--ob7:subscribe(_G.print)
--
----sj1:onNext('hel')
----sj2:onNext('world')
--for i = 1, 10 do
--    sj1:onNext(i)
--end


-- test merge & selectTo
--local sj1 = subject()
--local sj2 = subject()
--local sj3 = subject()
--local ob1 = observable(sj1):selectTo(2, 4)
--local ob2 = observable(sj2):selectTo(6, 8)
--local ob3 = observable(sj3):selectTo(1, 3)
--
--local merged1 = ob1:merge(ob2, ob3):whereEquation('>=', 0)
--local merged2 = observable(sj1, sj2, sj3)
--
--merged1:subscribe(function(...)
--    print('merge1', ...)
--end)
--merged2:subscribe(function(...)
--    print('merge2', ...)
--end)
--
--sj1:onNext('sj1')
--sj2:onNext('sj2')
--sj3:onNext('sj3')
--sj3:onNext('sj4')


-- test map
--local sj1 = subject()
--local sj2 = subject()
--
--observable(sj1, sj2):selectFrom({}):subscribe(_G.print)

local raw = "abc NL def NL 123 #y456#r"
local after = string.gsub(raw, "#(%w)", function(tag)
    print('capture', tag)
    if tag == 'y' then
        return '#FF0000'
    end
end)

print('raw:')
print(raw)
print('')
print('after:')
print(after)