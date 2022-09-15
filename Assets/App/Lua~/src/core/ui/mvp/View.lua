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
        for _, v in self.disposable:iter() do
            if v.dispose then
                local ok, msg = pcall(v.dispose, v)
                if not ok then
                    log.error('disposed view with error, name: {}, msg: {}', self.class.__cname, msg)
                end
            end
        end
        self.disposable:clear()
    end
    self.disposable = nil
    self._localizationMeta = nil
end

function M:requireLocalizationMetadata()
    if self._localizationMeta then
        return self._localizationMeta
    end
    local name = self.class.__cname
    self._localizationMeta = require("metadata.localization.text.ui." .. name)
    return self._localizationMeta
end

function M:localizeText(key, ...)
    log.assert(type(key) == 'string', 'invalid localize key: {}, view: {}', key, self)
    if not self._localizationMeta then
        self:requireLocalizationMetadata()
    end
    if self._localizationMeta then
        local fmt = self._localizationMeta[key]
        if not fmt then
            log.error('localize key not found in metadata, key: {}, view: {}', key, self)
            return ""
        end
        return formatter.string(fmt, ...)
    end
end

return M