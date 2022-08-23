local raw_error = _G.raw_error or error
_G.raw_error = raw_error

local function log2CSharp(type, ...)
    local message = table.concat({ "LUA: ", ... })
    if type == "verbose" then
        CS.UnityEngine.Debug.Log(message)
    elseif type == "log" then
        CS.UnityEngine.Debug.Log(message)
    elseif type == "info" then
        CS.UnityEngine.Debug.Log(message)
    elseif type == "warn" then
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
    verbose = function(fmt, ...)
        local message = format(fmt, ...)
        log2CSharp(level, message, debug.traceback(message))
    end,
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
        log2CSharp("warn", message)
    end,
    error = function(fmt, ...)
        local message = format(fmt, ...)
        log2CSharp("error", message)
    end,
    exception = function(fmt, ...)
        local message = format(fmt, ...)
        error(message) -- lua native exception
    end,
    assert = function(exp, fmt, ...)
        if exp ~= true then
            local message = format(fmt, ...)
            assert(false, message)
        end
    end,
    traceback = debug.traceback,
}
