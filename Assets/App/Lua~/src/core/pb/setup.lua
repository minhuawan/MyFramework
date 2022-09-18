-- https://github.com/starwing/lua-protobuf
pb = require("pb")
protoc = require("core.pb.protoc")

function protoc.loadall(reload)
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