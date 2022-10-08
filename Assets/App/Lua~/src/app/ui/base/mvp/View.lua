---@class View
local M = class("View")

function M:ctor()
    self.disposable = collections.list()
end

function M:initialize(model)

end

function M:appearAsync(next)
    self.gameObject:SetActive(true)
    next()
end

function M:disappearAsync(next)
    self.gameObject:SetActive(false)
    next()
end

function M:dispose()
    if self.disposable:count() > 0 then
        for i, v in self.disposable:iter() do
            if v.dispose then
                local ok, msg = pcall(v.dispose, v)
                if not ok then
                    log.error('disposed view with error, name: {}, msg: {}', self.class.__cname, msg)
                end
            else
                log.warn('disposable object does not contain a `dispose` field, index: {}, self: {}', i, self)
            end
        end
        self.disposable:clear()
    end
    self.disposable = nil
end

function M:localizeText(key, ...)
    local module = 'text_ui_' .. self.class.__cname
    return App.localization.localizeText(module, key, ...)
end

return M