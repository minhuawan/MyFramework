-- https://github.com/starwing/lua-protobuf
local pb = require("pb")
local protoc = require("core.pb.protoc")

---@class ProtocolManager
local M = class("ProtocolManager")

function M:loadAllProtocol(reload)
    if reload then
        protoc.reload()
    end
    local function load(path)
        local chunk = CS.MyFramework.Runtime.Utils.LuaResourceUtils.Load(path)
        log.assert(protoc:load(chunk, path))
    end

    load("pb/test.proto")
    load("pb/test2.proto")
end

return M