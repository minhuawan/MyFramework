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

local sj1 = subject()
local sj2 = subject()
local ob1 = observable(sj1)
local ob2 = observable(sj2)

local ob3 = ob1:merge(ob2)

--ob3:subscribe(print)



local ob4 = ob1:merge(ob2):select(function(s)
    return string.sub(s, 1, 3)
end)

local ob5 = ob1:merge(ob2)
               :where(
        function(s)
            if s == 'hello' then
                return true
            end
        end)
               :select(function(s)
    return string.sub(s, 1, 3)
end)

ob5:subscribe(print)

sj1:onNext('hello')
sj2:onNext('world')