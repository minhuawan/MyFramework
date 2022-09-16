formatter = {}

function formatter.string(fmt, ...)
    local message = tostring(fmt)
    local args = { ... }
    --if #args == 0 then
    --    return message
    --end
    local i = 0
    message = string.gsub(message, "%{%}", function(...)
        i = i + 1
        return tostring(args[i])
    end)
    return message
end

function formatter.number(num, fmt)
    if fmt then
        return string.format(tostring(fmt), num)
    end
    local s = tostring(num)
    if #s <= 3 then
        return s
    end
    local parts = {}
    local remain = #s % 3
    local start = 1
    if remain > 0 then
        start = remain + 1
        table.insert(parts, string.sub(s, 1, remain))
    end
    for i = start, #s, 3 do
        table.insert(parts, string.sub(s, i, i + 2))
    end

    return table.concat(parts, ',')
end

function formatter.date(timestamp)
    -- todo
end

function formatter.time(timestamp)
    -- todo
end

function formatter.datetime(timestamp)
    -- todo
end
