local raw_error = _G.raw_error or error
_G.raw_error = raw_error

local function log2CSharp(type, message)
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

local function formatSource(info, parts)
    local source = info.source
    local indexes = {}
    local index = 0
    index = string.find(source, '/src')
    while true do
        index = string.find(source, "[/\\]", index + 1)
        if not index then
            break
        end
        --print('found', source, index)
        table.insert(indexes, index)
    end

    parts = parts or {}
    local count = #indexes
    local file
    if count > 0 then
        for i = 1, count - 1 do
            table.insert(parts, string.sub(source, indexes[i] + 1, indexes[i] + 1))
            table.insert(parts, '.')
        end
        local final_index = indexes[count] + 1
        local without_ext_index = #source - 4
        file = string.sub(source, final_index, without_ext_index)
    else
        file = '?main?'
    end
    local line = info.currentline
    table.insert(parts, file)
    table.insert(parts, ':')
    table.insert(parts, line)
    return parts
end

local LEVELS = {
    ["verbose"] = { tag = "[VER] ", traceback = true },
    ["debug"] = { tag = "[DEB] ", traceback = false },
    ["info"] = { tag = "[INF] ", traceback = false },
    ["warn"] = { tag = "[WAR] ", traceback = false },
    ["error"] = { tag = "[ERR] ", traceback = true },
    ["assert"] = { tag = "[AST] ", traceback = true },
    ["assert"] = { tag = "[AST] ", traceback = true },
    ["assert-unimplemented"] = { tag = "[AST unimplemented] ", traceback = true },
}
local function makeMessage(level, fmt, ...)
    local config = LEVELS[level]
    local parts = { config.tag }
    formatSource(debug.getinfo(3), parts)
    table.insert(parts, ' ')
    local content = formatter.string(tostring(fmt), ...)
    table.insert(parts, content)
    local message = table.concat(parts)
    return message
end

log = {
    verbose = function(fmt, ...)
        local message = makeMessage("verbose", fmt, ...)
        log2CSharp("verbose", message)
    end,
    debug = function(fmt, ...)
        local message = makeMessage("debug", fmt, ...)
        log2CSharp("debug", message)
    end,
    info = function(fmt, ...)
        local message = makeMessage("info", fmt, ...)
        log2CSharp("info", message)
    end,
    warn = function(fmt, ...)
        local message = makeMessage("warn", fmt, ...)
        log2CSharp("warn", message)
    end,
    error = function(fmt, ...)
        local message = makeMessage("error", fmt, ...)
        log2CSharp("error", message)
    end,
    exception = function(fmt, ...)
        local message = makeMessage("exception", fmt, ...)
        error(message, 2) -- lua native exception
    end,
    assert = function(exp, fmt, ...)
        if exp then
        else
            local message = makeMessage("assert", fmt, ...)
            assert(false, message)
        end
    end,
    unimplemented = function(fmt, ...)
        local message = makeMessage("assert-unimplemented", fmt, ...)
        assert(false, message)
    end,
    traceback = debug.traceback,
}
