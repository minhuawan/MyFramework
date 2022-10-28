---@class RelicsManager
local M = class("RelicsManager")

function M:ctor()
end

function M:createRelicInst(setId)
    local ok, ret = pcall(function()
        local n = setId:gsub("%w+", string.capitalize):gsub(" ", "")
        local path = "app.battle.relics." .. n
        local relic = require(path)
        local inst = relic(setId)
        return inst
    end)
    if ok then
        return ret
    else
        log.error("create relic inst error with setId `{}`, msg: {}", setId, ret)
        return
    end
end

return M