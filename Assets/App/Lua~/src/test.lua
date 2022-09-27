require("core.class")
local subject = require("core.reactive.subject")
local observable = require("core.reactive.observable")


local sj = subject()
local ob = observable(sj)

ob:subscribe(function(word)
    print('hello', word)
end)

ob:subscribe(function(word)
    print('hello2', word)
end):dispose()

sj:onNext('world')


local ButtonViewWrap = require("app.ui.base.ButtonViewWrap")
local ButtonView
local buttonView = ButtonViewWrap(ButtonView)

buttonView.clickEvent:subscribe()
