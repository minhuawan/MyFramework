formatter = {}

function formatter.string(fmt, ...)
    local message = tostring(fmt)
    local n = select('#', ...)
    if n == 0 then
        return message
    end
    local t = { ... }
    local i = 0
    local str, _ = string.gsub(message, "%{%}", function()
        i = i + 1
        return tostring(t[i])
    end, n)
    return str
end

function formatter.string2(fmt, t)
    if not t then
        return fmt
    end
    assert(type(t) == 'table', 'invalid parameter: t, type: ' .. type(t))
    local message = tostring(fmt)
    local str, _ = string.gsub(message, "%{(%w+)%}", function(k)
        return tostring(t[k])
    end)
    return str
end

function formatter.int(num, fmt)
    assert(type(num) == 'number', 'invalid parameter: num, type: ' .. type(num))
    if fmt then
        assert(type(fmt) == 'string', 'invalid parameter: fmt, type: ' .. type(fmt))
        return string.format(fmt, num)
    end
    num = math.floor(num)
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

local COLOR_TAG = {

}


formatter.localize_fmt = function(fmt, ...)
    local txt = formatter.string(fmt, ...)
    local r1, _ = string.gsub(txt, " NL ", function()
        return '\n'
    end)
    local r2, _ = string.gsub(r1, "#%w", function(tag)
        return COLOR_TAG[tag]
    end)
    return r2
end
