local LocalizeModule = require("app.localization.modules.TextLocalizeModule")
---@class LocalizationManager
local M = class("LocalizationManager")

function M:ctor()
    local config = require("config")
    local code = config['initial-language']
    assert(code, '`initial-language` not found in `config`')
    self:changeLanguage(code)
end

function M:preload()
    self.moduleMap = collections.map()
end

---@return TextLocalizeModule
function M:requireModule(name)
    log.assert(type(name) == 'string' and name ~= 'nil', 'invalid name: {}', name)
    if not self.moduleMap:has(name) then
        log.assert(self.languageCode, 'invalid language code: {}', self.languageCode)
        local path = formatter.string("localization/{}/{}.json", self.languageCode, name)
        local data = App.resources.load(path)
        log.assert(data, 'invalid path: {}', path)
        self.moduleMap:set(name, data)
        return data
    else
        return self.moduleMap:get(name)
    end
end

function M:changeLanguage(code, force)
    if self.languageCode == code and code ~= nil and not force then
        return
    end
    self.languageCode = code
    self:preload()
end

--function M:getText(module, key, ...)
--    log.assert(type(module) == 'string' and module ~= '', 'invalid module: `{}`', module)
--    log.assert(type(key) == 'string' and key ~= '', 'invalid key: `{}`', key)
--    local mod = self.moduleMap:get(module)
--    if not mod then
--        mod = self:requireModule(module)
--    end
--    local fmt = mod[key]
--    if not fmt then
--        log.error('localize key not found in {}, key: {}', module, key)
--        return
--    end
--end

function M:localizeDate(timestamp)
    assert(false, 'unimplemented')
end

---@return UILocalizeModule
function M:getUIModule()
    return self:requireModule('ui')
end

return M