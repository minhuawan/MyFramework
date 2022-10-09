---@class LocalizationManager
local M = class("LocalizationManager")

function M:init()
    local config = require("config")
    local code = config['initial-language']
    assert(code, '`initial-language` not found in `config`')
    self:changeLanguage(code)
end

function M:preload()
    self.map = {}
end

function M:requireModule(name)
    log.assert(type(name) == 'string' and name ~= 'nil', 'invalid name: {}', name)
    log.assert(self.languageCode, 'invalid language code: {}', self.languageCode)
    local path = formatter.string("app.localization.{}.{}", self.languageCode, name)
    local data = self.map[name]
    if not data then
        data = require(path)
        log.assert(type(data) == 'table', 'invalid module required, path: {}', path)
        self.map[name] = data
    end
    return data
end

function M:changeLanguage(code, force)
    if self.languageCode == code and code ~= nil and not force then
        return
    end
    self.languageCode = code
    self:preload()
end

function M:getText(module, key, ...)
    log.assert(type(module) == 'string' and module ~= '', 'invalid module: `{}`', module)
    log.assert(type(key) == 'string' and key ~= '', 'invalid key: `{}`', key)
    local mod = self.map[module]
    if not mod then
        mod = self:requireModule(module)
    end
    local fmt = mod[key]
    if not fmt then
        log.error('localize key not found in {}, key: {}', module, key)
        return
    end

end

function M:localizeDate(timestamp)
    assert(false, 'unimplemented')
end

local manager = M()
manager:init()
return {
    LocalizationManager = manager
}