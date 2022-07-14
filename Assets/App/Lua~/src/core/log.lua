local raw_error = _G.raw_error or error
_G.raw_error = raw_error

local function log2CSharp(type, message)
    if type == "log" then
        CS.UnityEngine.Debug.Log(message)
    elseif type == "info" then
        CS.UnityEngine.Debug.LogInfo(message)
    elseif type == "warning" then
        CS.UnityEngine.Debug.LogWarning(message)
    elseif type == "error" then
        CS.UnityEngine.Debug.LogError(message)
    end
end

local function format(fmt, ...)
    local message = tostring(fmt)
    local args = { ... }
    if #args == 0 then
        return message
    end
    local i = 0
    message = string.gsub(message, "%{%}", function(...)
        i = i + 1
        return tostring(args[i])
    end)
    return message
end

log = {
    debug = function(fmt, ...)
        local message = format(fmt, ...)
        log2CSharp("log", message)
    end,
    info = function(fmt, ...)
        local message = format(fmt, ...)
        log2CSharp("log", message)
    end,
    warn = function(fmt, ...)
        local message = format(fmt, ...)
        log2CSharp("warning", message)
    end,
    error = function(fmt, ...)
        local message = format(fmt, ...)
        log2CSharp("error", message)
    end
}